package com.xiancai.lora.utils;

import com.xiancai.lora.model.VO.UserVo;
import com.xiancai.lora.model.entity.User;

public class UserHolder {
    private static final ThreadLocal<UserVo> tl = new ThreadLocal<>();

    public static void saveUser(UserVo user){
        tl.set(user);
    }

    public static UserVo getUser(){
        return tl.get();
    }

    public static void removeUser(){
        tl.remove();
    }
}
