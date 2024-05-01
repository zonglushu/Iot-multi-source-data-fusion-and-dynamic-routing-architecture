package com.xiancai.lora.model.DTO.mqtt;

import io.swagger.models.auth.In;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MQTTModuleSensorData {
    private Integer[] scmd;
}
