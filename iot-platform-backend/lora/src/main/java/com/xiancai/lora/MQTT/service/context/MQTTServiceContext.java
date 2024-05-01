package com.xiancai.lora.MQTT.service.context;


import com.xiancai.lora.MQTT.service.proxy.ProxyFactory;
import com.xiancai.lora.MQTT.service.proxy.ProxyModuleService;
import com.xiancai.lora.MQTT.service.proxy.ProxyNodeService;

import com.xiancai.lora.utils.Result;

import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;


@Component
public class MQTTServiceContext {

    @Resource
    private ProxyFactory proxyFactory;
    @Resource
    private ProxyNodeService proxyNodeService;
    @Resource
    private ProxyModuleService proxyModuleService;

    //利用反射去操作数据库
    //先是通过反射去调用proxyFactory的对应方法
    public Result executeService(String classPath, Map<String,Object> data){
        //先进来判断是不是不用去处理库，如果不用就直接返回。
        boolean isNoRes = isNoRes(data);
        if (isNoRes){
            return Result.success(true);
        }
        System.out.println("要操作数据库的数据");
        for (String s : data.keySet()) {
            System.out.print(s+":"+" "+data.get(s));
        }

        Class[] paramTypes={Map.class};
        Object[] params={data};
        String[] split = classPath.split("_");
        if(split[0].equals("node")){
            proxyFactory.produceProxy(proxyNodeService);
            ProxyNodeService proxyInstance = (ProxyNodeService) proxyFactory.getProxyInstance();
            return  (Result) CallMethod.call(classPath, paramTypes, params, proxyInstance);
        }else {
            proxyFactory.produceProxy(proxyModuleService);
            ProxyModuleService proxyInstance = (ProxyModuleService) proxyFactory.getProxyInstance();
            return  (Result) CallMethod.call(classPath, paramTypes, params, proxyInstance);
        }
    }
    private boolean isNoRes(Map<String, Object> data){
        if(data.containsKey("X")&&data.get("X").equals(0)){
            return true;
        }
        return false;
    }
}
