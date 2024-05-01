package com.xiancai.lora.model.DTO.mqtt;

import lombok.Data;

/**
 * 修改节点上报周期的DTO
 */
@Data
public class UpdateInterval {
    /**
     * 节点Id
     */
    private Integer nodeId;
    /**
     * 新的 上报周期
     */
    private Integer reportInterval;
}
