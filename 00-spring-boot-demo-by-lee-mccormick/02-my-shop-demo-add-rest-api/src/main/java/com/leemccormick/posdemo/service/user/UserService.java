package com.leemccormick.posdemo.service.user;

import com.leemccormick.posdemo.entity.User;
import com.leemccormick.posdemo.entity.UserDetail;

import java.util.List;

public interface UserService {
    // READ
    List<User> findAllUsers();

    List<UserDetail> findAllUsersWithDetails();

    int getTotalNumberOfUsers();

    int getTotalNumberOfCustomerRoles();

    int getTotalNumberOfSaleRoles();

    int getTotalNumberOfAdminRoles();

    User findById(String theId);

    UserDetail findUserDetailById(String theUserId);

    String findUserFullName(String theId);

    String findRoles(String authenticationRoles);

    boolean hasCustomerRole(String authenticationRoles);

    boolean hasSaleRole(String authenticationRoles);

    boolean hasAdminRole(String authenticationRoles);
}
