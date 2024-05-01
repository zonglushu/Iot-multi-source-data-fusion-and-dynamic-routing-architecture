package com.xiancai.lora.Interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

@Configuration
public class MVCConfig implements WebMvcConfigurer {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        String[] excludePatterns = new String[]{"/swagger-resources/**", "/webjars/**", "/v3/**",
                 "/doc.html/**","/favicon.ico"};
        // 登录拦截器
//        registry.addInterceptor(new LoginInterceptor()).addPathPatterns("/**")
//                .excludePathPatterns(
//                        "/user/login",
//                        "/sensor/other/data/vr",
//                        "/user/register/email",
//                        "/user/modify/email",
//                        "/user/register",
//                        "/notice/notice",
//                        "/user/modify",
//                        "/user/forget/password",
//                        "/swagger-resources/**", "/webjars/**", "/v3/**",
//                        "/doc.html/**","/favicon.ico"
//                ).order(1);
//        registry.addInterceptor(new RefreshTokenInterceptor(stringRedisTemplate)).order(0).addPathPatterns("/**")
//                .excludePathPatterns("/doc.html/**",
//                        "/user/login",
//                        "/user/register/email",
//                        "/user/modify/email",
//                        "/user/modify",
//                        "/notice/notice",
//                        "/user/forget/password",
//                        "/user/register",
//                        "/user/code",
//                        "/sensor/other/data/vr").excludePathPatterns(excludePatterns);
//        registry.addInterceptor(new GatewayInterceptor()).addPathPatterns("/**");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**").addResourceLocations("classpath:/static/");
        registry.addResourceHandler("doc.html").addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/favicon.ico").addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
    }

}
