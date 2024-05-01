package com.xiancai.lora.model.DTO.mqtt.IO;

import lombok.Data;

@Data
public class IoInfo {
    /**
     * 结点id
     */
    private Integer nodeId;
    /**
     * 要设置的状态 1-开，0-关
     */
    private Boolean status;
    /**
     * 要设置哪个i/o口
     */
    private Integer ox;


}
