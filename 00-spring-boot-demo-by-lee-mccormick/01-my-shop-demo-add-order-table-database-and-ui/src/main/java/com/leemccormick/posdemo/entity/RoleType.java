package com.leemccormick.posdemo.entity;

public enum RoleType {
    CUSTOMER("ROLE_CUSTOMER"),
    SALE("ROLE_SALE"),
    ADMIN("ROLE_ADMIN");

    private final String value;

    RoleType(String theValue) {
        this.value = theValue;
    }

    public String getValue() {
        return value;
    }

    public String getDescription() {
        String description = value;
        if (value.toLowerCase().contains("Customer".toLowerCase())) {
            description = "Customer";
        } else if (value.toLowerCase().contains("Sale".toLowerCase())) {
            description = "Sale";
        } else if (value.toLowerCase().contains("Admin".toLowerCase())) {
            description = "Admin";
        }
        return description;
    }
}
