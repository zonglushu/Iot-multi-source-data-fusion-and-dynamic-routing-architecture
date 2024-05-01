package com.mayphyr.iotcommon.utils.converts;

public class TypeConverterFactory {
    public static TypeConverter getTypeConverter(String type) {
        switch (type) {
            case "int":
                return LongConverter.getInstance();
            case "boolean":
                return BooleanConverter.getInstance();
            case "object":
                return ObjectConverter.getInstance();
            case "list":
                return ListConverter.getInstance();
            default:
                throw new IllegalArgumentException("Unsupported type: " + type);
        }
    }
}