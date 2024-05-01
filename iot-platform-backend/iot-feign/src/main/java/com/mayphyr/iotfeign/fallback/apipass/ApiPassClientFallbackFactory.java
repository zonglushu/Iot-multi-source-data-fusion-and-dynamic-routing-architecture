package com.mayphyr.iotfeign.fallback.apipass;

import com.mayphyr.iotcommon.common.Result;
import com.mayphyr.iotcommon.constant.FeignConstant;
import com.mayphyr.iotcommon.model.dto.apipass.AddApiPassRequest;
import com.mayphyr.iotcommon.model.dto.apipass.GenApiPassRequest;
import com.mayphyr.iotcommon.model.dto.iotuser.FeignUserAKSK;
import com.mayphyr.iotcommon.model.entity.ApiPass;
import com.mayphyr.iotfeign.rpc.apipass.ApiPassClient;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ApiPassClientFallbackFactory implements FallbackFactory<ApiPassClient> {
    @Override
    public ApiPassClient create(Throwable cause) {
        return new ApiPassClient() {
            @Override
            public Result<Boolean> genApiPass(GenApiPassRequest genApiPassRequest) {
                log.error(FeignConstant.INVOKE_ERROR+"genApiPass",cause);
                return Result.error(false,"feign远程调用失败");
            }

            @Override
            public Result<Boolean> addApiPass(AddApiPassRequest addApiPassRequest) {
                log.error(FeignConstant.INVOKE_ERROR+"addApiPass",cause);
                return Result.error(false,"feign远程调用失败");
            }

            @Override
            public Result<ApiPass> getAKSK(  Long userId) {
                log.error(FeignConstant.INVOKE_ERROR+"getAKSK",cause);
                return Result.error(null,"feign远程调用失败");
            }
        };
    }
}
