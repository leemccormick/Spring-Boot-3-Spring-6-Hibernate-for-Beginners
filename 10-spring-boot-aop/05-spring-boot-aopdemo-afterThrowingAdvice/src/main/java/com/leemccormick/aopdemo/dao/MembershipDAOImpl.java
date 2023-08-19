package com.leemccormick.aopdemo.dao;

import org.springframework.stereotype.Repository;

// @Repository for component scanning
@Repository
public class MembershipDAOImpl implements MembershipDAO {

    @Override
    public void addAccount() {
        System.out.println(getClass() + ": DOING MY DB WORK : ADDING A MEMBERSHIP ACCOUNT");
    }

    @Override
    public void addSillyMember() {
        System.out.println(getClass() + ": DOING MY DB WORK : ADDING A SILLY MEMBERSHIP ACCOUNT");
    }

    @Override
    public boolean addBooleanSillyMember() {
        System.out.println(getClass() + ": DOING MY DB WORK : ADDING A SILLY MEMBERSHIP ACCOUNT AND RETURN BOOLEAN");
        return false;
    }

    @Override
    public void goToSleep() {
        System.out.println(getClass() + ": DOING MY DB WORK : AT MEMBERSHIP ACCOUNT : GO TO SLEEP");
    }
}
