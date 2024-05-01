package com.mayphyr.iotfeign.rpc.apipass;

import com.mayphyr.iotcommon.common.Result;
import com.mayphyr.iotcommon.model.dto.apipass.AddApiPassRequest;
import com.mayphyr.iotcommon.model.dto.apipass.GenApiPassRequest;
import com.mayphyr.iotcommon.model.entity.ApiPass;
import com.mayphyr.iotfeign.fallback.UserClientFallbackFactory;
import com.mayphyr.iotfeign.fallback.apipass.ApiPassClientFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;


@FeignClient(value = "iot-decider",contextId = "api-pass",fallbackFactory = ApiPassClientFallbackFactory.class)
public interface ApiPassClient {
//    用动态代理实现接口，远程调用接口

    @PostMapping("/api/decider/pass/gen")
    Result<Boolean> genApiPass(@RequestBody GenApiPassRequest genApiPassRequest);

    @PostMapping("/api/pass/add")
    Result<Boolean> addApiPass(@RequestBody AddApiPassRequest addApiPassRequest);

    /**
     * 根据用户id获取AK和SK
     *
     * @param userId
     * @return
     */
    @RequestMapping(value = "/api/decider/pass/get/all/{userid}", method = RequestMethod.GET)
    Result<ApiPass> getAKSK(@PathVariable("userid") Long userId);
}





