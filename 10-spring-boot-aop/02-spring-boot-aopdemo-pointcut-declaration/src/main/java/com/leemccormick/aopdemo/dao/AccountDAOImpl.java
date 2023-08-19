package com.leemccormick.aopdemo.dao;

import com.leemccormick.aopdemo.Account;
import org.springframework.stereotype.Repository;

// @Repository for component scanning
@Repository
public class AccountDAOImpl implements AccountDAO {
    private String name;
    private String serviceCode;

    @Override
    public void addAccount() {
        System.out.println(getClass() + ": DOING MY DB WORK : ADDING AN ACCOUNT");
    }

    @Override
    public void addAccountWithParams(Account theAccount) {
        System.out.println(getClass() + ": DOING MY DB WORK : ADDING AN ACCOUNT WITH PARAMS");
    }

    @Override
    public void addAccountWithParamsAccountAndBoolean(Account theAccount, Boolean vipFlag) {
        System.out.println(getClass() + ": DOING MY DB WORK : ADDING AN ACCOUNT WITH PARAMS --> Account and Boolean");
    }

    @Override
    public boolean doWork() {
        System.out.println(getClass() + ": DOING MY DB WORK : DO WORK");
        return false;
    }

    public String getName() {
        System.out.println(getClass() + ": in getName()");
        return name;
    }

    public void setName(String name) {
        System.out.println(getClass() + ": in setName()");
        this.name = name;
    }

    public String getServiceCode() {
        System.out.println(getClass() + ": in getServiceCode()");
        return serviceCode;
    }

    public void setServiceCode(String service) {
        System.out.println(getClass() + ": in setServiceCode()");
        this.serviceCode = service;
    }
}
