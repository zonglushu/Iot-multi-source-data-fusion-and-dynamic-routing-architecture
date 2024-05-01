package com.xiancai.lora.MQTT.service.proxy;

import cn.hutool.core.bean.BeanUtil;
import com.xiancai.lora.service.NodeService;
import com.xiancai.lora.utils.Result;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.cglib.proxy.*;
import org.springframework.stereotype.Component;


import java.lang.reflect.Method;


/**
 * Cglib代理,通过生成一个被代理对象的子类实现代理效果
 */
@Component
public class ProxyFactory implements MethodInterceptor {
    //维护一个目标对象
    private Object target;



    //传入一个被代理的对象
    public void produceProxy(Object target){
        this.target=target;
    }
    //返回一个代理对象，是target对象的代理对象
    public Object getProxyInstance(){
        //1.创建一个工具类
        Enhancer enhancer = new Enhancer();
        //2.设置父类
        enhancer.setSuperclass(target.getClass());
        //3.设置回调函数
        enhancer.setCallback(this);
        //4.创建子类对象，即代理对象
        return enhancer.create();
    }

    public void produceProxyByName(String classPath){

    }

    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        return method.invoke(target,objects);
    }
}
