package com.xiancai.lora.enums;

public enum QosEnum {
    QOS0(0),
    QOS1(1),
    QOS2(2);
    private final int value;

    QosEnum(int value) {
        this.value = value;
    }
    public int value(){
        return this.value;
    }
}
