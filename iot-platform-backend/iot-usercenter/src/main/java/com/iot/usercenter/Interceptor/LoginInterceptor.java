package com.iot.usercenter.Interceptor;

import cn.hutool.json.JSONUtil;
import com.iot.usercenter.utils.UserHolder;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Map<String, String> resultError = new HashMap<>();
        response.setContentType("json/html; charset=utf-8");
        if (UserHolder.getUser() == null) {
            // 无用户，需要拦截
            resultError.put("code","401");
            resultError.put("message","当前无用户登录，请先登录");
            PrintWriter printWriter = response.getWriter();
            Object ces = JSONUtil.toJsonStr(resultError);
            printWriter.write(ces.toString());
            return false;
        }
        // 有用户，放行
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // 操作完成后，将登录的用户信息移除调，避免用户泄露
        UserHolder.removeUser();
    }
}
