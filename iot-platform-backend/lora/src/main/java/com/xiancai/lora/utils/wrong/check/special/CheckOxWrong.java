package com.xiancai.lora.utils.wrong.check.special;

import com.xiancai.lora.exception.BusinessException;
import com.xiancai.lora.utils.wrong.check.CheckNormalWrong;

import static com.xiancai.lora.enums.StatusCode.PARAMS_ERR;

public class CheckOxWrong extends CheckNormalWrong implements CheckSpecialWrong{
    @Override
    public void checkSpecialWrong(Object obj) {
        Integer ox = (Integer) obj;
        if(ox<0||ox>16){
            throw new BusinessException(PARAMS_ERR.getMessage(),PARAMS_ERR.getCode(),"i/o 序号不符合规范");
        }
    }
}
