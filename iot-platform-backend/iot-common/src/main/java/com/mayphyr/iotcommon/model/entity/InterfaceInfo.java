package com.mayphyr.iotcommon.model.entity;


import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 
 * @TableName interface_info
 */
@TableName(value ="interface_info")
@Data
public class InterfaceInfo implements Serializable {
    /**
     * 自增id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 名称
     */
    private String name;

    /**
     * 接口描述
     */
    private String description;
    /**
     * 接口主机
     */
    private String host;

    /**
     * 请求参数
     */
    private String requestParams;

    /**
     * 请求参数说明
     */
    private String requestParamsRemark;


    /**
     * 响应参数说明
     */
    private String responseParamsRemark;

    /**
     * 创建者id
     */
    private Long founderId;

    /**
     * 接口地址
     */
    private String url;

    /**
     * 请求头
     */
    private String requestHeader;

    /**
     * 响应头
     */
    private String respnseHeader;

    /**
     * 接口状态（0-关闭，1-开启）
     */
    private Integer status;

    /**
     * 请求类型（GET,POST）
     */
    private String method;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;
    /**
     * 更新时间
     */
    private Long projectId;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}