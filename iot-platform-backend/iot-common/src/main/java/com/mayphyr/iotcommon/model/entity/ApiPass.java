package com.mayphyr.iotcommon.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 
 * @TableName api_pass
 */
@TableName(value ="api_pass")
@Data

public class ApiPass implements Serializable {
    /**
     * 系统用户的ID值
     */
    @TableId
    private Long userId;

    /**
     * 用户的accesskey
     */
    private String accessKey;

    /**
     * 用户的secretkey
     */
    private String secretKey;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}