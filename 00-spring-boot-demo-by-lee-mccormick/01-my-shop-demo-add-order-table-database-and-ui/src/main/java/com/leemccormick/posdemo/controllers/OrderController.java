package com.leemccormick.posdemo.controllers;

import com.leemccormick.posdemo.entity.Order;
import com.leemccormick.posdemo.entity.Product;
import com.leemccormick.posdemo.service.order.OrderService;
import com.leemccormick.posdemo.service.product.ProductService;
import com.leemccormick.posdemo.service.user.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
@Slf4j
public class OrderController {
    private ProductService productService;
    private UserService userService;
    private OrderService orderService;
    private List<Order> listOfOrder = new ArrayList<>();

    public OrderController(ProductService theProductService,
                           UserService theUserService,
                           OrderService theOrderService
    ) {
        productService = theProductService;
        userService = theUserService;
        orderService = theOrderService;
    }

    @GetMapping("/orders")
    public String showOrders(Authentication authentication, Model theModel) {
        try {
            String currentUserId = authentication.getName();
            String authenticationRoles = authentication.getAuthorities().toString();
            String username = userService.findUserFullName(currentUserId);
            boolean hasCustomerRole = userService.hasCustomerRole(authenticationRoles);
            boolean hasSaleRole = userService.hasSaleRole(authenticationRoles);
            boolean hasAdminRole = userService.hasAdminRole(authenticationRoles);
            String roles = userService.findRoles(authenticationRoles);
            String totalSalesDescription = "Total Sales".toUpperCase();
            if (hasAdminRole || hasSaleRole) {
                listOfOrder = orderService.findAllOrders();
            } else if (hasCustomerRole) {
                listOfOrder = orderService.findOrdersForCustomer(currentUserId);
                totalSalesDescription = "Total Spending".toUpperCase();
            }

            int totalOrders = listOfOrder.size();
            double totalSales = orderService.findTotalSales(listOfOrder);

            theModel.addAttribute("orders", listOfOrder);
            theModel.addAttribute("totalOrders", totalOrders);
            theModel.addAttribute("username", username);
            theModel.addAttribute("roles", roles);
            theModel.addAttribute("hasAdminRole", hasAdminRole);
            theModel.addAttribute("hasSaleRole", hasSaleRole);
            theModel.addAttribute("hasCustomerRole", hasCustomerRole);
            theModel.addAttribute("totalSalesDescription", totalSalesDescription);
            theModel.addAttribute("totalSales", totalSales);

            log.info(String.format("ORDER LIST PAGE | username is  : %s -->  | roles is  : %s --> | listOfOrder size is  : %s -->  | listOfOrder is  : %s --> ", username, roles, listOfOrder.size(), listOfOrder));
            return "/order-list";
        } catch (Exception exception) {
            log.error(String.format("ORDER LIST PAGE : Error Exception is  : %s --> ", exception));
            throw exception;
        }
    }
}
