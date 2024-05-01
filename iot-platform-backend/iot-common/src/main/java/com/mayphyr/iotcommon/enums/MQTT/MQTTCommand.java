package com.mayphyr.iotcommon.enums.MQTT;
import lombok.AllArgsConstructor;
import lombok.Getter;
/**
 * 准确的说是只有节点这一级才可以执行命令
 * 每个命令都要带节点,网关只能转发
 * 传感器受节点控制
 * 现在先把命令下发这一层跑通
 * 具体地发送及转发流程是跟硬件配合的
 * 所以我们要发命令，后面应该还要加上节点id,如果是传感器还要加上传感器id
 */

/**
 * 生成一个参数为所有实例变量的构造方法
 *
 */
@Getter
@AllArgsConstructor
public enum MQTTCommand {
    /**
     * 握手
     */
    HAND_SHAKE("handshake",1),
    /**
     * 节点-初始化
     */
    NODE_INIT("node_init",2),
    /**
     * 节点-重启
     */
    NODE_RESTART("node_restart",3),
    /**
     * 模块-开启
     */
    MODULE_ON("module_poweron",13);
//    /**
//     * 节点-开启
//     */
//    NODE_ON("node_on"),
//    /**
//     * 节点-关闭
//     */
//    NODE_OFF("node_off"),


    private final String command;

    private final Integer code;
}
