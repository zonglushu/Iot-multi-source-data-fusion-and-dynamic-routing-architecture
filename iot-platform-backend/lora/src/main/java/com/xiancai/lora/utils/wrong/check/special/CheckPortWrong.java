package com.xiancai.lora.utils.wrong.check.special;

import com.xiancai.lora.utils.wrong.check.CheckNormalWrong;

public class CheckPortWrong extends CheckNormalWrong implements CheckSpecialWrong {
    @Override
    public void checkSpecialWrong(Object obj) {
        Integer port = (Integer) obj;
        if(port<0||port>3){
            throwException("port非法");
        }
    }
}
