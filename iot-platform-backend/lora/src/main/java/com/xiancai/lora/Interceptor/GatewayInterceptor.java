package com.xiancai.lora.Interceptor;

import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class GatewayInterceptor implements HandlerInterceptor {
    private static final String GATE_WAY_HEADER = "origin";
    private static final String GATE_WAY_HEADER_VALUE = "gateway";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 获取请求头中的染色数据
        String dyeData = request.getHeader(GATE_WAY_HEADER);

        if (dyeData == null || !dyeData.equals(GATE_WAY_HEADER_VALUE)) {
            // 如果染色数据不存在或者不匹配，则返回错误响应
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return false;
        }

        // 继续向下执行
        return true;
    }
}
