package com.mayphyr.iotdecider.utils.converts;

import cn.hutool.core.convert.Convert;

public class BooleanConverter implements TypeConverter {
    private static BooleanConverter instance = new BooleanConverter();

    private BooleanConverter() {}

    public static BooleanConverter getInstance() {
        return instance;
    }

    @Override
    public Object convert(String value) {
        return Convert.toBool(value);
    }
}