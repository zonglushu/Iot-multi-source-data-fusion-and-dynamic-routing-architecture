package com.mayphyr.iotes.model.config;


import com.mayphyr.iotcommon.constant.MQConstants;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 配置消息同步的交换机和队列
 */
@Configuration
public class MQConfig {


    @Bean
    public TopicExchange topicExchange(){
        return new TopicExchange(MQConstants.DATA_EXCHANGE,true,false);
    }

    @Bean
    public Queue insertQueue(){
        return new Queue(MQConstants.DATA_INSERT_QUEUE,true);

    }
    @Bean
    public Queue deleteQueue(){
        return new Queue(MQConstants.DATA_DELETE_QUEUE,true);
    }

    @Bean
    public Binding insertQueueBinding(){
        return BindingBuilder.bind(insertQueue()).to(topicExchange()).with(MQConstants.DATA_INSERT_KEY);
    }

    @Bean
    public Binding deleteQueueBinding(){
        return BindingBuilder.bind(deleteQueue()).to(topicExchange()).with(MQConstants.DATA_DELETE_KEY);
    }
}
