package com.xiancai.lora.MQTT.util.res;

import cn.hutool.core.bean.BeanUtil;
import com.xiancai.lora.exception.BusinessException;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;

import static com.xiancai.lora.enums.StatusCode.PARAMS_ERR;


@Component
public class MQTTReqProperty {
    /**
     * 解析时间
     * @return
     */
    public List<Integer> parseTime(){
        LocalDateTime now = LocalDateTime.now();
        String date = now.format(DateTimeFormatter.ofPattern("yy,MM,dd,HH,mm,ss"));
        ArrayList<Integer> list = new ArrayList<>();
        for (String s : date.split(",")) {
            list.add(Integer.parseInt(s));
        }
        return list;
    }
    /**
     * 解析地址
     */


    /**
     * 解析 data,无论是什么 DTO 对象，我们都只要把他转成字符串就行了
     */
    public  Map<String,Object>  parseData(Object data){
        Map<String, Object> stringObjectMap = BeanUtil.beanToMap(data);
        return stringObjectMap;
    }
}
