package com.leemccormick.posdemo.service.user;

import com.leemccormick.posdemo.dao.UserRepository;
import com.leemccormick.posdemo.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository theUserRepository) {
        userRepository = theUserRepository;
    }

    // READ
    @Override
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User findById(String theId) {
        Optional<User> result = userRepository.findById(theId);

        User theUser = null;

        if (result.isPresent()) {
            theUser = result.get();
        } else {
            // we didn't find the user
            throw new RuntimeException("Did not find user with id - " + theId);
        }

        return theUser;
    }

    @Override
    public String findUserFullName(String theId) {

        User theUser = findById(theId);
        return theUser.getFirstName() + " " + theUser.getLastName();
    }

    @Override
    public String findRoles(String authenticationRoles) {
        String roles = "";
        if (hasCustomerRole(authenticationRoles)) {
            roles += "Customer";
        }

        if (hasSaleRole(authenticationRoles)) {
            if (roles.isEmpty()) {
                roles = "Sale";
            } else {
                roles += ", Sale";
            }

        }

        if (hasAdminRole(authenticationRoles)) {
            if (roles.isEmpty()) {
                roles = "Admin";
            } else {
                roles += ", Admin";
            }
        }
        return roles;
    }

    @Override
    public boolean hasCustomerRole(String authenticationRoles) {
        return authenticationRoles.toLowerCase().contains("Customer".toLowerCase());
    }

    @Override
    public boolean hasSaleRole(String authenticationRoles) {
        return authenticationRoles.toLowerCase().contains("Sale".toLowerCase());
    }

    @Override
    public boolean hasAdminRole(String authenticationRoles) {
        return authenticationRoles.toLowerCase().contains("Admin".toLowerCase());
    }
}
