package com.mayphyr.iotbi.entity.dto.chart;

import lombok.Data;

import java.io.Serializable;

@Data
public class GenChartByAiRequest implements Serializable {

    /**
     * 图表名称
     */
    private String name;

    /**
     * 图表目标
     */
    private String goal;

    /**
     * 图表类型
     */
    private String chartType;

    private static final long serialVersionUID=1l;
}
