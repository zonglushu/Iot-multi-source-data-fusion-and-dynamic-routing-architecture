package com.xiancai.lora.aop;


import com.xiancai.lora.annotation.SuperCheck;
import com.xiancai.lora.exception.BusinessException;
import com.xiancai.lora.utils.UserHolder;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import static com.xiancai.lora.enums.StatusCode.NO_PERMISSIONS;


/**
 * 超级用户校验 AOP
 */
@Aspect
@Component
public class SuperUserInterceptor {
    /**
     * 执行拦截
     *
     * @param joinPoint
     * @param superCheck
     * @return
     */
    @Around("@annotation(superCheck)")
    public Object doInterceptor(ProceedingJoinPoint joinPoint, SuperCheck superCheck) throws Throwable {
        Integer parentId = UserHolder.getUser().getParentId();
        if(parentId!=-1){
            throw new BusinessException(NO_PERMISSIONS.getMessage(), NO_PERMISSIONS.getCode(),
                    "该用户不是超级用户，无法访问该方法");
        }
        // 通过权限校验，放行
        return joinPoint.proceed();
    }
}
