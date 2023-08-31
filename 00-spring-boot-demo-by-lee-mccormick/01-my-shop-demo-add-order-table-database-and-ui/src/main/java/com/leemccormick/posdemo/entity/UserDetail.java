package com.leemccormick.posdemo.entity;

public class UserDetail {
    private User user;
    private String roles;
    private boolean hasCustomerRole;
    private boolean hasSaleRole;
    private boolean hasAdminRole;

    public UserDetail() {

    }

    public UserDetail(User user, String roles, boolean hasCustomerRole, boolean hasSaleRole, boolean hasAdminRole) {
        this.user = user;
        this.roles = roles;
        this.hasCustomerRole = hasCustomerRole;
        this.hasSaleRole = hasSaleRole;
        this.hasAdminRole = hasAdminRole;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getRoles() {
        return roles;
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }

    public boolean isHasCustomerRole() {
        return hasCustomerRole;
    }

    public void setHasCustomerRole(boolean hasCustomerRole) {
        this.hasCustomerRole = hasCustomerRole;
    }

    public boolean isHasSaleRole() {
        return hasSaleRole;
    }

    public void setHasSaleRole(boolean hasSaleRole) {
        this.hasSaleRole = hasSaleRole;
    }

    public boolean isHasAdminRole() {
        return hasAdminRole;
    }

    public void setHasAdminRole(boolean hasAdminRole) {
        this.hasAdminRole = hasAdminRole;
    }

    @Override
    public String toString() {
        return "UserDetail{" +
                "user=" + user +
                ", roles='" + roles + '\'' +
                ", hasCustomerRole=" + hasCustomerRole +
                ", hasSaleRole=" + hasSaleRole +
                ", hasAdminRole=" + hasAdminRole +
                '}';
    }
}
