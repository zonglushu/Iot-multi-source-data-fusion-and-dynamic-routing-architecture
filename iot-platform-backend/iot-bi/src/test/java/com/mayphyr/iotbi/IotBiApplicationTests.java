package com.mayphyr.iotbi;

import com.mayphyr.iotbi.manger.AIManger;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
class IotBiApplicationTests {

    @Resource
    private AIManger aiManger;

    @Test
    void doChat(){
//        String re = aiManger.doChat("\n" +
//                "分析需求：\n" +
//                "分析网站用户的增长情况：\n" +
//                "原始数据：\n" +
//                "日期：，用户数\n" +
//                "1号，10\n" +
//                "2号，20\n" +
//                "3号，30");
//        System.out.println(re);
    }
}
