package com.mayphyr.iotreception.mqtt.client;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

import static com.mayphyr.iotreception.constant.RedisConstants.MQTT_MESSAGE;


@Component
@Slf4j
public class MessageCallback implements MqttCallback {

    @Resource
    private StringRedisTemplate stringRedisTemplate;


    /**
     * 丢失了对服务端的连接后触发的回调
     * @param throwable
     */
    @Override
    public void connectionLost(Throwable throwable) {
        //资源的清理 重新连接

        log.info("丢失了对服务端的连接,原因是={}",throwable.toString());
        log.info("为什么丢失",throwable.getMessage());
    }

    /**
     * 应用收到消息后触发的回调
     * @param topic
     * @param mqttMessage
     * @throws Exception
     */
    @Override
    public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
        String message = new String(mqttMessage.getPayload());
        if(JSONUtil.isJsonObj(message)){
            JSONObject jsonObject = JSONUtil.parseObj(message);
            String base64MessageId = jsonObject.get("mid", String.class);
//            byte[] bytes=new byte[64];
//            Integer length = Base64.deCode(base64MessageId, message.length(), new int[2]);
//            String messageId = new String(bytes).substring(0, length);
            stringRedisTemplate.opsForValue().set(MQTT_MESSAGE+base64MessageId,message);
            log.info("已向Redis存储一条消息，消息id为"+base64MessageId);
        }


        log.info("订阅者订阅到了消息,topic={},messageid={},qos={},payload={}",
                topic,
                mqttMessage.getId(),
                mqttMessage.getQos(),
                message
                );

    }

    /**
     * 消息发布者发布完成产生的回调
     * @param iMqttDeliveryToken
     */
    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
        int messageId = iMqttDeliveryToken.getMessageId();

        String[] topics = iMqttDeliveryToken.getTopics();
        //Base64编码


        log.info("消息发布完成，messageid={},topics={}",messageId,topics);
//        publishService.save(Publish.builder()
//                .topics(StrUtil.join(",",topics))
//                .id(messageId).build());
    }
}
