// Licensed to the Apache Software Foundation (ASF) under one
// or more contributor license agreements.  See the NOTICE file
// distributed with this work for additional information
// regarding copyright ownership.  The ASF licenses this file
// to you under the Apache License, Version 2.0 (the
// "License"); you may not use this file except in compliance
// with the License.  You may obtain a copy of the License at
//
//   http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing,
// software distributed under the License is distributed on an
// "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
// KIND, either express or implied.  See the License for the
// specific language governing permissions and limitations
// under the License.

use crate::{
    decode::{decode_len, decode_long},
    encode::{encode_bytes, encode_long},
    types::Value,
    AvroResult, Error,
};
use bigdecimal::BigDecimal;
use num_bigint::{BigInt, Sign};
use std::io::Read;

#[derive(Debug, Clone)]
pub struct Decimal {
    value: BigInt,
    len: usize,
}

// We only care about value equality, not byte length. Can two equal `BigInt`s have two different
// byte lengths?
impl PartialEq for Decimal {
    fn eq(&self, other: &Self) -> bool {
        self.value == other.value
    }
}

impl Decimal {
    pub(crate) fn len(&self) -> usize {
        self.len
    }

    pub(crate) fn to_vec(&self) -> AvroResult<Vec<u8>> {
        self.to_sign_extended_bytes_with_len(self.len)
    }

    pub(crate) fn to_sign_extended_bytes_with_len(&self, len: usize) -> AvroResult<Vec<u8>> {
        let sign_byte = 0xFF * u8::from(self.value.sign() == Sign::Minus);
        let mut decimal_bytes = vec![sign_byte; len];
        let raw_bytes = self.value.to_signed_bytes_be();
        let num_raw_bytes = raw_bytes.len();
        let start_byte_index = len.checked_sub(num_raw_bytes).ok_or(Error::SignExtend {
            requested: len,
            needed: num_raw_bytes,
        })?;
        decimal_bytes[start_byte_index..].copy_from_slice(&raw_bytes);
        Ok(decimal_bytes)
    }
}

impl From<Decimal> for BigInt {
    fn from(decimal: Decimal) -> Self {
        decimal.value
    }
}

/// Gets the internal byte array representation of a referenced decimal.
/// Usage:
/// ```
/// use apache_avro::Decimal;
/// use std::convert::TryFrom;
///
/// let decimal = Decimal::from(vec![1, 24]);
/// let maybe_bytes = <Vec<u8>>::try_from(&decimal);
/// ```
impl std::convert::TryFrom<&Decimal> for Vec<u8> {
    type Error = Error;

    fn try_from(decimal: &Decimal) -> Result<Self, Self::Error> {
        decimal.to_vec()
    }
}

/// Gets the internal byte array representation of an owned decimal.
/// Usage:
/// ```
/// use apache_avro::Decimal;
/// use std::convert::TryFrom;
///
/// let decimal = Decimal::from(vec![1, 24]);
/// let maybe_bytes = <Vec<u8>>::try_from(decimal);
/// ```
impl std::convert::TryFrom<Decimal> for Vec<u8> {
    type Error = Error;

    fn try_from(decimal: Decimal) -> Result<Self, Self::Error> {
        decimal.to_vec()
    }
}

impl<T: AsRef<[u8]>> From<T> for Decimal {
    fn from(bytes: T) -> Self {
        let bytes_ref = bytes.as_ref();
        Self {
            value: BigInt::from_signed_bytes_be(bytes_ref),
            len: bytes_ref.len(),
        }
    }
}

pub(crate) fn serialize_big_decimal(decimal: &BigDecimal) -> Vec<u8> {
    let mut buffer: Vec<u8> = Vec::new();
    let (big_int, exponent): (BigInt, i64) = decimal.as_bigint_and_exponent();
    let big_endian_value: Vec<u8> = big_int.to_signed_bytes_be();
    encode_bytes(&big_endian_value, &mut buffer);
    encode_long(exponent, &mut buffer);

    buffer
}

pub(crate) fn deserialize_big_decimal<R: Read>(reader: &mut R) -> Result<BigDecimal, Error> {
    let mut big_decimal_buffer = match decode_len(reader) {
        Ok(size) => vec![0u8; size],
        Err(err) => return Err(Error::BigDecimalLen(Box::new(err))),
    };

    reader
        .read_exact(&mut big_decimal_buffer[..])
        .map_err(Error::ReadDouble)?;

    let big_int: BigInt = BigInt::from_signed_bytes_be(&big_decimal_buffer);

    match decode_long(reader) {
        Ok(Value::Long(scale_value)) => {
            let decimal = BigDecimal::new(big_int, scale_value);
            Ok(decimal)
        }
        _ => Err(Error::BigDecimalScale),
    }
}

#[cfg(test)]
mod tests {
    use super::*;
    use apache_avro_test_helper::TestResult;
    use bigdecimal::{One, Zero};
    use pretty_assertions::assert_eq;
    use std::{
        convert::TryFrom,
        ops::{Div, Mul},
    };

    use crate::{Codec, Reader, Schema, Writer};
    use crate::types::Record;
    use crate::types::Value;

    #[test]
    fn test_decimal_from_bytes_from_ref_decimal() -> TestResult {
        let input = vec![1, 24];
        let d = Decimal::from(&input);

        let output = <Vec<u8>>::try_from(&d)?;
        assert_eq!(output, input);

        Ok(())
    }

    #[test]
    fn test_decimal_from_bytes_from_owned_decimal() -> TestResult {
        let input = vec![1, 24];
        let d = Decimal::from(&input);

        let output = <Vec<u8>>::try_from(d)?;
        assert_eq!(output, input);

        Ok(())
    }

    #[test]
    fn test_avro_3779_bigdecimal_serial() -> TestResult {
        let value: bigdecimal::BigDecimal =
            bigdecimal::BigDecimal::from(-1421).div(bigdecimal::BigDecimal::from(2));
        let mut current: bigdecimal::BigDecimal = bigdecimal::BigDecimal::one();

        for iter in 1..180 {
            let result: Vec<u8> = serialize_big_decimal(&current);

            let deserialize_big_decimal: Result<bigdecimal::BigDecimal, Error> =
                deserialize_big_decimal(&mut result.as_slice());
            assert!(
                deserialize_big_decimal.is_ok(),
                "can't deserialize for iter {iter}"
            );
            assert_eq!(
                current,
                deserialize_big_decimal.unwrap(),
                "not equals for ${iter}"
            );
            current = current.mul(&value);
        }

        let result: Vec<u8> = serialize_big_decimal(&BigDecimal::zero());
        let deserialize_big_decimal: Result<bigdecimal::BigDecimal, Error> =
            deserialize_big_decimal(&mut result.as_slice());
        assert!(
            deserialize_big_decimal.is_ok(),
            "can't deserialize for zero"
        );
        assert_eq!(
            BigDecimal::zero(),
            deserialize_big_decimal.unwrap(),
            "not equals for zero"
        );

        Ok(())
    }

    #[test]
    fn other() -> TestResult {
        let schema_str = r#"
    {
      "type": "record",
      "name": "test",
      "fields": [
        {
          "name": "field_name",
          "type": "bytes",
          "logicalType": "big-decimal"
        }
      ]
    }
    "#;
        let schema = Schema::parse_str(schema_str)?;

        // build record with big decimal value
        let mut record = Record::new(&schema).unwrap();
        let val = BigDecimal::new(BigInt::from(12), 2);
        record.put("field_name", val.clone());

        // save record in writer
        let codec = Codec::Null;
        let mut writer = Writer::builder()
            .schema(&schema)
            .codec(codec)
            .writer(Vec::new())
            .build();

        let result = writer.append(record.clone());
        assert!(result.is_ok(), "Append KO  : {}", result.unwrap_err());
        assert!(writer.flush().is_ok(), "Flush KO");

        // read record
        let wrote_data = writer.into_inner().unwrap();
        let mut reader = Reader::new(&wrote_data[..])?;

        let v = reader.next().unwrap();
        assert!(v.is_ok(), "reader next didn't work : {}", v.unwrap_err());
        let value = v.unwrap();

        // extract field value
        let big_decimal_result: Result<&Value, &str> = match value {
            Value::Record(ref fields) => Ok(&fields[0].1),
            _ => Err("should be record"),
        };
        assert!(big_decimal_result.is_ok());
        let big_decimal_value = big_decimal_result.unwrap();
        let x1res: Result<&BigDecimal, &str> = match big_decimal_value {
            Value::BigDecimal(ref s) => Ok(s),
            _ => Err("Not bg"),
        };
        assert!(x1res.is_ok(), "res is not big decimal");
        assert_eq!(&val, x1res.unwrap());

        Ok(())
    }
}
