package com.xiancai.lora.model.VO.mqtt;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LEDStatus {
    private String status;
}
