package com.mayphyr.iotgateway;


import com.mayphyr.iotfeign.config.DefaultFeignConfig;
import com.mayphyr.iotfeign.rpc.UserClient;
import com.mayphyr.iotfeign.rpc.interfaces.CheckAccessClient;
import com.mayphyr.iotgateway.config.DynamicRouteService;
import com.mayphyr.iotgateway.service.GatewayRoutesService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;




@SpringBootApplication
// 开启feignClient的自动装配
@EnableFeignClients(clients = {UserClient.class, CheckAccessClient.class},defaultConfiguration = DefaultFeignConfig.class)
public class IotGatewayApplication {

    @Resource
    private GatewayRoutesService gatewayRoutesService;


    @Resource
    private DynamicRouteService dynamicRouteService;

    public static void main(String[] args) {
        SpringApplication.run(IotGatewayApplication.class, args);
    }

    /**
     * 初始方法，只执行一次，在项目启动时，将数据库里的路由数据加载到项目中。
     * @throws InterruptedException
     */
    @PostConstruct
    public void  init() throws InterruptedException {
//        Route
////        获取数据库里的全部路由信息
//        List<GatewayRoutes> routes = gatewayRoutesService.query().list();
//        // 将数据库里的路由信息转化为可以添加路由库里的路由对象
//        List<RouteDefinition> routeDefinitions = routes.stream().map(route ->
//                gatewayRouteUtil.convertRouteDefinition(route)).collect(Collectors.toList());
//        // 将路由添加到容器中，并通知上下文
//        dynamicRouteService.addRoute(routeDefinitions);


    }

}
