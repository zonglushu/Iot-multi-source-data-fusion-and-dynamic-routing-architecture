package com.mayphyr.iotcommon.model.dto.gateway;

import lombok.Data;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 自定义网关过滤器
 */
@Data
public class GatewayFilterDefinition {
    //Filter Name
    private String name;

    //对应的路由规则
    private Map<String, String> args = new LinkedHashMap<>();

}
