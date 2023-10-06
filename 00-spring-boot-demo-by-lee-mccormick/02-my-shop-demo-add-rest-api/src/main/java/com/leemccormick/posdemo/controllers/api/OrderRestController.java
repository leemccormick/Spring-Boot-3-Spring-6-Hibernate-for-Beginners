package com.leemccormick.posdemo.controllers.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.leemccormick.posdemo.aspect.ApiErrorException;
import com.leemccormick.posdemo.entity.*;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/mystoredemo/orders")
@Slf4j
public class OrderRestController {
    private OrderService orderService;
    private ProductService productService;
    private UserService userService;
    private ObjectMapper objectMapper;

    @Autowired
    public OrderRestController(OrderService theOrderService,
                               ProductService theProductService,
                               UserService theUserService,
                               ObjectMapper theObjectMapper
    ) {
        orderService = theOrderService;
        productService = theProductService;
        userService = theUserService;
        objectMapper = theObjectMapper;
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

    @GetMapping(value = "/pendingOrder", produces = "application/json") // Look for last pending order for customer to display total items in the cart
    public ResponseEntity<String> findPendingOrder(Authentication authentication) {
        try {
            Order currentOrder = orderService.findPendingOrderForTheCustomer(authentication.getName());
            Map<String, Object> data = new HashMap<>();

            if (currentOrder != null) {
                data.put("pendingOrderId", currentOrder.getId());
                data.put("customerId", currentOrder.getCustomerId());
                if (!currentOrder.getItems().isEmpty()) {
                    int itemsInOrder = 0;
                    for (OrderItem item : currentOrder.getItems()) {
                        itemsInOrder += item.getQuantity();
                    }
                    data.put("totalItems", itemsInOrder);
                    data.put("showCheckOutAction", true);
                } else {
                    data.put("totalItems", 0);
                    data.put("showCheckOutAction", false);
                }
            } else {
                data.put("totalItems", 0);
                data.put("showCheckOutAction", false);
            }
            log.info(String.format("ORDER API /pendingOrder : AFTER SUCCESSFULLY FIND PENDING ORDER : data  is : %s --> ", data));
            return ResponseEntity.ok(objectMapper.writeValueAsString(data));
        } catch (ApiErrorException exception) {
            throw new ApiErrorException(exception.getMessage(), exception.getHttpStatus());
        } catch (Exception exception) {
            throw new ApiErrorException(exception.getMessage());
        }
    }

    @PostMapping("/addItemToCart") // Only customer can add item to cart
    public ResponseEntity<Order> addItemToCart(@RequestParam(name = "productId", required = true) int theProductId,
                                               Authentication authentication
    ) {
        try {
            Order currentOrder = orderService.findPendingOrderForTheCustomer(authentication.getName());
            Product theProduct = productService.findById(theProductId);

            if (theProduct.getQuantity() > 0) {
                if (currentOrder != null) {
                    if (currentOrder.getCustomerId().equals(authentication.getName())) {
                        if (currentOrder.getItems().isEmpty()) {
                            currentOrder = orderService.addNewItemToCart(currentOrder.getId(), theProduct, authentication.getName());
                            log.info(String.format("ORDER API /addItemToCart : AFTER SUCCESSFULLY ADD TO CART : Current Order  is : %s --> ", currentOrder));
                            return ResponseEntity.ok(currentOrder);
                        } else {
                            boolean isTheNewItem = true;

                            OrderItem orderItemToUpdate = new OrderItem();

                            for (OrderItem item : currentOrder.getItems()) {
                                if (item.getProductId() == theProduct.getId()) {
                                    isTheNewItem = false;
                                    orderItemToUpdate = item;
                                    break;
                                }
                            }

                            if (isTheNewItem) {
                                currentOrder = orderService.addNewItemToCart(currentOrder.getId(), theProduct, authentication.getName());
                                log.info(String.format("ORDER API /addItemToCart : AFTER SUCCESSFULLY ADD TO CART : Current Order  is : %s --> ", currentOrder));
                                return ResponseEntity.ok(currentOrder);
                            } else {
                                int newQuantity = orderItemToUpdate.getQuantity() + 1;
                                double newSubtotal = newQuantity * theProduct.getPrice();
                                orderItemToUpdate.setQuantity(newQuantity);
                                orderItemToUpdate.setSubtotal(newSubtotal);

                                if (theProduct.getQuantity() >= newQuantity) {
                                    currentOrder = orderService.updateOrderWithItem(currentOrder, orderItemToUpdate, authentication.getName());
                                    log.info(String.format("ORDER API /addItemToCart : AFTER SUCCESSFULLY ADD TO CART : Current Order  is : %s --> ", currentOrder));
                                    return ResponseEntity.ok(currentOrder);
                                } else {
                                    String errorMessage = "Out of stock, item's quantity in the stock is less than your order.";
                                    errorMessage += "\nThe product name : " + theProduct.getName() + ". Quantity : " + theProduct.getQuantity();
                                    log.error(String.format("ORDER API /addItemToCart  : ERROR ADD TO CART : %s | Current Order  is : %s --> ", errorMessage, currentOrder));
                                    throw new ApiErrorException(errorMessage, HttpStatus.BAD_REQUEST);
                                }
                            }
                        }
                    } else { // Only customer can modify or add item to their order
                        String errorMessage = "Only the customer who started this order can modify or add item to their order";
                        log.error(String.format("ORDER API /addItemToCart  : ERROR ADD TO CART : %s | Current Order  is : %s --> ", errorMessage, currentOrder));
                        throw new ApiErrorException(errorMessage, HttpStatus.METHOD_NOT_ALLOWED);
                    }
                } else { // Create new Order and add the first item, if current order is null
                    Order savedOrder = orderService.addNewOrder(new Order(), authentication.getName());
                    currentOrder = orderService.addNewItemToCart(savedOrder.getId(), theProduct, authentication.getName());
                    log.info(String.format("ORDER API /addItemToCart : AFTER SUCCESSFULLY ADD TO CART : Current Order  is : %s --> ", currentOrder));
                    return ResponseEntity.ok(currentOrder);
                }
            } else {
                String errorMessage = "Out of stock, item's quantity in the stock is out.";
                errorMessage += "\nThe product name : " + theProduct.getName() + ". Quantity : " + theProduct.getQuantity();
                log.error(String.format("ORDER API /addItemToCart  : ERROR ADD TO CART : %s | Current Order  is : %s --> ", errorMessage, currentOrder));
                throw new ApiErrorException(errorMessage, HttpStatus.BAD_REQUEST);
            }
        } catch (ApiErrorException exception) {
            throw new ApiErrorException(exception.getMessage(), exception.getHttpStatus());
        } catch (Exception exception) {
            throw new ApiErrorException(exception.getMessage());
        }
    }

    @PostMapping("/updateItemQuantityInTheCart") // Only customer can update their cart
    public ResponseEntity<Order> updateItemQuantityInTheCart(@RequestParam(name = "orderId", required = true) int theOrderId,
                                                             @RequestParam(name = "orderItemId", required = true) int theOrderItemId,
                                                             @RequestParam(name = "quantity", required = true) int theQuantity,
                                                             Authentication authentication) {
        try {
            Order updatedOrder = orderService.updateItemQuantityInTheCart(theOrderId, theOrderItemId, theQuantity, authentication.getName());
            return ResponseEntity.ok(updatedOrder);
        } catch (ApiErrorException exception) {
            throw new ApiErrorException(exception.getMessage(), exception.getHttpStatus());
        } catch (Exception exception) {
            throw new ApiErrorException(exception.getMessage());
        }
    }

    @DeleteMapping("/deleteItemInTheCart") // Only customer can delete item in their cart
    public ResponseEntity<ApiResponse> deleteItemInTheCart(@RequestParam(name = "orderId", required = true) int theOrderId,
                                                           @RequestParam(name = "orderItemId", required = true) int theOrderItemId,
                                                           Authentication authentication) {
        try {
            Order deletedOrder = orderService.deleteItemInTheCart(theOrderItemId, theOrderId, authentication);
            String message = "Successfully delete the item on this order." + deletedOrder.toString();
            ApiResponse response = new ApiResponse(false, message);
            return ResponseEntity.ok(response);
        } catch (Exception exception) {
            throw new ApiErrorException(exception.getMessage());
        }
    }

    @PostMapping("/checkout")
    public ResponseEntity<Order> checkoutOrder(@RequestBody Order theOrder, Authentication authentication) {
        try {
            Order checkoutOrdered = orderService.validateAndCheckOut(theOrder, authentication);
            return ResponseEntity.ok(checkoutOrdered);
        } catch (ApiErrorException exception) {
            throw new ApiErrorException(exception.getMessage(), exception.getHttpStatus());
        } catch (Exception exception) {
            throw new ApiErrorException(exception.getMessage());
        }
    }

    @PutMapping("/updateOrderStatusToProcessing")
    public ResponseEntity<ApiResponse> updateOrderStatusToProcessing(@RequestParam("orderId") int theOrderId, Authentication authentication) {
        try {
            Order updatedOrder = orderService.updateOrderStatus(theOrderId, authentication, OrderStatus.PROCESSING);
            ApiResponse response = new ApiResponse(false, "Successfully updated order status to " + updatedOrder.getStatus());
            return ResponseEntity.ok(response);
        } catch (ApiErrorException exception) {
            throw new ApiErrorException(exception.getMessage(), exception.getHttpStatus());
        } catch (Exception exception) {
            throw new ApiErrorException(exception.getMessage());
        }
    }

    @PutMapping("/updateOrderStatusToShipped")
    public ResponseEntity<ApiResponse> updateOrderStatusToShipped(@RequestParam("orderId") int theOrderId, Authentication authentication) {
        try {
            Order updatedOrder = orderService.updateOrderStatus(theOrderId, authentication, OrderStatus.SHIPPED);
            ApiResponse response = new ApiResponse(false, "Successfully updated order status to " + updatedOrder.getStatus());
            return ResponseEntity.ok(response);
        } catch (ApiErrorException exception) {
            throw new ApiErrorException(exception.getMessage(), exception.getHttpStatus());
        } catch (Exception exception) {
            throw new ApiErrorException(exception.getMessage());
        }
    }

    @PutMapping("/updateOrderStatusToDelivered")
    public ResponseEntity<ApiResponse> updateOrderStatusToDelivered(@RequestParam("orderId") int theOrderId, Authentication authentication) {
        try {
            Order updatedOrder = orderService.updateOrderStatus(theOrderId, authentication, OrderStatus.DELIVERED);
            ApiResponse response = new ApiResponse(false, "Successfully updated order status to " + updatedOrder.getStatus());
            return ResponseEntity.ok(response);
        } catch (ApiErrorException exception) {
            throw new ApiErrorException(exception.getMessage(), exception.getHttpStatus());
        } catch (Exception exception) {
            throw new ApiErrorException(exception.getMessage());
        }
    }

    @PutMapping("/updateOrderStatusToCancelled")
    public ResponseEntity<ApiResponse> updateOrderStatusToCancelled(@RequestParam("orderId") int theOrderId, Authentication authentication) {
        try {
            Order updatedOrder = orderService.updateOrderStatus(theOrderId, authentication, OrderStatus.CANCELLED);
            ApiResponse response = new ApiResponse(false, "Successfully updated order status from to " + updatedOrder.getStatus());
            return ResponseEntity.ok(response);
        } catch (ApiErrorException exception) {
            throw new ApiErrorException(exception.getMessage(), exception.getHttpStatus());
        } catch (Exception exception) {
            throw new ApiErrorException(exception.getMessage());
        }
    }

    @DeleteMapping("/deleteOrder")
    public ResponseEntity<ApiResponse> deleteOrder(@RequestParam("orderId") int theOrderId, Authentication authentication) {
        try {
            orderService.deleteOrder(theOrderId, authentication);
            ApiResponse response = new ApiResponse(false, "Successfully deleted order with id : " + theOrderId);
            return ResponseEntity.ok(response);
        } catch (ApiErrorException exception) {
            throw new ApiErrorException(exception.getMessage(), exception.getHttpStatus());
        } catch (Exception exception) {
            throw new ApiErrorException(exception.getMessage());
        }
    }
}