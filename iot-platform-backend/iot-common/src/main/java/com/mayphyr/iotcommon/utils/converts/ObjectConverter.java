package com.mayphyr.iotcommon.utils.converts;

import cn.hutool.json.JSONUtil;

public class ObjectConverter implements TypeConverter {
    private static ObjectConverter instance = new ObjectConverter();

    private ObjectConverter() {}

    public static ObjectConverter getInstance() {
        return instance;
    }

    @Override
    public Object convert(String value) {
        return JSONUtil.parseObj(value);
    }
}