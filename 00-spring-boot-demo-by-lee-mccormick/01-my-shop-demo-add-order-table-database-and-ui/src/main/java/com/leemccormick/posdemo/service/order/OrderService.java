package com.leemccormick.posdemo.service.order;

import com.leemccormick.posdemo.entity.Order;
import com.leemccormick.posdemo.entity.OrderItem;
import com.leemccormick.posdemo.entity.Product;
import com.leemccormick.posdemo.entity.User;

import java.util.List;

public interface OrderService {
    // CREATE
    Order addNewOrder(Order theOrder, String userId);

    Order addNewItemToTheOrder(int theOrderId, OrderItem theOrderItem, String userId);

    Order addNewItemToTheOrder(int theOrderId, Product theProduce, String userId);

    // READ
    List<Order> findAllOrders();

    Order findOrderById(int theId);
    //  Order findPendingOrderForTheCustomer(String customerId);

    Order findPendingOrderForTheCustomer(String customerId);

    // UPDATE
    Order updateOrder(Order theOrder, String userId);

    OrderItem updateOrderItem(OrderItem theOrderItem);

    Order updateOrderWithItem(Order theOrder, OrderItem theOrderItem, String userId);

    // DELETE
    void deleteOrder(Order theOrder);

    void deleteOrderItem(OrderItem theOrderOrderItem);
}
