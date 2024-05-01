package com.xiancai.lora.utils.wrong.check.special;

import com.xiancai.lora.exception.BusinessException;
import com.xiancai.lora.utils.StringUtils;
import org.springframework.stereotype.Component;

import static com.xiancai.lora.constant.UsuStatus.CHECK_WRONG_REFLECT_PREFIX;

@Component
public class CheckSpecialContext {

    public void checkWrong(String classPath,Object value){
        try {
            if (classPath.contains("time")){
                classPath="times";
            }
            CheckSpecialWrong checkSpecialWrong = (CheckSpecialWrong) Class
                    .forName(CHECK_WRONG_REFLECT_PREFIX + StringUtils.getMethodName(classPath)+"Wrong").newInstance();
            checkSpecialWrong.checkSpecialWrong(value);
        } catch (BusinessException e){
           throw e;
        }catch (Exception e){
            return;
        }
    }
}
