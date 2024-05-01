package com.mayphyr.iotcommon.constant;

/**
 * 用于MySQL与ES进行消息同步的消息队列配置
 */

public class MQConstants {
    /**
     * 传感器消息同步的交换机
     */
    public final static String DATA_EXCHANGE="data.topic";
    /**
     * 用于新增和修改的队列
     */
    public final static String DATA_INSERT_QUEUE="data.insert.queue";
    /**
     * 用于删除的队列
     */
    public final static String DATA_DELETE_QUEUE="data.delete.queue";
    /**
     * 用于新增和修改的RoutingKey
     */
    public final static String DATA_INSERT_KEY="data.insert";
    /**
     * 用于删除的RoutingKey
     */
    public final static String DATA_DELETE_KEY="data.delete";

}
