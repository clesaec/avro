/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.avro.reflect;

import java.io.IOException;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Field;

import org.apache.avro.AvroRuntimeException;
import org.apache.avro.io.Decoder;
import org.apache.avro.io.Encoder;

public class FieldAccessHandle extends FieldAccess {

  @Override
  protected FieldAccessor getAccessor(Field field) {
    AvroEncode enc = field.getAnnotation(AvroEncode.class);
    if (enc != null) {
      try {
        final CustomEncoding<?> customEncoding = enc.using().getDeclaredConstructor().newInstance();
        return new HandleCustomEncodedFieldAccessor(field, customEncoding);
      } catch (ReflectiveOperationException ex) {
        throw new AvroRuntimeException("Could not instantiate custom Encoding for " + enc.using().getName(), ex);
      }
    } else {
      return new HandleFieldAccessor(field);
    }
  }

  private static class HandleFieldAccessor extends FieldAccessor {

    private final Field field;

    private final boolean isStringable;

    private final MethodHandle getter;

    private final MethodHandle setter;

    private final ReaderWriter rw;

    public HandleFieldAccessor(Field field) {
      this.field = field;
      this.field.setAccessible(true);
      this.isStringable = field.isAnnotationPresent(Stringable.class);

      this.rw = new ReaderWriter(field.getType());
      try {
        this.getter = MethodHandles.lookup().unreflectGetter(this.field);
        this.setter = MethodHandles.lookup().unreflectSetter(this.field);
      } catch (IllegalAccessException ex) {
        throw new AvroRuntimeException("Can't initiate field getter/setter for " + this.getFieldLongName(), ex);
      }

    }

    @Override
    protected Object get(Object object) {
      try {
        return this.getter.invoke(object);
      } catch (Throwable ex) {
        throw new AvroRuntimeException(
            "Can't get value for field " + getFieldLongName() + "for object " + this.getValueObject(object), ex);
      }
    }

    @Override
    protected void set(Object object, Object value) {
      try {
        this.setter.invoke(object, value);
      } catch (Throwable ex) {
        throw new AvroRuntimeException("Can't set value for field " + getFieldLongName() + "for object "
            + this.getValueObject(object) + " and value " + this.getValueObject(value), ex);
      }
    }

    @Override
    protected Field getField() {
      return this.field;
    }

    @Override
    protected boolean supportsIO() {
      return true;
    }

    @Override
    protected boolean isStringable() {
      return this.isStringable;
    }

    private String getValueObject(Object obj) {
      if (obj == null) {
        return "'Null object'";
      } else {
        return obj.getClass().getName();
      }
    }

    private String getFieldLongName() {
      return this.field.getDeclaringClass().getName() + "." + this.field.getName();
    }

    @Override
    protected void read(Object object, Decoder in) throws IOException {
      this.set(object, this.rw.reader.read(in));
    }

    @Override
    protected void write(Object object, Encoder out) throws IOException {
      final Object value = this.get(object);
      if (value == null) {
        out.writeNull();
      } else {
        this.rw.writer.write(out, value);
      }
    }
  }

  @FunctionalInterface
  interface ReaderFunction {
    Object read(Decoder decoder) throws IOException;
  }

  @FunctionalInterface
  interface WriterFunction {
    void write(Encoder encoder, Object value) throws IOException;
  }

  private static class ReaderWriter {

    public final ReaderFunction reader;

    public final WriterFunction writer;

    public ReaderWriter(Class<?> type) {
      if (String.class.equals(type)) {
        this.reader = Decoder::readString;
        this.writer = (Encoder encoder, Object value) -> encoder.writeString((String) value);
      } else if (int.class.equals(type) || Integer.class.equals(type)) {
        this.reader = Decoder::readInt;
        this.writer = (Encoder encoder, Object value) -> encoder.writeInt((int) value);
      } else if (long.class.equals(type) || Long.class.equals(type)) {
        this.reader = Decoder::readLong;
        this.writer = (Encoder encoder, Object value) -> encoder.writeLong((long) value);
      } else if (boolean.class.equals(type) || Boolean.class.equals(type)) {
        this.reader = Decoder::readBoolean;
        this.writer = (Encoder encoder, Object value) -> encoder.writeBoolean((boolean) value);
      } else if (double.class.equals(type) || Double.class.equals(type)) {
        this.reader = Decoder::readDouble;
        this.writer = (Encoder encoder, Object value) -> encoder.writeDouble((double) value);
      } else if (float.class.equals(type) || Float.class.equals(type)) {
        this.reader = Decoder::readFloat;
        this.writer = (Encoder encoder, Object value) -> encoder.writeFloat((float) value);
      } else {
        throw new AvroRuntimeException("Can't find read/write for type " + type.getName());
      }

    }

  }

  private final static class HandleCustomEncodedFieldAccessor extends HandleFieldAccessor {

    private CustomEncoding<?> encoding;

    HandleCustomEncodedFieldAccessor(Field f, CustomEncoding<?> encoding) {
      super(f);
      this.encoding = encoding;
    }

    @Override
    protected void read(Object object, Decoder in) throws IOException {
      this.set(object, encoding.read(in));
    }

    @Override
    protected void write(Object object, Encoder out) throws IOException {
      encoding.write(this.get(object), out);
    }

    @Override
    protected boolean isCustomEncoded() {
      return true;
    }
  }
}
