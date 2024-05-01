package com.mayphyr.iotfeign.fallback.interfaces;

import com.mayphyr.iotcommon.constant.FeignConstant;
import com.mayphyr.iotcommon.model.dto.ApiInfo;
import com.mayphyr.iotcommon.model.entity.ApiPass;
import com.mayphyr.iotcommon.model.entity.InterfaceInfo;
import com.mayphyr.iotcommon.model.entity.InterfaceUser;
import com.mayphyr.iotfeign.rpc.interfaces.CheckAccessClient;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CheckAccessClientFallbackFactory implements FallbackFactory<CheckAccessClient> {
    @Override
    public CheckAccessClient create(Throwable cause) {
        return new CheckAccessClient() {
            @Override
            public ApiPass checkAccessUser(String accessKey) {
                log.error(FeignConstant.INVOKE_ERROR+"checkAccessUser方法",cause);
                return null;
            }

            @Override
            public InterfaceInfo checkInterfaceInfo(ApiInfo apiInfo) {
                log.error(FeignConstant.INVOKE_ERROR+"checkInterfaceInfo",cause);
                return null;
            }

            @Override
            public InterfaceUser hasRemainCount(Long interfaceId, Long userId) {
                log.error(FeignConstant.INVOKE_ERROR+"hasRemainCount",cause);
                return null;
            }

            @Override
            public void addDefaultInterfaceUser(Long interfaceId,  Long userId) {
                log.error(FeignConstant.INVOKE_ERROR+"hasRemainCount",cause);
            }

            @Override
            public void decreaseInvokeCount(Long interfaceId,  Long userIdr) {
                log.error(FeignConstant.INVOKE_ERROR+"hasRemainCount",cause);
            }
        };
    }
}
