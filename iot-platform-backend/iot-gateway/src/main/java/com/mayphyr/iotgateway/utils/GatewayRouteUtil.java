package com.mayphyr.iotgateway.utils;


import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.alibaba.excel.util.StringUtils;
import com.mayphyr.iotcommon.enums.StatusCode;
import com.mayphyr.iotcommon.exception.ThrowUtils;
import com.mayphyr.iotcommon.model.dto.gateway.GatewayFilterDefinition;
import com.mayphyr.iotcommon.model.dto.gateway.GatewayPredicateDefinition;
import com.mayphyr.iotcommon.model.dto.gateway.GatewayRouteDefinition;
import com.mayphyr.iotcommon.model.entity.GatewayRoutes;
import nonapi.io.github.classgraph.json.JSONUtils;
import org.ehcache.core.util.CollectionUtil;
import org.springframework.cloud.gateway.filter.FilterDefinition;
import org.springframework.cloud.gateway.handler.predicate.PredicateDefinition;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.util.UriComponentsBuilder;


import java.net.URI;
import java.util.*;


public class GatewayRouteUtil {

    /**
     * 数据库路由信息表对应的对象转换为前端路由对象
     * GatewayRoutes对象中，routePredicates和routeFilter是需要进行json转换
     * @param gatewayRoutes
     * @return
     */
    public static GatewayRouteDefinition GatewayRoutesConvertGatewayRouteDefinition(GatewayRoutes gatewayRoutes){
        // 路由id
        String routeId = gatewayRoutes.getRouteId();
        // 路由uri
        String routeUri = gatewayRoutes.getRouteUri();
        // 路由过滤器
        String routeFilters = gatewayRoutes.getRouteFilters();
        // 路由执行顺序
        int routeOrder = gatewayRoutes.getRouteOrder();
        // 路由断言（这里有许多断言，比如说Before断言，After断言，Path断言）
        String routePredicates = gatewayRoutes.getRoutePredicates();
        ThrowUtils.throwIf(StringUtils.isBlank(routeId), StatusCode.ERROR,"路由Id不存在，检查路由数据库");
        ThrowUtils.throwIf(StringUtils.isBlank(routeUri), StatusCode.ERROR,"路由Id不存在，检查路由数据库");
        ThrowUtils.throwIf(StringUtils.isBlank(routePredicates), StatusCode.ERROR,"路由Id不存在，检查路由数据库");
        // 获取路由断言的Json串
        JSONArray JsonRoutePredicates = JSONUtil.parseArray(routePredicates);
        // 获取路由过滤器的Json串
        JSONArray JsonRouteFilters = JSONUtil.parseArray(routeFilters);
        List<GatewayPredicateDefinition> routePredicatesList = JSONUtil.toList(JsonRoutePredicates, GatewayPredicateDefinition.class);
        List<GatewayFilterDefinition> routeFiltersList = JSONUtil.toList(JsonRouteFilters, GatewayFilterDefinition.class);
        GatewayRouteDefinition gatewayRouteDefinition = GatewayRouteDefinition.builder().routeOrder(routeOrder).routeFilters(routeFiltersList).routeUri(routeUri)
                .routePredicates(routePredicatesList).routeId(routeId).build();
        return gatewayRouteDefinition;
    }


    /**
     * 数据库路由信息表对象转换为Gateway系统路由对象
     * @param gatewayRoutes
     * @return
     */
    public static RouteDefinition GateRoutesConvertRouteDefinition(GatewayRoutes gatewayRoutes){
        GatewayRouteDefinition gatewayRouteDefinition = GatewayRoutesConvertGatewayRouteDefinition(gatewayRoutes);
        return GatewayRouteDefinitionConvertRouteDefinition(gatewayRouteDefinition);

    }

    /**
     * 前端路由对象转换为数据库路由对象
     * @param gatewayRouteDefinition
     * @return
     */
    public static GatewayRoutes GatewayRouteDefinitionConvertGatewayRoutes(GatewayRouteDefinition gatewayRouteDefinition){
        GatewayRoutes gatewayRoutes = new GatewayRoutes();
        //
        List<GatewayFilterDefinition> routeFilters = gatewayRouteDefinition.getRouteFilters();
        List<GatewayPredicateDefinition> routePredicates = gatewayRouteDefinition.getRoutePredicates();
        int routeOrder = gatewayRouteDefinition.getRouteOrder();
        String routeId = gatewayRouteDefinition.getRouteId();
        String routeUri = gatewayRouteDefinition.getRouteUri();
        // 只用判断Id和uri就可以了，过滤器和断言可设置也可不设置，其实还有保证路由Id唯一，
        // 应该就还要去查重，这个没有办法，因为是用户自己设置的，所以可以重复
        // 或者我们也可以给用户分配，不让用户自己分配。
        ThrowUtils.throwIf(StringUtils.isNotBlank(routeId),StatusCode.ERROR,"网关路由Id错误");
        ThrowUtils.throwIf(StringUtils.isNotBlank(routeUri),StatusCode.ERROR,"网关路由uri错误");
        JSONArray jsonRouteFilters = JSONUtil.parseArray(routeFilters);
        JSONArray jsonRoutePredicates = JSONUtil.parseArray(routePredicates);
        String routerFiltersJsonString = jsonRouteFilters.toString();
        String routerPredicatesJsonString = jsonRoutePredicates.toString();
        //
        gatewayRoutes.setRouteId(routeId);
        gatewayRoutes.setRouteFilters(routerFiltersJsonString);
        gatewayRoutes.setRoutePredicates(routerPredicatesJsonString);
        gatewayRoutes.setRouteUri(routeUri);
        gatewayRoutes.setRouteOrder(routeOrder);
        return gatewayRoutes;

    }


    /**
     * 前端路由对象转换为Gateway路由对象
     * @param gatewayRouteDefinition
     * @return
     */
    public static RouteDefinition GatewayRouteDefinitionConvertRouteDefinition(GatewayRouteDefinition gatewayRouteDefinition) {
        RouteDefinition definition = new RouteDefinition();
        definition.setId(gatewayRouteDefinition.getRouteId());
        definition.setOrder(gatewayRouteDefinition.getRouteOrder());

        List<PredicateDefinition> pdList = new ArrayList<>();
        //获取所有的断言规则
        List<GatewayPredicateDefinition> gatewayPredicateDefinitionList = gatewayRouteDefinition.getRoutePredicates();
        for (GatewayPredicateDefinition gpDefinition: gatewayPredicateDefinitionList) {
            PredicateDefinition predicate = new PredicateDefinition();
            predicate.setArgs(gpDefinition.getArgs());
            predicate.setName(gpDefinition.getName());
            pdList.add(predicate);
        }
        definition.setPredicates(pdList);

        //设置过滤器
        List<FilterDefinition> filters = new ArrayList();
        List<GatewayFilterDefinition> gatewayFilters = gatewayRouteDefinition.getRouteFilters();
        for(GatewayFilterDefinition filterDefinition : gatewayFilters){
            FilterDefinition filter = new FilterDefinition();
            filter.setName(filterDefinition.getName());
            filter.setArgs(filterDefinition.getArgs());
            filters.add(filter);
        }
        definition.setFilters(filters);

        URI uri = null;
        // 判断Uri是哪一种类型
        if(gatewayRouteDefinition.getRouteUri().startsWith("http")){
            uri = UriComponentsBuilder.fromHttpUrl(gatewayRouteDefinition.getRouteUri()).build().toUri();
        }else{
            // uri为 lb://consumer-service 时使用下面的方法
            uri = URI.create(gatewayRouteDefinition.getRouteUri());
        }
        definition.setUri(uri);
        return definition;
    }

}
