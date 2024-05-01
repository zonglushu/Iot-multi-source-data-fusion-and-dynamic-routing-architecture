package com.iot.usercenter.utils;


import com.mayphyr.iotcommon.model.vo.IotUserVo;

public class UserHolder {
    private static final ThreadLocal<IotUserVo> tl = new ThreadLocal<>();

    public static void saveUser(IotUserVo user){
        tl.set(user);
    }

    public static IotUserVo getUser(){
        return tl.get();
    }

    public static void removeUser(){
        tl.remove();
    }
}
