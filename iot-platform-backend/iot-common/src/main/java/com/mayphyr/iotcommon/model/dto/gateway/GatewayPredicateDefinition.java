package com.mayphyr.iotcommon.model.dto.gateway;

import lombok.Data;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 自定义网关断言器
 */
@Data
public class GatewayPredicateDefinition {

    //断言对应的Name
    private String name;

    //配置的断言规则
    private Map<String, String> args = new LinkedHashMap<>();
}

