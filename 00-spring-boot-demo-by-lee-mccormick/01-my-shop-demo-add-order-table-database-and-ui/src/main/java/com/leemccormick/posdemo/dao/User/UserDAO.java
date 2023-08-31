package com.leemccormick.posdemo.dao.User;

import com.leemccormick.posdemo.entity.Role;

import java.util.List;

public interface UserDAO {
    List<Role> findRolesByUserId(String theUserId);
    List<Role> findCustomerRoles();
    List<Role> findSaleRoles();
    List<Role> findAdminRoles();
}
