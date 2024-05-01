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
 * @TableName cost
 */
@TableName(value ="cost")
@Data
public class Cost implements Serializable {

    /**
     * id主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 该条数据操作的用户
     */
    @TableField(value = "user_id")
    private Integer userId;

    /**
     * 消费或者充值
     */
    @TableField(value = "mode")
    private String mode;

    /**
     * 使用用途
     */
    @TableField(value = "useage")
    private String usage;

    /**
     * 此次操作的金额
     */
    @TableField(value = "amount")
    private Integer amount;

    /**
     * 该用户的余额
     */
    @TableField(value = "balance")
    private Integer balance;

    /**
     * 操作时间
     */
    @TableField(value = "cost_time")
    private Date costTime;
    /**
     * 充值总数
     */
    @TableField(exist = false)
    private int recharge;
    /**
     * 消费总数
     */
    @TableField(exist = false)
    private int spend;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}