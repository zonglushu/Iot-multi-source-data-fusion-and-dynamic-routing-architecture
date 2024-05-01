package com.xiancai.lora.utils.wrong.check.special;

import com.xiancai.lora.exception.BusinessException;
import com.xiancai.lora.utils.wrong.check.CheckNormalWrong;

import static com.xiancai.lora.enums.StatusCode.PARAMS_ERR;

public class CheckPasswordWrong extends CheckNormalWrong implements CheckSpecialWrong {
    @Override
    public void checkSpecialWrong(Object obj) {
        String password = (String) obj;
        if(password.length()>20||password.length()<6){
            throwException("密码不符合规范,密码为6位-20位");
        }
    }
}
