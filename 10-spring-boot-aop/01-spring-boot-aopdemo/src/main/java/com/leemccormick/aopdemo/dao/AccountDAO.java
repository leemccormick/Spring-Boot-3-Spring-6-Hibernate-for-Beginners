package com.leemccormick.aopdemo.dao;

import com.leemccormick.aopdemo.Account;

public interface AccountDAO {
    void addAccount();

    void addAccountWithParams(Account theAccount);

    void addAccountWithParamsAccountAndBoolean(Account theAccount, Boolean vipFlag);

    boolean doWork();
}
