package com.mayphyr.iotbi.entity.vo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BIResponse {
    private String genChart;

    private String genResult;

    private Long chartId;
}
