package com.xiancai.lora.MQTT.util;


import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.xiancai.lora.MQTT.bean.MQTTRes;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import java.util.HashMap;
import java.util.Map;

import static com.xiancai.lora.constant.RedisConstants.MQTT_MESSAGE;

@Service
public class MessageProcessor {

    //Redis里面还是存 json串
    @Resource
    private StringRedisTemplate stringRedisTemplate;


    public String getMessage(String messageId){
        int count=1;
        while (count<=5){
            String jsonMessage = stringRedisTemplate.opsForValue().get(MQTT_MESSAGE + messageId);
            if(StrUtil.isNotBlank(jsonMessage)) return jsonMessage ;
            try {
                //睡0.2秒再去查，不要查太频繁
                Thread.sleep(200);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            count++;
        }
        //5次还没得到消息，就直接返回null
        return null;
    }

    public MQTTRes parseMessage (String json){

        return null;
    }



}
