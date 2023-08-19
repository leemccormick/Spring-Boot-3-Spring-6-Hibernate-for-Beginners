package com.leemccormick.aopdemo.aspect;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

// @Aspect --> Java Class for AOP
@Aspect
@Component
public class MyDemoLoggingAspect {
    // This is where we add all of our related advices for logging

    // Let's start with an @Before advice
    /*
    - beforeAddAccountAdvice() --> Can be any name
    - @Before("execution(public void updateAccount())") --> if no calls on this function, it will not run anything inside..
    - @Before("execution(public void addAccount())")  --> Call before all classes with addAccount() functions
    - @Before("execution(public void com.leemccormick.aopdemo.dao.AccountDAO.addAccount())") --> Only call on AccountDAO Class
    - @Before("execution(public void add*())") --> Call when every with add*() functions, ex. addMember() or addAccount()
    - @Before("execution(void add*())") --> Only call add*() with void return
    - @Before("execution(* add*())") --> Call with add*() with any return type, but not with parameters
    - @Before("execution(* add*(com.leemccormick.aopdemo.Account))") -->  Call with add*() with any return type with specific params
    - @Before("execution(* add*(Account))") --> This won't work, need fully-qualify class name
    - @Before("execution(* add*(com.leemccormick.aopdemo.Account, ..))") --> Call with add*() with any return type with specific params and any params
    - @Before("execution(* add*(..))") --> Call with add*() with any return type with any params
    - @Before("execution(* com.leemccormick.aopdemo.dao.*.*(..))") --> Call when match methods in a package
    */

    @Before("execution(* com.leemccormick.aopdemo.dao.*.*(..))")
    public void beforeAddAccountAdvice() {
        System.out.println("\n ===> EXECUTE  @Before advice on method()");
    }
}
