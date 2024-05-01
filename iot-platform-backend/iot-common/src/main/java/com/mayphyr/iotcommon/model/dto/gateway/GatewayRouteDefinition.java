package com.mayphyr.iotcommon.model.dto.gateway;

import lombok.Builder;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 自定义网关路由类,返回给前端的
 */
@Data
@Builder
public class GatewayRouteDefinition {

    //路由的Id
    private String routeId;

    //路由断言集合配置
    private List<GatewayPredicateDefinition> routePredicates = new ArrayList<>();

    //路由过滤器集合配置
    private List<GatewayFilterDefinition> routeFilters = new ArrayList<>();

    //路由规则转发的目标uri
    private String routeUri;

    //路由执行的顺序
    private int routeOrder = 0;

}
