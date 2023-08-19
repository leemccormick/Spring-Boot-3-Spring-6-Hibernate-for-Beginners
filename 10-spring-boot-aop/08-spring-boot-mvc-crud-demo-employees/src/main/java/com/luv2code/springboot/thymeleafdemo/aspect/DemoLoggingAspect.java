package com.luv2code.springboot.thymeleafdemo.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.logging.Logger;

@Aspect
@Component
public class DemoLoggingAspect {
    // 1) Set up logger
    private Logger myLogger = Logger.getLogger(getClass().getName());

    // 2) Set up Pointcut Declarations
    @Pointcut("execution(* com.luv2code.springboot.thymeleafdemo.controller.*.*(..))")
    private void forControllerPackage() {
    }

    @Pointcut("execution(* com.luv2code.springboot.thymeleafdemo.service.*.*(..))")
    private void forServicePackage() {
    }

    @Pointcut("execution(* com.luv2code.springboot.thymeleafdemo.dao.*.*(..))")
    private void forDaoPackage() {
    }

    // 3) Combine Pointcuts
    @Pointcut("forControllerPackage() || forServicePackage() || forDaoPackage()")
    private void forAppFlow() {
    }

    // 4) Add @Before Advice
    @Before("forAppFlow()")
    public void before(JoinPoint theJoinPoint) {
        // Display method we are calling
        String method = theJoinPoint.getSignature().toShortString();
        myLogger.info("====>> in @Before - calling method : " + method);

        // Display the arguments to the method
        Object[] args = theJoinPoint.getArgs();
        for (Object tempArg : args) {
            myLogger.info("====>> Argument is : " + tempArg + " | " + tempArg.toString());
        }
    }

    // 4) Add @Before Advice
    @AfterReturning(
            pointcut = "forAppFlow()",
            returning = "theResult"
    )
    public void afterReturning(JoinPoint theJoinPoint, Object theResult) {
        // Display method we are calling
        String method = theJoinPoint.getSignature().toShortString();
        myLogger.info("====>> in @AfterReturning - calling method : " + method);

        // Display the data return
        myLogger.info("====>> Result is : " + theResult);
    }
}

