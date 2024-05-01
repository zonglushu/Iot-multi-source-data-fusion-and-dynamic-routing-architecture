package com.xiancai.lora.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;

import lombok.Builder;
import lombok.Data;

/**
 * 
 * @TableName set
 */
@TableName(value ="cohort")
@Data
@Builder
public class Group implements Serializable {
    /**
     * 
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 
     */
    @TableField(value = "cohort_name")
    private String groupName;

    /**
     * 
     */
    @TableField(value = "device_num")
    private Integer deviceNum;

    /**
     * 
     */
    @TableField(value = "online_num")
    private Integer onlineNum;

    /**
     * 
     */
    @TableField(value = "user_id")
    private Integer user_id;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}