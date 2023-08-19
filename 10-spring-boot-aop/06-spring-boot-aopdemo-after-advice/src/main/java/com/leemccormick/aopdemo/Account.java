package com.leemccormick.aopdemo;

/* @AfterReturning
1) Add constructors to Account class
2) Add new method findAccounts() in AccountsDAO
3) Update main app to call the new method: findAccounts()
4) Add @AfterReturning advice
*/

public class Account {
    private String name;
    private String level;

    public Account() {

    }

    public Account(String name, String level) {
        this.name = name;
        this.level = level;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    @Override
    public String toString() {
        return "Account{" +
                "name='" + name + '\'' +
                ", level='" + level + '\'' +
                '}';
    }
}
