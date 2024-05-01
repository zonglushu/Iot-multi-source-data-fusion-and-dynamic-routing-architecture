package com.mayphyr.iotcommon.enums;

import cn.hutool.core.util.ObjectUtil;


import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static cn.hutool.core.util.ObjectUtil.isEmpty;

/**
 * 接口状态枚举类
 *
 */
public enum InterfaceInfoStatusEnum {

    /**
     * 接口状态枚举值
     */
    OFFLINE("关闭", 0),
    ONLINE("上线", 1);

    private final String text;

    private final Integer value;

    InterfaceInfoStatusEnum(String text, Integer value) {
        this.text = text;
        this.value = value;
    }

    /**
     * 获取值列表
     *
     * @return
     */
    public static List<Integer> getValues() {
        return Arrays.stream(values()).map(item -> item.value).collect(Collectors.toList());
    }

    /**
     * 根据 value 获取枚举
     *
     * @param value
     * @return
     */
    public static InterfaceInfoStatusEnum getEnumByValue(String value) {
        if (ObjectUtil.isEmpty(value)) {
            return null;
        }
        for (InterfaceInfoStatusEnum anEnum : InterfaceInfoStatusEnum.values()) {
            if (anEnum.value.equals(value)) {
                return anEnum;
            }
        }
        return null;
    }

    public Integer getValue() {
        return value;
    }

    public String getText() {
        return text;
    }
}
