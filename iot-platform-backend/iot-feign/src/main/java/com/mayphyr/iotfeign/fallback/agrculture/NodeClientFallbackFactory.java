package com.mayphyr.iotfeign.fallback.agrculture;

import com.mayphyr.iotcommon.common.Result;
import com.mayphyr.iotfeign.rpc.agrculture.NodeClient;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;


@Slf4j
public class NodeClientFallbackFactory implements FallbackFactory<NodeClient> {
    @Override
    public NodeClient create(Throwable cause) {
        return new NodeClient() {
            @Override
            public Result poweron(Map<String, Object> data) {
                log.error("feign远程调用now2方法发生异常",cause);
                return Result.error(null,"尝试用feign远程调用now2方法失败");
            }
        };
    }
}
