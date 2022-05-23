package org.apache.avro.reflect;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Arrays;
import java.util.Collections;

import org.apache.avro.Schema;
import org.apache.avro.file.CodecFactory;
import org.apache.avro.file.DataFileStream;
import org.apache.avro.file.DataFileWriter;
import org.apache.avro.io.DatumReader;
import org.apache.avro.io.Decoder;
import org.apache.avro.io.Encoder;
import org.junit.Test;

public class CustomEncodingTest {


  @Test
  public void testReadSchemaIsSet() throws IOException {
    Custom in = new Custom("hello", "world");
    byte[] encoded = write(in);

    CustomEncoder.SCHEMA = CustomEncoder.v2Schema;
    Custom decoded = read(encoded);
    assertNotNull(decoded);
    assertEquals("hello", decoded.v1Field);
    assertNull(decoded.v2Field);
    encoded = write(in);

    decoded = read(encoded);
    assertNotNull(decoded);
    assertEquals("hello", decoded.v1Field);
    assertEquals("world", decoded.v2Field);
  }

  private Custom read(byte[] toDecode) throws IOException {
    DatumReader<Wrapper> datumReader = new ReflectDatumReader<>(null, null, new ReflectData());
    DataFileStream<Wrapper> dataFileReader = new DataFileStream<>(
      new ByteArrayInputStream(toDecode, 0, toDecode.length), datumReader);
    Wrapper wrapper = null;
    if (dataFileReader.hasNext()) {
      wrapper = dataFileReader.next();
    }
    return wrapper.custom;
  }

  private byte[] write(Custom custom) {
    ReflectData rd = new ReflectData();
    Schema schema = rd.getSchema(Wrapper.class);
    ReflectDatumWriter<Wrapper> datumWriter = new ReflectDatumWriter<>(rd);
    try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
         DataFileWriter<Wrapper> writer = new DataFileWriter<>(datumWriter)) {
      writer.setCodec(CodecFactory.snappyCodec());
      writer.create(schema, baos);
      writer.append(new Wrapper(custom));
      writer.flush();
      return baos.toByteArray();
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }

  public static class Custom {
    String v1Field;
    String v2Field;

    Custom(String v1Field, String v2Field) {
      this.v1Field = v1Field;
      this.v2Field = v2Field;
    }
  }

  public static class Wrapper {
    @AvroEncode(using = CustomEncoder.class)
    Custom custom;

    Wrapper() {
    }

    Wrapper(Custom custom) {
      this.custom = custom;
    }
  }

  public static class CustomEncoder extends CustomEncoding<Custom> {

    protected static Schema v1Schema = Schema.createRecord("Custom", null, null, false,
      Collections.singletonList(new Schema.Field("v1Field", Schema.create(Schema.Type.STRING), null, null)));

    protected static Schema v2Schema = Schema.createRecord("Custom", null, null, false,
      Arrays.asList(new Schema.Field("v1Field", Schema.create(Schema.Type.STRING), null, null),
        new Schema.Field("v2Field", Schema.create(Schema.Type.STRING), null, null)));

    protected static Schema SCHEMA = v1Schema;

    {
      schema = SCHEMA;
    }

    @Override
    protected void write(Object datum, Encoder out) throws IOException {
      Custom custom = (Custom) datum;
      if (SCHEMA == v1Schema) {
        out.writeString(custom.v1Field);
      } else {
        out.writeString(custom.v1Field);
        out.writeString(custom.v2Field);
      }
    }

    @Override
    protected Custom read(Object reuse, Decoder in, Schema.Field field) throws IOException {
      final String v1Field = in.readString();
      final String v2Field;
      if (field.schema().getField("v2Field") != null) {
        v2Field = in.readString();
      } else {
        v2Field = null;
      }
      return new Custom(v1Field, v2Field);
    }

    @Override
    protected Schema getSchema() {
      return super.getSchema();
    }
  }
}
