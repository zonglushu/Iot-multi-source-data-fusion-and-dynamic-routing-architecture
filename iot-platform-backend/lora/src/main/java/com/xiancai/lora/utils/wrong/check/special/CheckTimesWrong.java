package com.xiancai.lora.utils.wrong.check.special;

import com.xiancai.lora.utils.wrong.check.CheckNormalWrong;

public class CheckTimesWrong extends CheckNormalWrong implements CheckSpecialWrong{
    @Override
    public void checkSpecialWrong(Object obj) {
        String[] times = (String[]) obj;
        if(times.length!=2){
            throwException("时间数组的长度不是2");
        }
        //TODO 防止有人恶意传入不符合规范的时间格式
//        for (String time : times) {
//            if(time)
//        }
    }
}
