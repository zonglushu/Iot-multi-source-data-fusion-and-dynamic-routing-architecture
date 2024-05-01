package com.xiancai.lora.Interceptor;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.xiancai.lora.model.VO.UserVo;
import com.xiancai.lora.utils.UserHolder;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;


import static com.xiancai.lora.constant.RedisConstants.LOGIN_USER_KEY;
import static com.xiancai.lora.constant.RedisConstants.LOGIN_USER_TTL;

public class RefreshTokenInterceptor implements HandlerInterceptor {

    private StringRedisTemplate stringRedisTemplate;

    // 这里为什么要有一个构造器呢，是因为这个类没有交给 Spring 管理，Spring不会帮我们去创建他，我们要自己手动创建
    public RefreshTokenInterceptor(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // TODO 1. 获取请求头的token
        String token = request.getHeader("token");
        Map<String, String> resultError = new HashMap<>();
        response.setContentType("json/html; charset=utf-8");


        if (StrUtil.isBlank(token)) {
            resultError.put("code","401");
            resultError.put("message","token为null，请先传token");
            PrintWriter printWriter = response.getWriter();
            Object ces = JSONUtil.toJsonStr(resultError);
            printWriter.write(ces.toString());
            return false;
        }
        // TODO 2.基于Token获取redis中的用户
        Map<Object, Object> userMap = stringRedisTemplate.opsForHash().entries(LOGIN_USER_KEY + token);
        // 2.判断用户是否存在
        if (userMap.isEmpty() ) {
            resultError.put("code","401");
            resultError.put("message","请重新登录");
            PrintWriter printWriter = response.getWriter();
            Object ces = JSONUtil.toJsonStr(resultError);
            printWriter.write(ces.toString());
            return false;
        }
        // 5. TODO  将查到的用户从Hash转为userDTO
        UserVo userVo = BeanUtil.fillBeanWithMap(userMap, new UserVo(), false);
        // 6. 保存
        UserHolder.saveUser(userVo);

        // 7. 刷新token有效期
        stringRedisTemplate.expire(LOGIN_USER_KEY+token,LOGIN_USER_TTL, TimeUnit.SECONDS);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // 操作完成后，将登录的用户信息移除调，避免用户泄露
        UserHolder.removeUser();
    }
}
