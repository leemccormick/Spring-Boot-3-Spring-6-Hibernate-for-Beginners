package com.leemccormick.posdemo.controllers;

import com.leemccormick.posdemo.entity.Order;
import com.leemccormick.posdemo.entity.OrderStatus;
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
import java.util.Objects;

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

    @GetMapping("/orders/updateOrderStatus")
    public String updateOrderStatus(@RequestParam("orderId") int theOrderId, Authentication authentication) {
        try {
            Order theOrder = orderService.findOrderById(theOrderId);
            String orderStatus = theOrder.getStatus();

            if (Objects.equals(orderStatus, OrderStatus.PROCESSING.getValue())) {
                theOrder.setStatus(OrderStatus.SHIPPED.getValue());
            } else if (Objects.equals(orderStatus, OrderStatus.SHIPPED.getValue())) {
                theOrder.setStatus(OrderStatus.DELIVERED.getValue());
            }

            Order updatedOder = orderService.updateOrder(theOrder, authentication.getName());

            log.info(String.format("ORDER LIST PAGE | SUCCESS UPDATE ORDER STATUS | UpdatedOder Id is  : %s --> ", updatedOder));
            return "redirect:/orders";
        } catch (Exception exception) {
            log.error(String.format("ORDER LIST PAGE : UPDATE ORDER STATUS : Error Exception is  : %s --> ", exception));
            throw exception;
        }
    }

    @GetMapping("/orders/cancelOrder")
    public String cancelOrder(@RequestParam("orderId") int theOrderId, Authentication authentication) {
        try {
            Order theOrder = orderService.findOrderById(theOrderId);

            if (userService.hasAdminRole(authentication.getAuthorities().toString())) {
                theOrder.setStatus(OrderStatus.CANCELLED.getValue());
                Order cancelledOder = orderService.updateOrder(theOrder, authentication.getName());
                log.info(String.format("ORDER LIST PAGE | SUCCESS CANCEL ORDER | Cancelled Order is  : %s --> ", cancelledOder));
            }

            return "redirect:/orders";
        } catch (Exception exception) {
            log.error(String.format("ORDER LIST PAGE : CANCEL ORDER : Error Exception is  : %s --> ", exception));
            throw exception;
        }
    }

    @GetMapping("/orders/orderDetails")
    public String showOrderDetails(@RequestParam("orderId") int theOrderId, Authentication authentication, Model theModel) {
        try {
            Order theOrder = orderService.findOrderById(theOrderId);
            String customerName = userService.findUserFullName(theOrder.getCustomerId());
            String errorMessage = "";
            boolean shouldShowError = false;

            if (theOrder.getTotalAmount() == 0) {
                shouldShowError = true;
                errorMessage = "Total amount must be grater than 0 to continue processing order. Please, try again.";
            }

            theModel.addAttribute("order", theOrder);
            theModel.addAttribute("customerName", customerName);
            theModel.addAttribute("shouldShowError", shouldShowError);
            theModel.addAttribute("alertErrorMessage", errorMessage);

            log.info(String.format("ORDER LIST PAGE --> GO TO SEE ORDER DETAIL | Customer Name is Order is : %s : | --> Current Order is : %s  ", customerName, theOrder));
            return "/review-order";
        } catch (Exception exception) {
            log.error(String.format("ORDER LIST PAGE : GO TO SEE ORDER DETAIL : Error Exception is  : %s --> ", exception));
            throw exception;
        }
    }

    @GetMapping("/orders/deleteOrder")
    public String deleteOrder(@RequestParam("orderId") int theOrderId, Authentication authentication) {
        try {
            Order theOrder = orderService.findOrderById(theOrderId);

            if (userService.hasAdminRole(authentication.getAuthorities().toString())) {
                orderService.deleteOrder(theOrder);
                log.info(String.format("ORDER LIST PAGE | SUCCESS DELETE ORDER | Deleted Order is  : %s --> ", theOrder));
            }
            
            return "redirect:/orders";
        } catch (Exception exception) {
            log.error(String.format("ORDER LIST PAGE : DELETE ORDER : Error Exception is  : %s --> ", exception));
            throw exception;
        }
    }
}
