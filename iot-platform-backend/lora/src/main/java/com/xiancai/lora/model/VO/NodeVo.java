package com.xiancai.lora.model.VO;


import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class NodeVo {
    /**
     * 节点id
     */
    private Integer id;
    /**
     * 节点的ids
     */
    private String ids;
    /**
     * 节点绑定的用户名
     */
    private String bindUsername;
    /**
     * 该节点的上级网关名称
     */
    private String parentLoraName;
    /**
     * 节点名称
     */
    private String nodeName;
    /**
     * 节点下的传感器数量
     */
    private Integer sensorNum;
    /**
     * 节点的工作状态
     */
    private String workStatus;
    /**
     * 节点的在线状态
     */
    private String onlineStatus;
    /**
     * 经度
     */
    private Float longitude;
    /**
     * 纬度
     */
    private Float latitude;
    /**
     * 节点的电量
     */
    private Integer battery;
    /**
     * 节点的电压
     */
    private Float voltage;
    /**
     * 节点的硬件版本
     */
    private String hardVersion;
    /**
     *
     */
    private String firmwareVersion;
    /**
     * 更新试卷
     */
    private Date updateTime;
    /**
     * 播报间隔试卷
     */
    private Integer reportInterval;
    /**
     * 信号强度
     */
    private Integer rssi;


    /**
     * 绑定时间
     */
    private Date bindTime;


    /**
     * 生产时间
     */
    private Date productTime;
}
