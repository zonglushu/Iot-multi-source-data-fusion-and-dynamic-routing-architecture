package com.mayphyr.iotgateway.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.mayphyr.iotcommon.model.dto.gateway.GatewayRouteDefinition;
import com.mayphyr.iotcommon.model.entity.GatewayRoutes;

/**
* @author mayphyr
* @description 针对表【gateway_routes】的数据库操作Service
* @createDate 2023-07-26 10:54:21
*/
public interface GatewayRoutesService extends IService<GatewayRoutes> {


     boolean addGatewayRoutes(String flag, GatewayRouteDefinition gatewayRouteDefinition);

     boolean updateGatewayRoutes(String flag, GatewayRouteDefinition gatewayRouteDefinition);

}
