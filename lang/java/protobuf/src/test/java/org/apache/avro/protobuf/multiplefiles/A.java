// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: src/test/protobuf/test_multiple_files.proto

package org.apache.avro.protobuf.multiplefiles;

/**
 * <pre>
 * an enum
 * </pre>
 *
 * Protobuf enum {@code org.apache.avro.protobuf.multiplefiles.A}
 */
public enum A implements com.google.protobuf.ProtocolMessageEnum {
  /**
   * <code>X = 1;</code>
   */
  X(1),
  /**
   * <code>Y = 2;</code>
   */
  Y(2),
  /**
   * <code>Z = 3;</code>
   */
  Z(3),;

  /**
   * <code>X = 1;</code>
   */
  public static final int X_VALUE = 1;
  /**
   * <code>Y = 2;</code>
   */
  public static final int Y_VALUE = 2;
  /**
   * <code>Z = 3;</code>
   */
  public static final int Z_VALUE = 3;

  public final int getNumber() {
    return value;
  }

  /**
   * @param value The numeric wire value of the corresponding enum entry.
   * @return The enum associated with the given numeric wire value.
   * @deprecated Use {@link #forNumber(int)} instead.
   */
  @java.lang.Deprecated
  public static A valueOf(int value) {
    return forNumber(value);
  }

  /**
   * @param value The numeric wire value of the corresponding enum entry.
   * @return The enum associated with the given numeric wire value.
   */
  public static A forNumber(int value) {
    switch (value) {
    case 1:
      return X;
    case 2:
      return Y;
    case 3:
      return Z;
    default:
      return null;
    }
  }

  public static com.google.protobuf.Internal.EnumLiteMap<A> internalGetValueMap() {
    return internalValueMap;
  }

  private static final com.google.protobuf.Internal.EnumLiteMap<A> internalValueMap = new com.google.protobuf.Internal.EnumLiteMap<A>() {
    public A findValueByNumber(int number) {
      return A.forNumber(number);
    }
  };

  public final com.google.protobuf.Descriptors.EnumValueDescriptor getValueDescriptor() {
    return getDescriptor().getValues().get(ordinal());
  }

  public final com.google.protobuf.Descriptors.EnumDescriptor getDescriptorForType() {
    return getDescriptor();
  }

  public static final com.google.protobuf.Descriptors.EnumDescriptor getDescriptor() {
    return org.apache.avro.protobuf.multiplefiles.TestMultipleFiles.getDescriptor().getEnumTypes().get(0);
  }

  private static final A[] VALUES = values();

  public static A valueOf(com.google.protobuf.Descriptors.EnumValueDescriptor desc) {
    if (desc.getType() != getDescriptor()) {
      throw new java.lang.IllegalArgumentException("EnumValueDescriptor is not for this type.");
    }
    return VALUES[desc.getIndex()];
  }

  private final int value;

  private A(int value) {
    this.value = value;
  }

  // @@protoc_insertion_point(enum_scope:org.apache.avro.protobuf.multiplefiles.A)
}
