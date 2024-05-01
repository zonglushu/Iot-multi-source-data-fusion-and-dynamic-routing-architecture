package com.mayphyr.iotcommon.enums.MQTT;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Data
@Component
public class MQTTAddress {

    private Map<String, String> address = new HashMap<>();


    public MQTTAddress() {
        /**
         * 用 id和$拼接地址
         */
        address.put("$", "NormalAddress");
        /**
         * 用 id,ids,$,#拼接地址
         */
        address.put("@", "");
        /**
         * 用 domain,%拼接地址
         */
        address.put("%", "");
        /**
         * 用 ids，#拼接地址
         */
        address.put("#", "IdsAddress");
    }

    public String getClassPath(String symbol){
        for (String key : address.keySet()) {
            if(symbol.equals(key)){
                return address.get(key);
            }
        }
        return null;
    }
}
