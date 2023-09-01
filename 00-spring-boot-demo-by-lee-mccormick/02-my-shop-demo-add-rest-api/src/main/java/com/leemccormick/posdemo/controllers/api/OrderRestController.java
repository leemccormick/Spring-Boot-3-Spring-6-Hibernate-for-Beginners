package com.leemccormick.posdemo.controllers.api;

import com.leemccormick.posdemo.aspect.ApiErrorException;
import com.leemccormick.posdemo.entity.Order;
import com.leemccormick.posdemo.service.order.OrderService;
import com.leemccormick.posdemo.service.product.ProductService;
import com.leemccormick.posdemo.service.user.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/mystoredemo/orders")
@Slf4j
public class OrderRestController {
    private OrderService orderService;
    private ProductService productService;
    private UserService userService;

    @Autowired
    public OrderRestController(OrderService theOrderService,
                               ProductService theProductService,
                               UserService theUserService
    ) {
        orderService = theOrderService;
        productService = theProductService;
        userService = theUserService;
    }

    @GetMapping("/all") // Get all orders for SALE and ADMIN --> If added param will return order based on status
    public ResponseEntity<List<Order>> findAllOrders(Authentication authentication,
                                                     @RequestParam(name = "status", required = false) String orderStatus
    ) {
        try {
            if (userService.hasAdminRole(authentication) || userService.hasSaleRole(authentication)) {
                if (orderStatus != null) {
                    List<Order> orders = orderService.findAllOrdersByOrderStatus(orderStatus);
                    return ResponseEntity.ok(orders);
                } else {
                    List<Order> orders = orderService.findAllOrders();
                    return ResponseEntity.ok(orders);
                }
            } else {
                throw new ApiErrorException("Customer is not allow to see all orders.", HttpStatus.METHOD_NOT_ALLOWED);
            }
        } catch (ApiErrorException exception) {
            throw new ApiErrorException(exception.getMessage(), exception.getHttpStatus());
        } catch (Exception exception) {
            throw new ApiErrorException(exception.getMessage());
        }
    }

    @GetMapping("/myOrders")
    // Get user orders history for current user --> If added param will return order based on status
    public ResponseEntity<List<Order>> findMyOrders(Authentication authentication,
                                                    @RequestParam(name = "status", required = false) String orderStatus
    ) {
        try {
            if (userService.hasCustomerRole(authentication)) {
                List<Order> myOrders = new ArrayList<>();
                if (orderStatus != null) {
                    myOrders = orderService.findOrdersByOrderStatusForTheCustomer(orderStatus, authentication.getName());
                    if (myOrders.isEmpty()) {
                        throw new ApiErrorException("There is no " + orderStatus + " order history for your account.", HttpStatus.NOT_FOUND);
                    } else {
                        return ResponseEntity.ok(myOrders);
                    }
                } else {
                    myOrders = orderService.findOrdersForCustomer(authentication.getName());
                    if (myOrders.isEmpty()) {
                        throw new ApiErrorException("There is no order history for your account.", HttpStatus.NOT_FOUND);
                    } else {
                        return ResponseEntity.ok(myOrders);
                    }
                }
            } else {
                throw new ApiErrorException("Only user who has customer role is allowed to see their orders.", HttpStatus.METHOD_NOT_ALLOWED);
            }
        } catch (ApiErrorException exception) {
            throw new ApiErrorException(exception.getMessage(), exception.getHttpStatus());
        } catch (Exception exception) {
            throw new ApiErrorException(exception.getMessage());
        }
    }

    @GetMapping("/userOrders")
    // Get user orders history by user id for SALE and ADMIN --> If added param will return order based on status
    public ResponseEntity<List<Order>> findUserOrdersByUserId(@RequestParam(name = "userId", required = true) String userId,
                                                              @RequestParam(name = "status", required = false) String orderStatus,
                                                              Authentication authentication
    ) {
        try {
            if (userService.hasAdminRole(authentication) ||
                    userService.hasSaleRole(authentication) ||
                    (userService.hasCustomerRole(authentication) && authentication.getName().equals(userId))
            ) {
                List<Order> orders = new ArrayList<>();
                if (orderStatus != null) {
                    orders = orderService.findOrdersByOrderStatusForTheCustomer(orderStatus, userId);
                } else {
                    orders = orderService.findOrdersForCustomer(userId);
                }

                if (orders.isEmpty()) {
                    String userFullName = userService.findUserFullName(userId);
                    String errorMessage = "Error : ";
                    if (orderStatus != null) {
                        errorMessage += "There is no " + orderStatus + " order history for this user : " + userFullName + " | user id : " + userId;
                    } else {
                        errorMessage += "There is no order history for this user : " + userFullName + " | user id : " + userId;
                    }
                    throw new ApiErrorException(errorMessage, HttpStatus.NOT_FOUND);
                } else {
                    return ResponseEntity.ok(orders);
                }
            } else {
                throw new ApiErrorException("Only sales and admin are allowed to see user's orders.", HttpStatus.METHOD_NOT_ALLOWED);
            }
        } catch (ApiErrorException exception) {
            throw new ApiErrorException(exception.getMessage(), exception.getHttpStatus());
        } catch (Exception exception) {
            throw new ApiErrorException(exception.getMessage());
        }
    }

    @GetMapping("/details/{orderId}") // Get order by id using @PathVariable
    public ResponseEntity<Order> findOrder(Authentication authentication,
                                           @PathVariable String orderId
    ) {
        try {
            Order theOrder = orderService.findOrderById(Integer.parseInt(orderId));
            if (userService.hasAdminRole(authentication) || userService.hasSaleRole(authentication)) {
                return ResponseEntity.ok(theOrder);
            } else {
                if (theOrder.getCustomer().getId().equals(authentication.getName())) {
                    return ResponseEntity.ok(theOrder);
                } else {
                    throw new ApiErrorException("User is not allowed to see this order details.", HttpStatus.METHOD_NOT_ALLOWED);
                }
            }
        } catch (ApiErrorException exception) {
            throw new ApiErrorException(exception.getMessage(), exception.getHttpStatus());
        } catch (Exception exception) {
            throw new ApiErrorException(exception.getMessage());
        }
    }
}