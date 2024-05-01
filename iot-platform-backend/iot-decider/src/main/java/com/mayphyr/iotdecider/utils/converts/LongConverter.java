package com.mayphyr.iotdecider.utils.converts;

import cn.hutool.core.convert.Convert;

public class LongConverter implements TypeConverter {
    private static LongConverter instance = new LongConverter();

    private LongConverter() {}

    public static LongConverter getInstance() {
        return instance;
    }

    @Override
    public Object convert(String value) {
        return Convert.toLong(value);
    }
}