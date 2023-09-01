package com.leemccormick.posdemo.service.user;

import com.leemccormick.posdemo.dao.User.UserDAO;
import com.leemccormick.posdemo.dao.User.UserRepository;
import com.leemccormick.posdemo.entity.Role;
import com.leemccormick.posdemo.entity.User;
import com.leemccormick.posdemo.entity.UserDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;
    private UserDAO userDAO;

    @Autowired
    public UserServiceImpl(UserRepository theUserRepository,
                           UserDAO theUserDAO
    ) {
        userRepository = theUserRepository;
        userDAO = theUserDAO;
    }

    // READ
    @Override
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public List<UserDetail> findAllUsersWithDetails() {
        List<User> allUsers = findAllUsers();
        List<UserDetail> nReturnListOfUserDetail = new ArrayList<>();

        for (User theUser : allUsers) {
            List<Role> roles = userDAO.findRolesByUserId(theUser.getId());
            theUser.setRoles(roles);
            UserDetail newUserWithDetail = new UserDetail(theUser);
            nReturnListOfUserDetail.add(newUserWithDetail);
        }

        return nReturnListOfUserDetail;
    }

    @Override
    public int getTotalNumberOfUsers() {
        return findAllUsers().size();
    }

    @Override
    public int getTotalNumberOfCustomerRoles() {
        return userDAO.findCustomerRoles().size();
    }

    @Override
    public int getTotalNumberOfSaleRoles() {
        return userDAO.findSaleRoles().size();
    }

    @Override
    public int getTotalNumberOfAdminRoles() {
        return userDAO.findAdminRoles().size();
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
    public UserDetail findUserDetailById(String theUserId) {
        List<UserDetail> allUserDetails = findAllUsersWithDetails();
        UserDetail nReturnUserDetail = null;
        for (UserDetail theUserDetail : allUserDetails) {
            if (theUserDetail.getUser().getId().equals(theUserId)) {
                nReturnUserDetail = theUserDetail;
            }
        }
        return nReturnUserDetail;
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

    @Override
    public boolean hasCustomerRole(Authentication authentication) {
        return hasCustomerRole(authentication.getAuthorities().toString());
    }

    @Override
    public boolean hasSaleRole(Authentication authentication) {
        return hasSaleRole(authentication.getAuthorities().toString());

    }

    @Override
    public boolean hasAdminRole(Authentication authentication) {
        return hasAdminRole(authentication.getAuthorities().toString());
    }
}
