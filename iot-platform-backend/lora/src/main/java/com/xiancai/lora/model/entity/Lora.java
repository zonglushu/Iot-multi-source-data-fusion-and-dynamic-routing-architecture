package com.xiancai.lora.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName lora
 */
@TableName(value ="lora")
@Data
public class Lora extends TreeNode implements Serializable  {
    /**
     * 
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 
     */
    @TableField(value = "lora_name")
    private String loraName;

    /**
     * 
     */
    @TableField(value = "mqtt_id")
    private Integer mqttId;

    /**
     * 
     */
    @TableField(value = "cohort_id")
    private Integer groupId;

    /**
     * 
     */
    @TableField(value = "user_id")
    private Integer userId;



    /**
     * 离线或在线
     */
    @TableField(value = "work_status")
    private String workStatus;

    /**
     * 离线或在线
     */
    @TableField(value = "online_status")
    private String onlineStatus;

    /**
     * 
     */
    @TableField(value = "update_time")
    private Date update_time;


    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

}