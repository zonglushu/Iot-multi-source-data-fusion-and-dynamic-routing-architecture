package com.mayphyr.iotes.model.mq;

import com.mayphyr.iotcommon.constant.MQConstants;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.List;


/**
 * 监听传感器数据库的变化
 */
@Component
public class DataListener {



    @RabbitListener(queues = MQConstants.DATA_INSERT_QUEUE)
    public void listenDataInsertOrUpdate(List<Long> ids){

    }

    /**
     * 监听删除,是要删除过期的时间
     * @param ids
     */
    @RabbitListener(queues = MQConstants.DATA_DELETE_QUEUE)
    public void listenDataDelete(List<Long> ids){

    }
}
