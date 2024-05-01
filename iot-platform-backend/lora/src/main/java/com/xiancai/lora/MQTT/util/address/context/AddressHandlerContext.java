package com.xiancai.lora.MQTT.util.address.context;

import com.xiancai.lora.MQTT.util.BeanFactory;
import com.xiancai.lora.MQTT.util.address.AbstractAddressHandler;
import com.xiancai.lora.enums.MQTT.MQTTAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;

import static com.xiancai.lora.constant.UsuStatus.ADDRESS_REFLECT_PREFIX;
import static com.xiancai.lora.constant.UsuStatus.ADDRESS_REFLECT_SUFFIX;


@Component
public class AddressHandlerContext {
    private AbstractAddressHandler addressHandler;

    @Autowired
    private MQTTAddress mqttAddress;

    @Resource
    private BeanFactory beanFactory;

    public void produceAddressHandler(String symbol){

        try {
            String classPath=ADDRESS_REFLECT_PREFIX + mqttAddress.getClassPath(symbol) + ADDRESS_REFLECT_SUFFIX;
            Class<?> aClass = Class.forName(classPath);
            addressHandler =(AbstractAddressHandler) beanFactory.getApplicationContext().getBean(aClass);
        } catch (Exception e) {
            throw new RuntimeException("地址处理器创建异常"+e.getMessage());
        }

    }

    /**
     * 给硬件的地址
     * @param nodeId
     * @return
     */
    public String produceAddress(Integer nodeId){
       return addressHandler.produceAddress(nodeId);
    }

    /**
     * 解析硬件传来的地址，先把Base64的编码转换为正常格式，再进行拆分
     * @param address
     * @return
     */
    public Map<String, String> parseHardWareAddress(String address){
        return addressHandler.parseHardWareAddress(address);
    }


}
