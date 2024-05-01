package com.xiancai.lora;


import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xiancai.lora.MQTT.service.context.CallMethod;
import com.xiancai.lora.MQTT.service.context.MQTTServiceContext;
import com.xiancai.lora.MQTT.service.proxy.ProxyModuleService;
import com.xiancai.lora.MQTT.service.proxy.ProxyNodeService;
import com.xiancai.lora.MQTT.service.proxy.ProxyFactory;
import com.xiancai.lora.MQTT.util.BeanFactory;
import com.xiancai.lora.MQTT.util.address.NormalAddressHandler;
import com.xiancai.lora.MQTT.util.process.MQTTProcess;
import com.xiancai.lora.MQTT.util.res.MQTTReqProperty;

import com.xiancai.lora.constant.PurchasedDevice;
import com.xiancai.lora.mapper.DataMapper;
import com.xiancai.lora.model.DTO.mqtt.IO.IoInfo;
import com.xiancai.lora.model.DTO.mqtt.IO.MQTTIoInfo;
import com.xiancai.lora.model.entity.DataS;
import com.xiancai.lora.model.entity.Mapping;
import com.xiancai.lora.service.DataService;
import com.xiancai.lora.service.MappingService;
import com.xiancai.lora.service.ModuleService;
import com.xiancai.lora.service.NodeService;

import com.xiancai.lora.utils.Result;
import com.xiancai.lora.utils.wrong.check.CheckNormalWrong;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import javax.xml.crypto.Data;
import java.io.*;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static com.xiancai.lora.constant.UsuStatus.REFLECT_PREFIX;

@SpringBootTest
public class TimeTest {
    @Resource
    private MQTTProcess mqttProcess;

    @Resource
    private MQTTReqProperty mqttReqProperty;

    @Resource
    private ModuleService moduleService;

    @Resource
    private NodeService nodeService;

    @Resource
    private CheckNormalWrong checkNormalWrong;
    @Resource
    private NormalAddressHandler normalAddressHandler;

    @Resource
    private ProxyNodeService proxyNodeService;

    @Resource
    private ProxyFactory proxyFactory;


    @Resource
    private MQTTServiceContext mqttServiceContext;

    @Resource
    private BeanFactory beanFactory;

    @Resource
    private DataService dataService;

    @Resource
    private DataMapper dataMapper;

    @Resource
    private MappingService mappingService;

    @Test
    void test1(){

        // 先请求外部接口，拿到
        String url = String.format(PurchasedDevice.URL + "?deviceid=%s&start=%s&end=%s"
                , PurchasedDevice.DEVICE_ID, PurchasedDevice.START_TIME, PurchasedDevice.END_TIME);
        String jsonResult = HttpUtil.get(url);
        JSONArray array = JSONUtil.parseArray(jsonResult);
        List<DataS> dataList=new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (Object obj : array.stream().toArray()) {
            JSONObject sObj = (JSONObject) obj;
            Date date = sObj.get("dtime", Date.class);
            String EnType = sObj.get("item", String.class);
            Mapping en = mappingService.query().eq("en", EnType).one();
            DataS build = DataS.builder().dataType(en.getCh()).data(sObj.get("data", String.class))
                    .dataTime(date).build();
            dataList.add(build);
        }
        for (DataS dataS : dataList) {
            System.out.println(dataS);
        }
//        dataService.saveBatch(dataList);

    }

    @Test
    void test() throws IOException {
        //long转换为字节数组
        BigInteger n = new BigInteger("1677246568000");
        byte[] bytes = n.toByteArray();
        int[] array_1=new int[bytes.length];
        for (int i = 0; i < bytes.length; i++) {
            if(bytes[i]<0){
                array_1[i]=bytes[i]+256;
            }else {
                array_1[i]=bytes[i];
            }
        }
        System.out.println("整型");
        for (int i = 0; i < array_1.length; i++) {
            System.out.print(array_1[i]+" ");
        }

//        int[] array = Base64.longToBytesLittle(n);
//
//        System.out.println("byte数组");
//        for (int i = 0; i < array.length; i++) {
//            System.out.print(array[i]+" ");
//        }

//        StringBuilder outPut=new StringBuilder();
//        Base64.encode(array_1.length,array_1,outPut);
//        System.out.println("原始数据"+n);
//        System.out.println("经过Base64加密后的数据"+outPut.toString());
//        int[] de_byte=new int[64];
//        Integer integer = Base64.deCode(outPut.toString(), outPut.length(), de_byte);
//        long x=0;
//        for (int i = 0; i < integer; i++) {
//            x+=de_byte[i]<<(i*8);
//        }
//        System.out.println("要解密的数据"+outPut.toString());
//        System.out.println("解密后的数据"+x);
    }


}
