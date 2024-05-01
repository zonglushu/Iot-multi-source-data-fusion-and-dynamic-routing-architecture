package com.mayphyr.iotfeign.rpc;

import com.mayphyr.iotcommon.common.Result;
import com.mayphyr.iotcommon.model.vo.IotUserVo;
import com.mayphyr.iotcommon.model.vo.LoginUserVO;
import com.mayphyr.iotfeign.fallback.UserClientFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;


@FeignClient(value = "iot-usercenter",fallbackFactory = UserClientFallbackFactory.class)
public interface UserClient {
//    用动态代理实现接口，远程调用接口
    @GetMapping("/api/user/feign/{id}")
    String now2(@PathVariable("id") Integer id);



    @GetMapping("/api/iotuser/get/login")
    Result<IotUserVo> getLoginUser(@RequestParam("token") String token);
}
