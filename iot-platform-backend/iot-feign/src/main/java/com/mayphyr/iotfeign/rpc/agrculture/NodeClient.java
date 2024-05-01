package com.mayphyr.iotfeign.rpc.agrculture;


import com.mayphyr.iotcommon.common.Result;
import com.mayphyr.iotfeign.fallback.UserClientFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Map;


@FeignClient(value = "lora-backend",fallbackFactory = UserClientFallbackFactory.class)
public interface NodeClient {




    @PostMapping("/api/lora/ms/module/poweron")
    Result poweron(Map<String, Object> data);

}
