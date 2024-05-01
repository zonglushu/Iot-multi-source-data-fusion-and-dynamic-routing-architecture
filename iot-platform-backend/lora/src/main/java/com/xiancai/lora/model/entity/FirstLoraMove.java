package com.xiancai.lora.model.entity;

import lombok.Data;

@Data
public class FirstLoraMove {
    /**
     * 要移动的一级网关id
     */
    private Integer loraId;
    /**
     * 要调到的群组
     */
    private Integer groupId;
}
