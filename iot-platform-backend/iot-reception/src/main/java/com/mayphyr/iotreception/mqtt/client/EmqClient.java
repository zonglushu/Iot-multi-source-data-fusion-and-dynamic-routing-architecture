package com.mayphyr.iotreception.mqtt.client;

import com.mayphyr.iotcommon.enums.MQTT.QosEnum;
import com.mayphyr.iotreception.mqtt.publish.properties.MqttProperties;

import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Component
public class EmqClient {

    private static final Logger log= LoggerFactory.getLogger(EmqClient.class);

    private IMqttClient mqttClient;

    @Autowired
    private MqttProperties mqttProperties;

    @Autowired
    private MqttCallback mqttCallback;

    @PostConstruct
    public void init(){
        MqttClientPersistence memPersistence= new MemoryPersistence();
        try {
            mqttClient=new MqttClient(mqttProperties.getBrokerUrl(),mqttProperties.getClientId(),memPersistence);
        } catch (MqttException e) {
            log.error("初始化客户端mqttClient对象失败，errorMsg={},brokerUrl={},clientID={}",
                    e.getMessage(),mqttProperties.getBrokerUrl(),mqttProperties.getClientId());
        }
    }

    /**
     * 客户端连接服务端
     * @param username
     * @param password
     */
    public void connect(String username,String password){
        MqttConnectOptions options=new MqttConnectOptions();
        // 自动连接
        options.setAutomaticReconnect(true);
        options.setUserName(username);
        options.setPassword(password.toCharArray());
        options.setCleanSession(false);
        mqttClient.setCallback(mqttCallback);
        try {
            mqttClient.connect(options);
        } catch (MqttException e) {
            log.error("mqtt 客户端连接服务端失败，失败的原因{}",e.getMessage());
        }
    }

    /**
     * 断开连接
     */
    @PreDestroy
    public void disconnect(){
        try {
            mqttClient.disconnect();
        } catch (MqttException e) {
            log.error("断开连接产生异常,异常信息{}",e.getMessage());
        }
    }
    public void reconnect(){
        try {
            mqttClient.reconnect();
        } catch (MqttException e) {
            log.error("重新连接产生异常,异常信息{}",e.getMessage());
        }
    }

    /**
     * 发布消息
     * @param topic
     * @param msg
     * @param qos
     * @param isRetain
     */
    public void publish(String topic, String msg, QosEnum qos, boolean isRetain){
        MqttMessage mqttMessage=new MqttMessage();
        mqttMessage.setPayload(msg.getBytes());
        mqttMessage.setQos(qos.value());
        mqttMessage.setRetained(isRetain);
        try {
            mqttClient.publish(topic,mqttMessage);
        } catch (MqttException e) {
            log.error("发布消息失败，errorMsg={},topic={},msg={},retain={}",e.getMessage(),topic,msg,qos.value(),isRetain);
        }
    }

    /**
     * 订阅消息
     * @param topicFilter
     * @param qos
     */
    public void subscribe(String topicFilter,QosEnum qos){
        try {
            mqttClient.subscribe(topicFilter,qos.value());
        } catch (MqttException e) {
            log.error("订阅主题失败,errorMsg={},topFilter={},qos={}",e.getMessage(),topicFilter,qos);
        }
    }

    /**
     * 取消订阅
     * @param topicFilter
     */
    public void unsubscribe(String topicFilter){
        try {
            mqttClient.unsubscribe(topicFilter);
        } catch (MqttException e) {
            log.error("订阅主题失败,errorMsg={},topFilter={}",e.getMessage(),topicFilter);
        }
    }
}
