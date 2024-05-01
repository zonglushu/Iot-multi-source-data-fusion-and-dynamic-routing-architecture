package com.mayphyr.iotdecider.sentinel;

import com.alibaba.csp.sentinel.adapter.spring.webmvc.callback.RequestOriginParser;
import org.apache.commons.lang3.StringUtils;
import org.checkerframework.checker.units.qual.C;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * 这个类是给sentinel用的，作用是给请求加上一个origin的请求头，
 * 这样配合sentinel的控制面板就可以，控制浏览器的路径不能访问
 */
@Component
public class HeaderOriginParser implements RequestOriginParser {
    @Override
    public String parseOrigin(HttpServletRequest httpServletRequest) {
        String origin = httpServletRequest.getHeader("origin");
        if(StringUtils.isEmpty(origin)){
            origin="blank";

        }
        return origin;

    }
}
