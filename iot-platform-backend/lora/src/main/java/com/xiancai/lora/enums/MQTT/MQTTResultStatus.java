package com.xiancai.lora.enums.MQTT;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MQTTResultStatus {
    FAIL("-1"),

    SUCCESS("0"),

    WARNING("1"),
    /**
     * 21代表错误类型为errcode表对应的错误码为1的错误
     */
    ERROR("2");

    private final String status;
}
