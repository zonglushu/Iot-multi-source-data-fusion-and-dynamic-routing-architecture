package com.xiancai.lora.MQTT.util.res;

import com.xiancai.lora.MQTT.bean.MQTTReq;
import com.xiancai.lora.MQTT.util.address.context.AddressHandlerContext;
import com.xiancai.lora.MQTT.util.res.MQTTReqProperty;
import com.xiancai.lora.utils.Base64;
import com.xiancai.lora.utils.RedisIdWorker;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

import java.util.List;
import java.util.Map;



/**
 * 门面模式的外观类，用来装配一个MQTTReq
 */
@Component
public class MQTTReqFacade {
    /**
     * id生成器
     */
    @Resource
    private RedisIdWorker redisIdWorker;

    @Resource
    private MQTTReqProperty mqttReqProperty;

    /**
     * 生成对应的地址
     */
    @Resource
    private AddressHandlerContext addressHandlerContext;




    /**
     * 在这里构建的时候
     * @param data
     * @param symbol
     * @param commandId
     * @param
     * @return
     */
    public MQTTReq combineMQTTReq(Object data,String symbol,Integer commandId){
        //准备对应的addressHandler
        addressHandlerContext.produceAddressHandler(symbol);
        //解析对应的data
        Map<String, Object> dataMap = mqttReqProperty.parseData(data);
        //生成对应的地址
        String address = addressHandlerContext.produceAddress((Integer) dataMap.remove("nodeId"));
        //生成消息id
        if(dataMap.size()==0){
            dataMap.put("X",0);
        }
        String messageId=Long.toString(System.currentTimeMillis());
//        StringBuilder base64MessageId=new StringBuilder();
//        Base64.encode(messageId.length(),new int[2],base64MessageId);
//        String messageId = redisIdWorker.nextId("command")+"";
        String adr="#A";
        if(commandId==24){
            address+="@2";
        }
        List<Integer> time = mqttReqProperty.parseTime();
        MQTTReq req = MQTTReq.builder().mid(messageId.toString())
                .cmd(commandId)
                .adr(address).ct(dataMap).build();
        return req;
    }

    public Map<String,String> parseHardWareAddress(String address){
       return addressHandlerContext.parseHardWareAddress(address);
    }




}
