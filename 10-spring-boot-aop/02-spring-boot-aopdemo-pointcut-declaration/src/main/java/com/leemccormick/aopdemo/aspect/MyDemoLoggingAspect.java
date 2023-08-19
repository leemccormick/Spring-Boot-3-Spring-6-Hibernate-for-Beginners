package com.leemccormick.aopdemo.aspect;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/* Pointcut Declaration
1) Create a pointcut declaration
2) Apply pointcut declaration
*/

/* Combining Pointcut
1) Create a pointcut declarations
2) Combine pointcut declarations
3) Apply pointcut declaration to advices
*/

/* Ordering Aspects
 1) Refactor: Place advices in separate Aspects
 2) Add @Order Annotation
 */

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

    // 1) Pointcut Declaration --> Create a pointcut declaration --> forDaoPackage() We can named it anything.
    @Pointcut("execution(* com.leemccormick.aopdemo.dao.*.*(..))")
    private void forDaoPackage() {
    }

    // Combining Pointcut
    // Combining Pointcut --> Create a pointcut for getter method
    @Pointcut("execution(* com.leemccormick.aopdemo.dao.*.get*(..))")
    private void getter() {
    }

    // Combining Pointcut --> Create a pointcut for setter method
    @Pointcut("execution(* com.leemccormick.aopdemo.dao.*.set*(..))")
    private void setter() {
    }

    // Combining Pointcut --> Create a pointcut to include package or exclude getter/setter
    @Pointcut("forDaoPackage() && !(getter() || setter())")
    private void forDaoPackageNoGetterSetter() {
    }

    // 2) Pointcut Declaration --> Apply pointcut declaration
    @Before("forDaoPackage()")
    public void beforeAddAccountAdvice() {
        System.out.println("\n ===> EXECUTE  @Before advice on method()");
    }

    // 3) We can reuse pointcut declaration
    @Before("forDaoPackage()")
    public void performApiAnalytics() {
        System.out.println("\n ===> Performing API Analytic");
    }

    @Before("forDaoPackageNoGetterSetter()")
    public void getterSetter() {
        System.out.println("\n ===> Combining Getter/Setter");
    }
}
