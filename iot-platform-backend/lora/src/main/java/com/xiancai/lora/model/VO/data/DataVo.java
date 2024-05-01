package com.xiancai.lora.model.VO.data;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DataVo {
    /**
     *
     */
    private String sensorName;

    /**
     *
     */

    private String data;
    /**
     *
     */
    private Date dataTime;

}
