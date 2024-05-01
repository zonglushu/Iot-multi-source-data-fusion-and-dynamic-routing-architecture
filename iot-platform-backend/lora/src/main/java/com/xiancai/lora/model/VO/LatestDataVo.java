package com.xiancai.lora.model.VO;

import lombok.Builder;
import lombok.Data;
import com.xiancai.lora.model.entity.DataS;
@Data
@Builder
public class LatestDataVo {
    private String sensorName;

    private DataS sensorData;

}
