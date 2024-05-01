package com.xiancai.lora.MQTT.publish;

import cn.hutool.json.JSONUtil;
import com.xiancai.lora.MQTT.bean.MQTTReq;
import com.xiancai.lora.MQTT.client.EmqClient;
import com.xiancai.lora.MQTT.publish.properties.MqttProperties;
import com.xiancai.lora.enums.QosEnum;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.xiancai.lora.enums.MQTT.MQTTCommand.MODULE_ON;
import static com.xiancai.lora.enums.MQTT.MQTTReqType.MQTT_REQ_GET;


@Service
public class PublishMessage {

    @Resource
    private EmqClient emqClient;
    @Resource
    private MqttProperties mqttProperties;



    public void publishMessage(Map map,String messageId){

        // 命令的地址，表示命令到哪一个节点
        String adr="#123#456#789";
        List<Integer> time = parseTime();
        MQTTReq req = MQTTReq.builder().mid(messageId)
                .adr(adr).cmd(MODULE_ON.getCode())
                .ct(map).build();
        emqClient.publish(mqttProperties.getWebTopic(), JSONUtil.toJsonPrettyStr(req), QosEnum.QOS2, false);
    }

    private List<Integer> parseTime(){
        LocalDateTime now = LocalDateTime.now();
        String date = now.format(DateTimeFormatter.ofPattern("yy,MM,dd,HH,mm,ss"));
        ArrayList<Integer> list = new ArrayList<>();
        for (String s : date.split(",")) {
            list.add(Integer.parseInt(s));
        }
        return list;
    }
}
