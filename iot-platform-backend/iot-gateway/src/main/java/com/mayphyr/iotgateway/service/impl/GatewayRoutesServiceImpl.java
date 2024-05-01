package com.mayphyr.iotgateway.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.mayphyr.iotcommon.model.dto.gateway.GatewayRouteDefinition;
import com.mayphyr.iotcommon.model.entity.GatewayRoutes;
import com.mayphyr.iotgateway.service.GatewayRoutesService;
import com.mayphyr.iotgateway.mapper.GatewayRoutesMapper;
import com.mayphyr.iotgateway.utils.GatewayRouteUtil;
import org.springframework.stereotype.Service;

/**
* @author mayphyr
* @description 针对表【gateway_routes】的数据库操作Service实现
* @createDate 2023-07-26 10:54:21
*/
@Service
public class GatewayRoutesServiceImpl extends ServiceImpl<GatewayRoutesMapper, GatewayRoutes>
    implements GatewayRoutesService{

    @Override
    public boolean addGatewayRoutes(String flag, GatewayRouteDefinition gatewayRouteDefinition) {
        if(flag.equals("success")){
            GatewayRoutes gatewayRoutes = GatewayRouteUtil.GatewayRouteDefinitionConvertGatewayRoutes(gatewayRouteDefinition);
            return save(gatewayRoutes);
        }else {
            return false;
        }
    }

    @Override
    public boolean updateGatewayRoutes(String flag, GatewayRouteDefinition gatewayRouteDefinition) {
        if(flag.equals("success")){
            GatewayRoutes gatewayRoutes = GatewayRouteUtil.GatewayRouteDefinitionConvertGatewayRoutes(gatewayRouteDefinition);
            return updateById(gatewayRoutes);
        }else {
            return false;
        }
    }
}




