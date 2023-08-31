package com.leemccormick.posdemo.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "roles")
public class Role {
    @Id
    @GeneratedValue
    @Column(name = "user_id")
    private String userId;

    @Column(name = "role")
    private String roleDescription;

    public Role() {

    }

    public Role(String role) {
        this.roleDescription = role;
    }

    public Role(String userId, String role) {
        this.userId = userId;
        this.roleDescription = role;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getRoleDescription() {
        return roleDescription;
    }

    public void setRoleDescription(String roleDescription) {
        this.roleDescription = roleDescription;
    }

    @Override
    public String toString() {
        return "Role{" +
                "userId='" + userId + '\'' +
                ", role='" + roleDescription + '\'' +
                '}';
    }
}
