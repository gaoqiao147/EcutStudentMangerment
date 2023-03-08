package com.ecut.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 *@Author: 周伟
 *@CreateTime: 2023-03-08  15:52
 *@Version: 1.0
 */

@Aspect
@Component
public class MyAspect {
    /**
     * 定义切点
     */
    @Pointcut(value = "execution(* com.ecut.controller.LoginController.loginVerify(..))")
    public void pointCut(){

    }
    /**
     * 环绕通知
     */
    @Around(value = "pointCut()")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {

        Object proceed = pjp.proceed();
        System.out.println(proceed);
        return proceed;
    }
}
