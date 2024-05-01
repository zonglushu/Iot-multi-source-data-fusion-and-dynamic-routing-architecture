package com.iot.usercenter.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class RabbitMQCommonConfig implements ApplicationContextAware {


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        // 获取 RabbitTemplate对象
        RabbitTemplate rabbitTemplate = applicationContext.getBean(RabbitTemplate.class);
        // 消息没有成功到达队列时调用的回调函数
        rabbitTemplate.setReturnsCallback((message)->{
            //记录日志

            log.error("消息发送到队列失败,响应码:{},失败原因：{},交换机:{},路由key:{},消息：{}",
                    message.getReplyCode(),message.getReplyText(),message.getRoutingKey(),message.getMessage());
        });

        // 可以在这里重试，或者告诉管理员发送这个消息
    }
}
