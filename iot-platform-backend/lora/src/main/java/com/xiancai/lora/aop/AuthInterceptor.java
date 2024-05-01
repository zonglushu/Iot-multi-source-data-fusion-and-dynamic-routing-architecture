package com.xiancai.lora.aop;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.xiancai.lora.annotation.AuthCheck;

import com.xiancai.lora.exception.BusinessException;
import com.xiancai.lora.model.VO.UserVo;
import com.xiancai.lora.model.entity.Admin;
import com.xiancai.lora.model.entity.User;
import com.xiancai.lora.service.AdminService;
import com.xiancai.lora.service.UserService;
import com.xiancai.lora.utils.UserHolder;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static com.xiancai.lora.enums.StatusCode.*;


/**
 * 权限校验 AOP
 *
 * @author yupi
 */
@Aspect
@Component
public class AuthInterceptor {

    @Resource
    private AdminService adminService;

    /**
     * 执行拦截
     *
     * @param joinPoint
     * @param authCheck
     * @return
     */
    @Around("@annotation(authCheck)")
    public Object doInterceptor(ProceedingJoinPoint joinPoint, AuthCheck authCheck) throws Throwable {
        //将int数组转换为数值流,流中的元素全部装箱，转换为Integer流,将流转换为数组
        List<Integer> anyRole = Arrays.stream(Arrays.stream(authCheck.anyRole())
                .boxed().toArray(Integer[]::new)).collect(Collectors.toList());
        int mustRole = authCheck.mustRole();
        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
        // 当前登录用户
        UserVo user = UserHolder.getUser();
        // 先查看这个是不是管理员
        Admin admin = adminService.getById(user.getId());
        if(admin==null){
            throw new BusinessException(NO_AUTH.getMessage(), NO_AUTH.getCode(), "该用户不是管理员");
        }

        // 拥有任意权限即通过
        if (CollectionUtils.isNotEmpty(anyRole)) {
            Integer adminLevel = admin.getAdminLevel();
            if (!anyRole.contains(adminLevel)) {
                throw new BusinessException(NO_PERMISSIONS.getMessage(), NO_PERMISSIONS.getCode(), "该用户没有权限访问该方法");
            }
        }
        // 必须有所有权限才通过
        if (mustRole > -1) {
            Integer adminLevel = admin.getAdminLevel();
            if (adminLevel!=mustRole) {
                throw new BusinessException(NO_PERMISSIONS.getMessage(), NO_PERMISSIONS.getCode(), "该用户没有权限访问该方法");
            }
        }
        // 通过权限校验，放行
        return joinPoint.proceed();
    }
}

