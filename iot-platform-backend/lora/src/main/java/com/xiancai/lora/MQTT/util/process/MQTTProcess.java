package com.xiancai.lora.MQTT.util.process;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.xiancai.lora.MQTT.bean.MQTTReq;
import com.xiancai.lora.MQTT.bean.MQTTRes;
import com.xiancai.lora.MQTT.client.EmqClient;
import com.xiancai.lora.MQTT.publish.properties.MqttProperties;
import com.xiancai.lora.MQTT.service.context.MQTTServiceContext;

import com.xiancai.lora.MQTT.util.res.MQTTReqFacade;
import com.xiancai.lora.enums.QosEnum;
import com.xiancai.lora.exception.BusinessException;
import com.xiancai.lora.service.CommandService;
import com.xiancai.lora.utils.Base64;
import com.xiancai.lora.utils.Result;
import com.xiancai.lora.utils.UserHolder;
import com.xiancai.lora.utils.wrong.check.CheckHardWareWrong;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

import java.util.Map;

import static com.xiancai.lora.constant.RedisConstants.MQTT_MESSAGE;

import static com.xiancai.lora.constant.RedisConstants.MQTT_MESSAGE_ID;
import static com.xiancai.lora.enums.StatusCode.PARAMS_ERR;

/**
 * MQTT整体流程
 */
@Component
@Slf4j
public class MQTTProcess {

    @Resource
    private MQTTReqFacade mqttReqFacade;
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Resource
    private CommandService commandService;

    @Resource
    private CheckHardWareWrong checkHardWareWrong;

    @Resource
    private EmqClient emqClient;
    @Resource
    private MqttProperties mqttProperties;

    @Resource
    private MQTTServiceContext mqttServiceContext;

    /**
     * 准备MQTTReq
     *
     * @return
     */
    protected MQTTReq prepareMQTTReq(Object data, String symbol, Integer commandId) {
        return mqttReqFacade.combineMQTTReq(data, symbol, commandId);
    }

    /**
     * 发布消息
     */
    public String publishMessage(Object data, String symbol, Integer commandId) {
        //封装请求对象
        MQTTReq mqttReq = prepareMQTTReq(data, symbol, commandId);
        //发布消息
        String jsonMes = JSONUtil.toJsonPrettyStr(mqttReq).replace(" ", "").replace("\n", "");
        log.info("给硬件发布的消息是"+jsonMes);
        emqClient.publish(mqttProperties.getWebTopic(),jsonMes, QosEnum.QOS0, false);
        //解密id
//        byte[] de_byte=new byte[64];
//        Integer length = Base64.deCode(mqttReq.getMid(), mqttReq.getMid().length(), new int[2]);
//        String messageId=new String(de_byte).substring(0,length);
        return mqttReq.getMid();
    }

    /**
     * 接收消息
     */
    public String receiveMessage(String messageId) {
        int count = 1;
        while (count <= 5) {
            String jsonMessage = stringRedisTemplate.opsForValue().get(MQTT_MESSAGE + messageId);
            if (StrUtil.isNotBlank(jsonMessage)) {
                log.info("收到硬件的消息,消息为"+jsonMessage);
                return jsonMessage;
            }
            try {
                //睡0.2秒再去查，不要查太频繁
                Thread.sleep(200);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            count++;
        }
        //5次还没得到消息，就直接返回null
        return null;
    }

    /**
     * 解析消息,判断错误，还是根据硬件的地址去找，因为后面会有数据自动上报，我们是不发消息的
     */
    public Map<String, Object> parseMessage(String jsonMessage) {
        log.info("开始处理硬件的消息");
        //先检查传过来的信息是不是null
        checkHardWareWrong.checkJsonMessage(jsonMessage);
        //然后用json包将硬件发送的json信息转化为MQTTRes对象
        MQTTRes mqttRes = JSONUtil.toBean(jsonMessage, MQTTRes.class);
        //先判断res的状态
        String res = mqttRes.getRes();
        checkHardWareWrong.checkRes(res);
        //解析硬件传过来的地址，截取到的可能是id,ids,domain,所以我们用一个map标识一下
        Map<String, String> address = mqttReqFacade.parseHardWareAddress(mqttRes.getAdr());
        //获取硬件传来的信息
        Map<String, Object> data = mqttRes.getCt();
        //获取硬件的命令，这里也是看硬件的，原因还是那个自动上报的那个东西
        Integer rcmd = mqttRes.getRcmd();
        //将消息Id存入Redis
        stringRedisTemplate.opsForValue().set(UserHolder.getUser().getEmail()+MQTT_MESSAGE_ID+rcmd,mqttRes.getMid());
        String commandContent = commandService.getById(rcmd).getContent();
        checkHardWareWrong.checkString(commandContent, "未找到硬件传来的id为" + rcmd + "的命令");
        //将命令和地址都放进data中
        data.put("commandContent", commandContent);
        data.put("messageId",mqttRes.getMid());
        data.putAll(address);
        return data;
    }


    /**
     * 操作数据库
     */
    public Result executeDataBase(Map<String, Object> data) {
        //这里还是用一个remove方法，因为我们操作数据库是用不到这个键值对的
        return mqttServiceContext.executeService((String) data.remove("commandContent"), data);
    }

    /**
     * 总的MQTT的操作流程
     *
     * @param data
     * @param symbol
     * @param commandId
     * @return
     * 模块开启(模块 - 传感器13)
      {
      "adr":"AAAAIzE0IzE3IzE4",
      "msgid":"143954423154999297",
      "rcmd":13,
      "res":"0",
      "dytype":1,
      "data":{"port":0},
      "time":[23,1,19,22,40,10]
      }
     * 模块关闭(模块-传感器13)
      {
      "adr":"AAAAIzE0IzE3IzE4",
      "msgid":"143955166184341506",
      "rcmd":12,
      "res":"0",
      "dytype":1,
      "data":{"port":0},
      "time":[23,1,20,22,40,10]
      }
     * 节点重启(节点12)
      {
      "adr":"AAAAIzE0IzE3IzE4",
      "msgid":"142713181901422595",
      "rcmd":3,
      "res":"0",
      "dytype":1,
      "data":{"value":-1},
      "time":[23,1,20,22,40,10]
      }
     * 节点初始化(节点12)
      {
      "adr":"AAAAIzE0IzE3IzE4",
      "msgid":"142477199218311170",
      "rcmd":2,
      "res":"0",
      "dytype":1,
      "data":{"value":-1},
      "time":[23,1,20,22,40,10]
      }
     * 修改上报周期(节点12)
      {
      "adr":"AAAAIzE0IzE3IzE4",
      "msgid":"142712511886524418",
      "rcmd":17,
      "res":"0",
      "dytype":1,
      "data":{"report_interval":10},
      "time":[23,1,20,22,40,10]
      }

      获取定位(节点12)
       {
        "adr":"AAAAIzE0IzE3IzE4",
        "msgid":"142725864939847692",
        "rcmd":5,
        "res":"0",
        "dytype":1,
        "data":{"gps":[122.322,45.556]},
        "time":[23,1,20,22,40,10]
        }
     */
    public Result MQTTProcess(Object data, String symbol, Integer commandId) {
        String messageId = publishMessage(data, symbol, commandId);
        String jsonMessage = receiveMessage(messageId);
        Map<String, Object> hardWareData = parseMessage(jsonMessage);
        return executeDataBase(hardWareData);
    }

    /**
     * 解析 data,无论是什么 DTO 对象，我们都只要把他转成Map就行了
     */
    public Map<String, Object> parseData(Object data) {
        Map<String, Object> stringObjectMap = BeanUtil.beanToMap(data);
        if (stringObjectMap.isEmpty()) {
            throw new BusinessException(PARAMS_ERR.getMessage(), PARAMS_ERR.getCode(), "未解析到要给硬件的数据");
        }
        return stringObjectMap;
    }
}
