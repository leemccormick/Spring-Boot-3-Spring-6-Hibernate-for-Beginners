package com.leemccormick.posdemo.dao.Order;

import com.leemccormick.posdemo.entity.Order;
import com.leemccormick.posdemo.entity.OrderItem;

import java.util.List;

public interface OrderDAO {
    // CREATE
    void saveNewOrder(Order theOrder);

    void saveItemToOrder(int theOrderId, OrderItem theOrderItem);

    Order saveItemToTheOrder(int theOrderId, OrderItem theOrderItem);

    // READ
    List<Order> findOrdersForTheCustomer(String theCustomerId);

    List<Order> findOrdersForTheCustomerByOrderStatus(String theCustomerId, String theStatus);

    Order findTheOrderById(int theOrderId);

    // UPDATE
    void updateOrder(Order theOrder);

    void updateOrderItem(OrderItem theOrderItem);

    // DELETE
    void deleteOrderItem(OrderItem theOrderOrderItem);
}
