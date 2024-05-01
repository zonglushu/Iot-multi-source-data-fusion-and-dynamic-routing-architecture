package com.mayphyr.iotgateway;


import com.mayphyr.iotfeign.rpc.UserClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class IotGatewayApplicationTests {

    @Autowired
    private UserClient userClient;

    @Test
    void contextLoads() {

        String s = userClient.now2(new Integer(1));
        System.out.println(s);
    }

}
