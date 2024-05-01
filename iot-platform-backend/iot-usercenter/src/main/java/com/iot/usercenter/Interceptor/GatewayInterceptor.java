package com.iot.usercenter.Interceptor;

import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Enumeration;

public class GatewayInterceptor implements HandlerInterceptor {
    private static final String GATE_WAY_HEADER = "mayphyr";
    private static final String GATE_WAY_HEADER_VALUE = "gateway";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 获取请求头中的染色数据
        String dyeData = request.getHeader(GATE_WAY_HEADER);

        System.out.println(dyeData+"----------------------");        // 获取全部请求头名称
//        Enumeration<String> headerNames = request.getHeaderNames();
//        while (headerNames.hasMoreElements()) {
//            String headerName = headerNames.nextElement();
//            // 获取请求头的值
//            String headerValue = request.getHeader(headerName);
//            System.out.println(headerName + ": " + headerValue);
//        }
        if (dyeData == null || !dyeData.equals(GATE_WAY_HEADER_VALUE)) {
            // 如果染色数据不存在或者不匹配，则返回错误响应
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return false;
        }

        // 继续向下执行
        return true;
    }
}
