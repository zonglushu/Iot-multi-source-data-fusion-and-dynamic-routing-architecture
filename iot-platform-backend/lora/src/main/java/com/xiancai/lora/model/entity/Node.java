package com.xiancai.lora.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 
 * @TableName node
 */
@TableName(value ="node")
@Data
@Builder
@AllArgsConstructor
public class Node extends TreeNode implements Serializable {
    /**
     * 
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 
     */
    @TableField(value = "ids")
    private String ids;

    /**
     * 
     */
    @TableField(value = "user_id")
    private Integer userId;

    /**
     * 
     */
    @TableField(value = "lora_id")
    private Integer loraId;

    /**
     * 
     */
    @TableField(value = "node_name")
    private String nodeName;

    /**
     * 
     */
    @TableField(value = "sensor_num")
    private Integer sensorNum;

    /**
     * 
     */
    @TableField(value = "work_status")
    private String workStatus;

    /**
     * 
     */
    @TableField(value = "online_status")
    private String onlineStatus;

    /**
     * 经度
     */
    @TableField(value = "longitude")
    private Float longitude;

    /**
     * 纬度
     */
    @TableField(value = "latitude")
    private Float latitude;

    /**
     * 节点的电量
     */
    @TableField(value = "battery")
    private Integer battery;

    /**
     * 电压
     */
    @TableField(value = "voltage")
    private Double voltage;

    /**
     * 硬件版本号
     */
    @TableField(value = "hard_version")
    private String hardVersion;

    /**
     * 固件版本号
     */
    @TableField(value = "firmware_version")
    private String firmwareVersion;

    /**
     * 
     */
    @TableField(value = "update_time")
    private Date updateTime;

    /**
     * 上报周期
     */
    @TableField(value = "report_interval")
    private Integer reportInterval;

    /**
     * 
     */
    @TableField(value = "bind_time")
    private Date bindTime;

    /**
     * 
     */
    @TableField(value = "product_time")
    private Date productTime;

    /**
     * 信号强度
     */
    @TableField(value = "rssi")
    private Integer rssi;

    /**
     * 判断该节点是否是网关，如果是网关，该属性是对应网关表的id,如果不是那就是-1
     */
    @TableField(value = "is_lora")
    private Integer isLora;

    /**
     * 每一层的网关节点的唯一标识，用来通讯
     */
    @TableField(value = "domain")
    private String domain;

    /**
     * 节点的发射功率
     */
    @TableField(value = "txpower")
    private Integer txPower;

    /**
     * 数据的渠道
     */
    @TableField(value = "channel")
    private Integer channel;
    /**
     * 结点的io口信息，前8个是 i 口，后8个是 o 口，1 代表开启，0 代表关闭
     */
    @TableField(value = "io")
    private Integer IO;

    public Node() {
    }

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}