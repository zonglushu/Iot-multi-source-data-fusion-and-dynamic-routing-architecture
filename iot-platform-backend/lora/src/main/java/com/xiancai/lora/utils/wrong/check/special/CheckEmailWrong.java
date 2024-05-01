package com.xiancai.lora.utils.wrong.check.special;

import com.xiancai.lora.exception.BusinessException;
import com.xiancai.lora.utils.RegexUtils;
import com.xiancai.lora.utils.wrong.check.CheckNormalWrong;

import static com.xiancai.lora.enums.StatusCode.PARAMS_ERR;

public class CheckEmailWrong extends CheckNormalWrong implements CheckSpecialWrong {
    @Override
    public void checkSpecialWrong(Object obj) {
        String email = (String) obj;
        if (RegexUtils.isEmailInvalid(email)) {
            throwException("邮箱格式不对");
        }


    }
}
