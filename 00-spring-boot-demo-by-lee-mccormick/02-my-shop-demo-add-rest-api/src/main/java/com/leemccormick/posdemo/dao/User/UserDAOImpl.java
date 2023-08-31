package com.leemccormick.posdemo.dao.User;

import com.leemccormick.posdemo.entity.Order;
import com.leemccormick.posdemo.entity.Role;
import com.leemccormick.posdemo.entity.RoleType;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
@Slf4j
public class UserDAOImpl implements UserDAO{
    private EntityManager entityManager;

    @Autowired
    public UserDAOImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<Role> findRolesByUserId(String theUserId) {
        List<Role> nReturnUserRoles = new ArrayList<>();
        List<Role> customerRoles = findCustomerRoles();
        List<Role> saleRoles = findSaleRoles();
        List<Role> adminRoles = findAdminRoles();

        for (Role theCustomerRole : customerRoles) {
            if (theCustomerRole.getUserId().equals(theUserId)) {
                Role newCustomerRole = new Role(theUserId, RoleType.CUSTOMER.getValue());
                nReturnUserRoles.add(newCustomerRole);
            }
        }

        for (Role theSaleRole : saleRoles) {
            if (theSaleRole.getUserId().equals(theUserId)) {
                Role newSaleRole = new Role(theUserId, RoleType.SALE.getValue());
                nReturnUserRoles.add(newSaleRole);
            }
        }

        for (Role theAdminRole : adminRoles) {
            if (theAdminRole.getUserId().equals(theUserId)) {
                Role newAdminRole = new Role(theUserId, RoleType.ADMIN.getValue());
                nReturnUserRoles.add(newAdminRole);
            }
        }

        System.out.println("findRolesByUserId customerRoles : " + customerRoles);

        System.out.println("findRolesByUserId saleRoles: " + saleRoles);

        System.out.println("findRolesByUserId adminRoles: " + adminRoles);


        System.out.println("findRolesByUserId findCustomerRoles TEST : " + nReturnUserRoles);

        return nReturnUserRoles;
    }

    @Override
    public List<Role> findCustomerRoles() {
        TypedQuery<Role> query = entityManager.createQuery(
                "SELECT r FROM Role r " +
                        "WHERE r.roleDescription = :role",
                Role.class
        );

        // 2) Set Parameter
        query.setParameter("role", RoleType.CUSTOMER.getValue());


        // 3) Execute the query
        return query.getResultList();
    }

    @Override
    public List<Role> findSaleRoles() {
        TypedQuery<Role> query = entityManager.createQuery(
                "SELECT r FROM Role r " +
                        "WHERE r.roleDescription = :role",
                Role.class
        );

        // 2) Set Parameter
        query.setParameter("role",  RoleType.SALE.getValue());


        // 3) Execute the query
        return query.getResultList();
    }

    @Override
    public List<Role> findAdminRoles() {
        TypedQuery<Role> query = entityManager.createQuery(
                "SELECT r FROM Role r " +
                        "WHERE r.roleDescription = :role",
                Role.class
        );

        // 2) Set Parameter
        query.setParameter("role", RoleType.ADMIN.getValue());


        // 3) Execute the query
        return query.getResultList();
    }
}
