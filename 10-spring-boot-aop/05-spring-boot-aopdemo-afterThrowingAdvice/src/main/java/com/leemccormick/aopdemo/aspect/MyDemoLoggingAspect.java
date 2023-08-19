package com.leemccormick.aopdemo.aspect;

import com.leemccormick.aopdemo.Account;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.List;

/* Pointcut Declaration
1) Create a pointcut declaration
2) Apply pointcut declaration
*/

/* Combining Pointcut
1) Create a pointcut declarations
2) Combine pointcut declarations
3) Apply pointcut declaration to advices
*/

// @Aspect --> Java Class for AOP
@Aspect
@Component
@Order(2)
public class MyDemoLoggingAspect {
    // This is where we add all of our related advices for logging
    // @AfterThrowing
    // Add a new advice for @AfterThrowing  on the findAccounts method
    @AfterThrowing(
            pointcut = "execution(* com.leemccormick.aopdemo.dao.AccountDAO.findAccounts(..))",
            throwing = "theExc"
    )
    public void afterThrowingFindAccountsAdvice(JoinPoint theJointPoint, Throwable theExc) {
        // Print out which method we are advising on
        String method = theJointPoint.getSignature().toLongString();
        System.out.println("\n ===> EXECUTE  @AfterThrowing on method: " + method);

        // Print out the exception for the method call
        System.out.println("\n ===> theExc is : " + theExc);
    }

    // @AfterReturning
    // Add a new advice for @AfterReturning  on the findAccounts method
    @AfterReturning(
            pointcut = "execution(* com.leemccormick.aopdemo.dao.AccountDAO.findAccounts(..))",
            returning = "result"
    )
    public void afterReturningFindAccountsAdvice(JoinPoint theJointPoint, List<Account> result) {
        // Print out which method we are advising on
        String method = theJointPoint.getSignature().toShortString();
        System.out.println("\n ===> EXECUTE  @AfterReturning on method: " + method);

        // Print out the result of the method call
        System.out.println("\n ===> result is : " + result);

        // Let's post-process the data, let modify it
        convertAccountNamesToUpperCase(result);
        System.out.println("\n ===> result after modify is : " + result);
    }

    private void convertAccountNamesToUpperCase(List<Account> result) {
        for (Account tempAccount : result) {
            String theUpperName = tempAccount.getName().toUpperCase();
            tempAccount.setName(theUpperName);
        }
    }

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

    // 2) Pointcut Declaration --> Apply pointcut declaration
    @Before("com.leemccormick.aopdemo.aspect.LuvAopExpressions.forDaoPackage()")
    public void beforeAddAccountAdvice(JoinPoint theJointPoint) {
        System.out.println("\n ===> EXECUTE  @Before advice on method()");

        // JoinPoints
        // 1) Display the method signature
        MethodSignature methodSignature = (MethodSignature) theJointPoint.getSignature();
        System.out.println("Method : " + methodSignature);

        // 2) Display method arguments
        // 2.1) Get args
        Object[] args = theJointPoint.getArgs();

        // 2.2) Loop thru args
        for (Object tempArg : args) {
            System.out.println("Temp Arg : " + tempArg);

            if (tempArg instanceof Account) {
                // Downcast and print Account specific stuff
                Account theAccount = (Account) tempArg;
                System.out.println("Account name : " + theAccount.getName());
                System.out.println("Account level : " + theAccount.getLevel());
            }
        }
    }

    // 3) We can reuse pointcut declaration
    @Before("com.leemccormick.aopdemo.aspect.LuvAopExpressions.forDaoPackageNoGetterSetter()")
    public void getterSetter() {
        System.out.println("\n ===> Combining Getter/Setter");
    }
}
