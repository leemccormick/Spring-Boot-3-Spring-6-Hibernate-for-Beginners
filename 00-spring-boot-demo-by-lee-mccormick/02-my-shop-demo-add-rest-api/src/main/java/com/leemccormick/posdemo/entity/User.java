package com.leemccormick.posdemo.entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue
    @Column(name = "user_id")
    private String id;

    @Column(name = "username")
    private String username;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "email")
    private String email;

    @Column(name = "address")
    private String address;

    @Column(name = "phone_number")
    private String phoneNumber;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private List<Role> roles;

    public User() {

    }

    public User(String id, String username, String firstName, String lastName, String email, String address, String phoneNumber) {
        this.id = id;
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.address = address;
        this.phoneNumber = phoneNumber;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public String getRolesDescription() {
        String rolesDescription = "";
        if (hasCustomerRole()) {
            rolesDescription += "Customer";
        }

        if (hasSaleRole()) {
            if (rolesDescription.isEmpty()) {
                rolesDescription = "Sale";
            } else {
                rolesDescription += ", Sale";
            }

        }

        if (hasAdminRole()) {
            if (rolesDescription.isEmpty()) {
                rolesDescription = "Admin";
            } else {
                rolesDescription += ", Admin";
            }
        }
        return rolesDescription;
    }

    public boolean hasCustomerRole() {
        boolean hasCustomerRole = false;
        for (Role theRole : this.roles) {
            if (theRole.getRoleDescription().toLowerCase().contains("Customer".toLowerCase())) {
                hasCustomerRole = true;
                break;
            }
        }
        return hasCustomerRole;
    }

    public boolean hasSaleRole() {
        boolean hasSaleRole = false;
        for (Role theRole : this.roles) {
            if (theRole.getRoleDescription().toLowerCase().contains("Sale".toLowerCase())) {
                hasSaleRole = true;
                break;
            }
        }
        return hasSaleRole;
    }

    public boolean hasAdminRole() {
        boolean hasAdminRoles = false;
        for (Role theRole : this.roles) {
            if (theRole.getRoleDescription().toLowerCase().contains("Admin".toLowerCase())) {
                hasAdminRoles = true;
                break;
            }
        }
        return hasAdminRoles;
    }

    public void addRole(Role theRole) {
        if (roles == null) {
            roles = new ArrayList<>();
        }
        roles.add(theRole);
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", username='" + username + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", address='" + address + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", roles=" + roles +
                '}';
    }
}
