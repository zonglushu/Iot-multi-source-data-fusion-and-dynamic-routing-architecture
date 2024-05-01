package com.xiancai.lora.MQTT.service.context;

import cn.hutool.core.util.StrUtil;
import com.xiancai.lora.utils.StringUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static com.xiancai.lora.constant.UsuStatus.REFLECT_PREFIX;
import static com.xiancai.lora.constant.UsuStatus.REFLECT_SUFFIX;

/**
 * 利用反射去调用方法
 */
public class CallMethod {
    /**
     * 通过字符串串调用方法
     * @param classAndMethod 类名-方法名，通过此字符串调用类中的方法
     * @param paramTypes 方法类型列表(因为方法可能重载)
     * @param params 需要调用的方法的参数列表
     * @return
     */
    public static Object call(String classAndMethod,Class[] paramTypes,Object[] params,Object o){
        String[] args=classAndMethod.split("_",2);
        //要调用的类名
        String className=args[0];
        className=REFLECT_PREFIX+StringUtils.getMethodName(className)+REFLECT_SUFFIX;
        //要调用的方法名
        String method="";
        method=StrUtil.toCamelCase(args[1]);
        try {
            //加载类，参数是完整类名 //第一个参数是方法名，后面的参数指示方法的参数类型和个数
            Method newMethod=Class.forName(className).getMethod(method,paramTypes);
            //accessiable设为true表示忽略java语言访问检查（可访问private方法）
            //method.setAccessible(true);
            //第一个参数类实例(必须有对象才能调用非静态方法,如果是静态方法此参数可为null)
            //第二个是要传给方法的参数
            Object result=newMethod.invoke(o,params);
            return result;
        } catch (InvocationTargetException e) {
            System.out.println("此处接收被调用方法内部未被捕获的异常");
            Throwable t = e.getTargetException();// 获取目标异常
            t.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        return null;
    }



}
