package com.mayphyr.iotcommon;

import cn.hutool.core.convert.Convert;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.mayphyr.iotcommon.model.dto.gateway.GatewayPredicateDefinition;
import com.mayphyr.iotcommon.model.dto.gateway.GatewayRouteDefinition;
import com.mayphyr.iotcommon.utils.converts.TypeConverter;
import com.mayphyr.iotcommon.utils.converts.TypeConverterFactory;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class IotCommonApplicationTests {

    @Test
    void contextLoads() {

        JSONArray jsonObject = JSONUtil.parseArray("[\n" +
                "        {\n" +
                "            \"name\":\"Before\",\n" +
                "            \"args\":{\"datetime\":\"2012-2-3-4\"}\n" +
                "            \n" +
                "        },\n" +
                "        {\n" +
                "            \"name\":\"Path\",\n" +
                "            \"args\":{\"pattern\":\"/api/user/get\"}\n" +
                "        }\n" +
                "    ]");
        List<GatewayPredicateDefinition> list = JSONUtil.toList(jsonObject, GatewayPredicateDefinition.class);
        String string = list.toString();
        System.out.println(string);
    }



    @Test
    void  ttt(){
        String s="{\n" +
                "\"name\": \n" +
                "\"John Doe\",\n" +
                "\"age\": \n" +
                "30,\n" +
                "\"email\": \n" +
                "\"john@example.com\"\n" +
                "}";
        TypeConverter typeConverter = TypeConverterFactory.getTypeConverter("object");
        Object convert = typeConverter.convert(s);
        System.out.println(convert.toString());

    }

}
