package com.mayphyr.iotfeign.fallback;

import com.mayphyr.iotcommon.common.Result;
import com.mayphyr.iotcommon.model.vo.IotUserVo;
import com.mayphyr.iotcommon.model.vo.LoginUserVO;
import com.mayphyr.iotfeign.rpc.UserClient;
import lombok.extern.slf4j.Slf4j;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;


@Slf4j
public class UserClientFallbackFactory implements FallbackFactory<UserClient> {

    @Override
    public UserClient create(Throwable cause) {
        return new UserClient() {
//            因为
            @Override
            public String now2(Integer id) {
                log.error("feign远程调用now2方法发生异常",cause);
                return "尝试用feign远程调用now2方法失败";
            }

            @Override
            public Result<IotUserVo> getLoginUser(String token) {
                log.error("feign远程调用iot-user的getLoginUser方法发生异常",cause);
                return null;
            }
        };
    }
}
