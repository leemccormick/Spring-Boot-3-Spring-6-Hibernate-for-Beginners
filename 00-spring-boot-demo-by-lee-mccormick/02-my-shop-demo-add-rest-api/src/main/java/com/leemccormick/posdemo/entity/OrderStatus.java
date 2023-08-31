package com.leemccormick.posdemo.entity;

public enum OrderStatus {
    PENDING("Pending"),
    PROCESSING("Processing"),
    SHIPPED("Shipped"),
    DELIVERED("Delivered"),
    CANCELLED("Cancelled");

    private final String value;

    OrderStatus(String theValue) {
        this.value = theValue;
    }

    public String getValue() {
        return value;
    }
}
