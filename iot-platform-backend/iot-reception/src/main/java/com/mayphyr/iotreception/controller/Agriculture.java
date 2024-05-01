package com.mayphyr.iotreception.controller;


import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;

@Resource
@RequestMapping("/agr")
public class Agriculture {



    @PostMapping("/send")
    public void sendMessage(){
//        mqttProcess.MQTTProcess();
    }
}
