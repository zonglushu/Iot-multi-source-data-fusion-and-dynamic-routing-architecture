package com.xiancai.lora.utils.wrong.check;


import com.xiancai.lora.MQTT.bean.MQTTRes;
import com.xiancai.lora.service.ErrorService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

import static com.xiancai.lora.constant.UsuStatus.ERROR_COUNT;

@Component
public class CheckHardWareWrong extends CheckNormalWrong {


    @Resource
    private ErrorService errorService;

    public void checkJsonMessage(String jsonMessage){
        isNULL(jsonMessage,"硬件传来的消息");
    }


    public void checkDetail(Object obj) {
        isNULL(obj,"MQTTRes为null");
        MQTTRes mqttRes = (MQTTRes) obj;
        checkRes(mqttRes.getRes());
    }

    public void checkRes(String res){
        if (res.length()==2) {
            checkError(res.substring(1));
        }else {
            checkFail(res);
        }
    }

    public void checkError(String res){
        int status = Integer.parseInt(res);
        if(status<=0||status>ERROR_COUNT) throwException("硬件传输的错误编号错误");
        throwException(errorService.getById(status).getContent());
    }

    public void checkFail(String res){
        int status = Integer.parseInt(res);
        if(status==-1){
            throwException("硬件处理错误");
        }
    }
}
