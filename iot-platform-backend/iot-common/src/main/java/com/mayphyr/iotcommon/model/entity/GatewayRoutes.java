package com.mayphyr.iotcommon.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 
 * @TableName gateway_routes
 */
@TableName(value ="gateway_routes")
@Data
public class GatewayRoutes implements Serializable {
    /**
     * 路由id
     */
    @TableId
    private String routeId;

    /**
     * 路由uri
     */
    private String routeUri;

    /**
     * 路由断言（用json串）
     */
    private String routePredicates;

    /**
     * 路由过滤器（用json串）
     */
    private String routeFilters;

    /**
     * 路由顺序
     */
    private int routeOrder;

    /**
     * 逻辑删除
     */
    private String isDelete;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}