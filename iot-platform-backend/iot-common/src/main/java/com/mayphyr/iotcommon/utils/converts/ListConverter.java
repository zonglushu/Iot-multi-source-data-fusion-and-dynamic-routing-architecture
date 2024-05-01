package com.mayphyr.iotcommon.utils.converts;

import cn.hutool.core.convert.Convert;

public class ListConverter implements TypeConverter {
    private static ListConverter instance = new ListConverter();

    private ListConverter() {}

    public static ListConverter getInstance() {
        return instance;
    }

    @Override
    public Object convert(String value) {
        return Convert.toList(value);
    }
}