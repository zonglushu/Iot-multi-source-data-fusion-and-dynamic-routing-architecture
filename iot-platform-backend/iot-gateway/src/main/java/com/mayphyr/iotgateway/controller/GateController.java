package com.mayphyr.iotgateway.controller;


import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.mayphyr.iotcommon.common.Result;
import com.mayphyr.iotcommon.model.dto.gateway.GatewayRouteDefinition;
import com.mayphyr.iotcommon.model.entity.GatewayRoutes;
import com.mayphyr.iotgateway.config.DynamicRouteService;
import com.mayphyr.iotgateway.service.GatewayRoutesService;
import com.mayphyr.iotgateway.utils.GatewayRouteUtil;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("sssss")
public class GateController {

    @Resource
    private DynamicRouteService dynamicRouteService;


    @Resource
    private GatewayRoutesService gatewayRoutesService;



    /**
     * 增加路由
     * @param gatewayRouteDefinition 路由模型
     *  报文:{"filters":[{"name":"StripPrefix","args":{"_genkey_0":"2"}}],"id":"ijep-service-sys-hyk11","uri":"lb://ijep-service-sys","order":2,"predicates":[{"name":"Path","args":{"_genkey_0":"/api/hyk11/**"}}]}
     * @return
     */
    @PostMapping("/add")
    public Result add(@RequestBody GatewayRouteDefinition gatewayRouteDefinition) {
        boolean isAdd= false;
        try
        {
            RouteDefinition routeDefinition = GatewayRouteUtil.
                    GatewayRouteDefinitionConvertRouteDefinition(gatewayRouteDefinition);
            String flag = this.dynamicRouteService.add(routeDefinition);
            isAdd = gatewayRoutesService.addGatewayRoutes(flag, gatewayRouteDefinition);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Result.success(isAdd);
    }


    /**
     * 更新路由
     * @param gatewayRouteDefinition 路由模型
     *  报文:{"filters":[{"name":"StripPrefix","args":{"_genkey_0":"2"}}],"id":"ijep-service-sys-hyk11","uri":"lb://ijep-service-sys","order":2,"predicates":[{"name":"Path","args":{"_genkey_0":"/api/hyk11/**"}}]}
     * @return
     */
    @PostMapping("/update")
    public Result update(@RequestBody GatewayRouteDefinition gatewayRouteDefinition) {
        RouteDefinition definition = GatewayRouteUtil.GatewayRouteDefinitionConvertRouteDefinition(gatewayRouteDefinition);
        String flag = this.dynamicRouteService.update(definition);
        boolean isUpdate = gatewayRoutesService.updateGatewayRoutes(flag, gatewayRouteDefinition);
        return Result.success(isUpdate);
    }



    /**
     * 删除路由
     * @param id 路由Id
     * @return
     */
    @DeleteMapping("/routes/{id}")
    public Result delete(@PathVariable String id) {
        Mono<ResponseEntity<Object>> responseEntityMono = this.dynamicRouteService.delete(id);
        return Result.success(responseEntityMono);
    }



}
