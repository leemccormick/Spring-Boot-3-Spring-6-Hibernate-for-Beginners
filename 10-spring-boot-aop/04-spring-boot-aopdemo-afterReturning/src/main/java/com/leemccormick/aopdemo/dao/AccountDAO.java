package com.leemccormick.aopdemo.dao;

import com.leemccormick.aopdemo.Account;

import java.util.List;

public interface AccountDAO {
    void addAccount();

    void addAccountWithParams(Account theAccount);

    void addAccountWithParamsAccountAndBoolean(Account theAccount, Boolean vipFlag);

    boolean doWork();

    public String getName();

    public void setName(String name);

    public String getServiceCode();

    public void setServiceCode(String service);
    List<Account> findAccounts();
}
