package com.leemccormick.aopdemo;

import com.leemccormick.aopdemo.dao.AccountDAO;
import com.leemccormick.aopdemo.dao.MembershipDAO;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class AopdemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(AopdemoApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(AccountDAO theAccountDAO, MembershipDAO theMembershipDAO) {
        return runner -> {
            System.out.println("Hello world : Running the commandLineRunner here...");
            demoTheBeforeAdvice(theAccountDAO, theMembershipDAO);
        };
    }

    private void demoTheBeforeAdvice(AccountDAO theAccountDAO, MembershipDAO theMembershipDAO) {
        // To Test Calling first time
        System.out.println("\n Let's call addAccount()\n");
        theAccountDAO.addAccount();

        // To Test Calling Again, before this print out, it will print the beforeAddAccountAdvice() on MyDemoLoggingAspect Class
        System.out.println("\n Let's call it again to addAccount()\n");
        theAccountDAO.addAccount();

        // To Test with another class with addAccount()
        System.out.println("\n Let's call it again to addAccount() with Membership Class\n");
        theMembershipDAO.addAccount();

        // To Test with Membership class with addSillyMember()
        System.out.println("\n Let's call it again to addSillyMember() with Membership Class\n");
        theMembershipDAO.addSillyMember();

        // To Test with Membership class with return boolean addBooleanSillyMember()
        System.out.println("\n Let's call it again to addBooleanSillyMember() with Membership Class\n");
        theMembershipDAO.addBooleanSillyMember();

        Account account = new Account();
        // To Test with Account class with addAccountWithParams()
        System.out.println("\n Let's call it again to addAccountWithParams() with Account Class\n");
        theAccountDAO.addAccountWithParams(account);

        // To Test with Account class with addAccountWithParamsAccountAndBoolean()
        System.out.println("\n Let's call it again to addAccountWithParamsAccountAndBoolean() with Account Class\n");
        theAccountDAO.addAccountWithParamsAccountAndBoolean(account, true);

        // To Test with Account class with doWork()
        System.out.println("\n Let's call it again to doWork() with Account Class\n");
        theAccountDAO.doWork();

        // To Test with Membership class with goToSleep()
        System.out.println("\n Let's call it again to goToSleep() with Membership Class\n");
        theMembershipDAO.goToSleep();

        // To Call theAccountDAO getter/setter methods
        System.out.println("\n Let's call it again from theAccountDAO getter/setter methods\n");
        theAccountDAO.setName("foobar");
        theAccountDAO.setServiceCode("silver");
        String name = theAccountDAO.getName();
        String code = theAccountDAO.getServiceCode();

        // To test join point
        System.out.println("\n Let's call it again to test join point \n");
        Account myAccount = new Account();
        myAccount.setName("Madhu");
        myAccount.setLevel("Platinum");
        theAccountDAO.addAccountWithParamsAccountAndBoolean(myAccount, true);
        theAccountDAO.doWork();
    }
}
