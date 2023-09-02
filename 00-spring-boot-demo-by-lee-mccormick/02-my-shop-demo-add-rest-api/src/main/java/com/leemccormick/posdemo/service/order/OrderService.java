package com.leemccormick.posdemo.service.order;

import com.leemccormick.posdemo.entity.*;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface OrderService {
    // CREATE
    Order addNewOrder(Order theOrder, String userId);

    Order addNewItemToTheOrder(int theOrderId, OrderItem theOrderItem, String userId);

    Order addNewItemToTheOrder(int theOrderId, Product theProduce, String userId);
    Order addNewItemToCart(int theOrderId, Product theProduce, String userId);

    // READ
    List<Order> findAllOrders();

    List<Order> findAllOrdersByOrderStatus(String orderStatus);

    List<Order> findOrdersForCustomer(String customerId);

    List<Order> findOrdersByOrderStatusForTheCustomer(String orderStatus, String customerId);

    Order findOrderById(int theId);

    OrderItem findOrderItemById(int theOrderItemId);

    Order findPendingOrderForTheCustomer(String customerId);

    double findTotalSales(List<Order> theListOfOrder);

    ErrorResponse validateOrderBeforeProcessing(Order theOrder);

    SaleInfo findSaleInfo();

    // UPDATE
    Order updateOrder(Order theOrder, String userId);

    OrderItem updateOrderItem(OrderItem theOrderItem);

    Order updateOrderWithItem(Order theOrder, OrderItem theOrderItem, String userId);

    Order addItemToOrderByIds(int theOrderId, int theOrderItemId);

    Order subtractItemToOrderByIds(int theOrderId, int theOrderItemId);
    Order updateItemQuantityInTheCart(int theOrderId, int theOrderItemId, int theQuantity, String userId);

    Order checkOut(Order theOrder);

    // DELETE
    void deleteOrder(Order theOrder);

    void deleteOrderItem(OrderItem theOrderOrderItem);
    Order deleteItemInTheCart(int theOrderItemId, int theOrderId,  Authentication authentication);
}
