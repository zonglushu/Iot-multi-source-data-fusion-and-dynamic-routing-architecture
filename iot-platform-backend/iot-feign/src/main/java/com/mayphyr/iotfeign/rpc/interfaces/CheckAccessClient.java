package com.mayphyr.iotfeign.rpc.interfaces;

import com.mayphyr.iotcommon.model.dto.ApiInfo;
import com.mayphyr.iotcommon.model.entity.ApiPass;
import com.mayphyr.iotcommon.model.entity.InterfaceInfo;
import com.mayphyr.iotcommon.model.entity.InterfaceUser;
import com.mayphyr.iotfeign.fallback.interfaces.CheckAccessClientFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(value = "iot-decider",contextId = "check-access",fallbackFactory = CheckAccessClientFallbackFactory.class)
public interface CheckAccessClient {

    //TODO 看decider接口里面需不需要加api前缀
    @GetMapping("/api/decider/check/access/{accessKey}")
    ApiPass checkAccessUser(@PathVariable("accessKey") String accessKey);



    @PostMapping("/api/check/interface")
    InterfaceInfo checkInterfaceInfo(@RequestBody ApiInfo apiInfo);

    /**
     * 判断该接口，该用户是否还有调用次数
     * @param interfaceId
     * @param userId
     * @return
     */
    @GetMapping("/api/interface/remain")
    InterfaceUser hasRemainCount(@RequestParam("interfaceId") Long interfaceId, @RequestParam("userId") Long userId);

    /**
     * 添加接口-用户数据
     * @param interfaceId
     * @param userId
     */
    @GetMapping("/api/interface/add")
    void addDefaultInterfaceUser(@RequestParam("interfaceId") Long interfaceId, @RequestParam("userId") Long userId);

    /**
     * 更新接口调用次数
     * @param interfaceId
     * @param userId
     */
    @PostMapping("/api/interface/decrease")
    void decreaseInvokeCount(@RequestParam("interfaceId") Long interfaceId, @RequestParam("userId") Long userId);
}
