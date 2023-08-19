package com.leemccormick.aopdemo.aspect;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

/* Ordering Aspects
 1) Refactor: Place advices in separate Aspects
 2) Add @Order Annotation
 */

@Aspect
public class LuvAopExpressions {
    // 1) Pointcut Declaration --> Create a pointcut declaration --> forDaoPackage() We can named it anything.
    @Pointcut("execution(* com.leemccormick.aopdemo.dao.*.*(..))")
    public void forDaoPackage() {
    }

    // Combining Pointcut
    // Combining Pointcut --> Create a pointcut for getter method
    @Pointcut("execution(* com.leemccormick.aopdemo.dao.*.get*(..))")
    public void getter() {
    }

    // Combining Pointcut --> Create a pointcut for setter method
    @Pointcut("execution(* com.leemccormick.aopdemo.dao.*.set*(..))")
    public void setter() {
    }

    // Combining Pointcut --> Create a pointcut to include package or exclude getter/setter
    @Pointcut("forDaoPackage() && !(getter() || setter())")
    public void forDaoPackageNoGetterSetter() {
    }
}
