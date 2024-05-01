package com.mayphyr.iotdecider.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class listener {

    @RabbitListener(queues = "simple.queue")
    public void listenSimpleQueue(String msg){
        System.out.println("消费者接收到 simple.queue的消息：【"+msg+"】");
        System.out.println(1/0);
        System.out.println("消费者处理消息成功");
    }


    @RabbitListener(bindings = @QueueBinding(value = @Queue(name = "dl.queue",durable = "true"),
            exchange = @Exchange(name = "dl.direct"),key = "dl"))
    public void listenDlQueue(String msg){
        log.info("消费者接收到了dl.queue的 延迟消息");
    }
}
