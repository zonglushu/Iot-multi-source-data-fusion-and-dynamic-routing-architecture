package com.mayphyr.iotdecider.controller;


import com.mayphyr.iotapiclientsdk.client.SunApiClient;
import com.mayphyr.iotcommon.common.Result;
import com.mayphyr.iotfeign.rpc.UserClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/decider")

public class DecideController {
    @Resource
    private UserClient userClient;

    @GetMapping("/feign1")
    public void testFeign1(){

        String s = userClient.now2(new Integer(1));
        System.out.println(s);
    }

    @GetMapping("/prop")
    public Result testFeign2(){

        return Result.success("decider的prop方法被调用了");
    }
}
