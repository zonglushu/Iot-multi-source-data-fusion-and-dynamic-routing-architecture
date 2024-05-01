package com.xiancai.lora.utils.wrong.check.special;

import com.xiancai.lora.exception.BusinessException;
import com.xiancai.lora.utils.RegexUtils;
import com.xiancai.lora.utils.wrong.check.CheckNormalWrong;

import static com.xiancai.lora.enums.StatusCode.PARAMS_ERR;

public class CheckPhoneWrong extends CheckNormalWrong implements CheckSpecialWrong {
    @Override
    public void checkSpecialWrong(Object obj) {
        String phone = (String) obj;
        if (RegexUtils.isPhoneInvalid(phone)) {
            throwException("电话号不符合规范");
        }
    }
}
