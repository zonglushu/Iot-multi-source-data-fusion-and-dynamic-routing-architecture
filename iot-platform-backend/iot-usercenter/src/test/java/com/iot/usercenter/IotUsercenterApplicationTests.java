package com.iot.usercenter;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

@SpringBootTest
@Slf4j
class IotUsercenterApplicationTests {

    @Resource
    private RabbitTemplate rabbitTemplate;


    @Test
    void contextLoads() throws InterruptedException {

        String queueName="simple.queue";
        String message="next message idea";
        String exchange="amq.topic";
        String routingKey="simple.test";
//        构建持久化消息
        Message message1 = MessageBuilder.withBody("hello,springAMQP".getBytes(StandardCharsets.UTF_8)).setDeliveryMode(MessageDeliveryMode.PERSISTENT).build();

        // 准备 CorrelationData
        CorrelationData correlationData = new CorrelationData(UUID.randomUUID().toString());
        correlationData.getFuture().addCallback(result -> {
            // 判断
            if(result.isAck()){
                //ACK
                log.debug("消息成功投递到消息交换机！消息ID:{}",correlationData.getId());
            }else {
                //NACK
                log.error("消息投递到交换机失败！消息ID：{}",correlationData.getId());
            }
        },ex -> {
            // 记录日志
            log.error("消息发送失败！",ex);
            // 重发消息
        });
        rabbitTemplate.convertAndSend(exchange,routingKey,message,correlationData);
//        Thread.sleep(5000);

    }

    @Test
    void testTTLMessage(){
        Message message1 = MessageBuilder.withBody("hello,springAMQP".getBytes(StandardCharsets.UTF_8)).setDeliveryMode(MessageDeliveryMode.PERSISTENT).build();

        rabbitTemplate.convertAndSend("ttl.direct","ttl",message1);
    }

}
