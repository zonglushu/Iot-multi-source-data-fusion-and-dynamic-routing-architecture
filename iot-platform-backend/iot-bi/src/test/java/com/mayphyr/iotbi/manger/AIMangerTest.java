package com.mayphyr.iotbi.manger;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AIMangerTest {

    @Resource
    private AIManger aiManger;

    @Test
    void doChat(){
//        String re = aiManger.doChat("邓紫棋");
//        System.out.println(re);
    }
}