// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: src/test/protobuf/test_multiple_files.proto

package org.apache.avro.protobuf.multiplefiles;

public interface FooOrBuilder extends
    // @@protoc_insertion_point(interface_extends:org.apache.avro.protobuf.multiplefiles.Foo)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <pre>
   * all the primitive types
   * </pre>
   *
   * <code>required int32 int32 = 1;</code>
   * 
   * @return Whether the int32 field is set.
   */
  boolean hasInt32();

  /**
   * <pre>
   * all the primitive types
   * </pre>
   *
   * <code>required int32 int32 = 1;</code>
   * 
   * @return The int32.
   */
  int getInt32();

  /**
   * <code>optional int64 int64 = 2;</code>
   * 
   * @return Whether the int64 field is set.
   */
  boolean hasInt64();

  /**
   * <code>optional int64 int64 = 2;</code>
   * 
   * @return The int64.
   */
  long getInt64();

  /**
   * <code>optional uint32 uint32 = 3;</code>
   * 
   * @return Whether the uint32 field is set.
   */
  boolean hasUint32();

  /**
   * <code>optional uint32 uint32 = 3;</code>
   * 
   * @return The uint32.
   */
  int getUint32();

  /**
   * <code>optional uint64 uint64 = 4;</code>
   * 
   * @return Whether the uint64 field is set.
   */
  boolean hasUint64();

  /**
   * <code>optional uint64 uint64 = 4;</code>
   * 
   * @return The uint64.
   */
  long getUint64();

  /**
   * <code>optional sint32 sint32 = 5;</code>
   * 
   * @return Whether the sint32 field is set.
   */
  boolean hasSint32();

  /**
   * <code>optional sint32 sint32 = 5;</code>
   * 
   * @return The sint32.
   */
  int getSint32();

  /**
   * <code>optional sint64 sint64 = 6;</code>
   * 
   * @return Whether the sint64 field is set.
   */
  boolean hasSint64();

  /**
   * <code>optional sint64 sint64 = 6;</code>
   * 
   * @return The sint64.
   */
  long getSint64();

  /**
   * <code>optional fixed32 fixed32 = 7;</code>
   * 
   * @return Whether the fixed32 field is set.
   */
  boolean hasFixed32();

  /**
   * <code>optional fixed32 fixed32 = 7;</code>
   * 
   * @return The fixed32.
   */
  int getFixed32();

  /**
   * <code>optional fixed64 fixed64 = 8;</code>
   * 
   * @return Whether the fixed64 field is set.
   */
  boolean hasFixed64();

  /**
   * <code>optional fixed64 fixed64 = 8;</code>
   * 
   * @return The fixed64.
   */
  long getFixed64();

  /**
   * <code>optional sfixed32 sfixed32 = 9;</code>
   * 
   * @return Whether the sfixed32 field is set.
   */
  boolean hasSfixed32();

  /**
   * <code>optional sfixed32 sfixed32 = 9;</code>
   * 
   * @return The sfixed32.
   */
  int getSfixed32();

  /**
   * <code>optional sfixed64 sfixed64 = 10;</code>
   * 
   * @return Whether the sfixed64 field is set.
   */
  boolean hasSfixed64();

  /**
   * <code>optional sfixed64 sfixed64 = 10;</code>
   * 
   * @return The sfixed64.
   */
  long getSfixed64();

  /**
   * <code>optional float float = 11;</code>
   * 
   * @return Whether the float field is set.
   */
  boolean hasFloat();

  /**
   * <code>optional float float = 11;</code>
   * 
   * @return The float.
   */
  float getFloat();

  /**
   * <code>optional double double = 12;</code>
   * 
   * @return Whether the double field is set.
   */
  boolean hasDouble();

  /**
   * <code>optional double double = 12;</code>
   * 
   * @return The double.
   */
  double getDouble();

  /**
   * <code>optional bool bool = 13;</code>
   * 
   * @return Whether the bool field is set.
   */
  boolean hasBool();

  /**
   * <code>optional bool bool = 13;</code>
   * 
   * @return The bool.
   */
  boolean getBool();

  /**
   * <code>optional string string = 14;</code>
   * 
   * @return Whether the string field is set.
   */
  boolean hasString();

  /**
   * <code>optional string string = 14;</code>
   * 
   * @return The string.
   */
  java.lang.String getString();

  /**
   * <code>optional string string = 14;</code>
   * 
   * @return The bytes for string.
   */
  com.google.protobuf.ByteString getStringBytes();

  /**
   * <code>optional bytes bytes = 15;</code>
   * 
   * @return Whether the bytes field is set.
   */
  boolean hasBytes();

  /**
   * <code>optional bytes bytes = 15;</code>
   * 
   * @return The bytes.
   */
  com.google.protobuf.ByteString getBytes();

  /**
   * <code>optional .org.apache.avro.protobuf.multiplefiles.A enum = 16 [default = Z];</code>
   * 
   * @return Whether the enum field is set.
   */
  boolean hasEnum();

  /**
   * <code>optional .org.apache.avro.protobuf.multiplefiles.A enum = 16 [default = Z];</code>
   * 
   * @return The enum.
   */
  org.apache.avro.protobuf.multiplefiles.A getEnum();

  /**
   * <pre>
   * some repeated types
   * </pre>
   *
   * <code>repeated int32 intArray = 17;</code>
   * 
   * @return A list containing the intArray.
   */
  java.util.List<java.lang.Integer> getIntArrayList();

  /**
   * <pre>
   * some repeated types
   * </pre>
   *
   * <code>repeated int32 intArray = 17;</code>
   * 
   * @return The count of intArray.
   */
  int getIntArrayCount();

  /**
   * <pre>
   * some repeated types
   * </pre>
   *
   * <code>repeated int32 intArray = 17;</code>
   * 
   * @param index The index of the element to return.
   * @return The intArray at the given index.
   */
  int getIntArray(int index);

  /**
   * <code>repeated .org.apache.avro.protobuf.multiplefiles.Foo fooArray = 20;</code>
   */
  java.util.List<org.apache.avro.protobuf.multiplefiles.Foo> getFooArrayList();

  /**
   * <code>repeated .org.apache.avro.protobuf.multiplefiles.Foo fooArray = 20;</code>
   */
  org.apache.avro.protobuf.multiplefiles.Foo getFooArray(int index);

  /**
   * <code>repeated .org.apache.avro.protobuf.multiplefiles.Foo fooArray = 20;</code>
   */
  int getFooArrayCount();

  /**
   * <code>repeated .org.apache.avro.protobuf.multiplefiles.Foo fooArray = 20;</code>
   */
  java.util.List<? extends org.apache.avro.protobuf.multiplefiles.FooOrBuilder> getFooArrayOrBuilderList();

  /**
   * <code>repeated .org.apache.avro.protobuf.multiplefiles.Foo fooArray = 20;</code>
   */
  org.apache.avro.protobuf.multiplefiles.FooOrBuilder getFooArrayOrBuilder(int index);

  /**
   * <code>repeated .org.apache.avro.protobuf.multiplefiles.A syms = 19;</code>
   * 
   * @return A list containing the syms.
   */
  java.util.List<org.apache.avro.protobuf.multiplefiles.A> getSymsList();

  /**
   * <code>repeated .org.apache.avro.protobuf.multiplefiles.A syms = 19;</code>
   * 
   * @return The count of syms.
   */
  int getSymsCount();

  /**
   * <code>repeated .org.apache.avro.protobuf.multiplefiles.A syms = 19;</code>
   * 
   * @param index The index of the element to return.
   * @return The syms at the given index.
   */
  org.apache.avro.protobuf.multiplefiles.A getSyms(int index);

  /**
   * <pre>
   * a recursive type
   * </pre>
   *
   * <code>optional .org.apache.avro.protobuf.multiplefiles.Foo foo = 18;</code>
   * 
   * @return Whether the foo field is set.
   */
  boolean hasFoo();

  /**
   * <pre>
   * a recursive type
   * </pre>
   *
   * <code>optional .org.apache.avro.protobuf.multiplefiles.Foo foo = 18;</code>
   * 
   * @return The foo.
   */
  org.apache.avro.protobuf.multiplefiles.Foo getFoo();

  /**
   * <pre>
   * a recursive type
   * </pre>
   *
   * <code>optional .org.apache.avro.protobuf.multiplefiles.Foo foo = 18;</code>
   */
  org.apache.avro.protobuf.multiplefiles.FooOrBuilder getFooOrBuilder();

  /**
   * <pre>
   * a predefined message type
   * </pre>
   *
   * <code>optional .google.protobuf.Timestamp timestamp = 21;</code>
   * 
   * @return Whether the timestamp field is set.
   */
  boolean hasTimestamp();

  /**
   * <pre>
   * a predefined message type
   * </pre>
   *
   * <code>optional .google.protobuf.Timestamp timestamp = 21;</code>
   * 
   * @return The timestamp.
   */
  com.google.protobuf.Timestamp getTimestamp();

  /**
   * <pre>
   * a predefined message type
   * </pre>
   *
   * <code>optional .google.protobuf.Timestamp timestamp = 21;</code>
   */
  com.google.protobuf.TimestampOrBuilder getTimestampOrBuilder();
}
