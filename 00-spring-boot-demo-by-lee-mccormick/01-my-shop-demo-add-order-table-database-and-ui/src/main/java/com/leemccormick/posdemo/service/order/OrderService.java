package com.leemccormick.posdemo.service.order;

import com.leemccormick.posdemo.entity.Order;
import com.leemccormick.posdemo.entity.OrderItem;
import com.leemccormick.posdemo.entity.User;

import java.util.List;

public interface OrderService {
    // CREATE
//    Order addNewOrder(Order theOrder);
    Order addNewOrder(Order theOrder);
    void  addNewItemToTheOrder(int theOrderId, OrderItem theOrderItem);

    // READ
    List<Order> findAllOrders();

    Order findOrderById(int theId);
  //  Order findPendingOrderForTheCustomer(String customerId);

    Order findPendingOrderForTheCustomer(String customerId);

    // UPDATE
    Order updateOrder(Order theOrder);

    // DELETE
    void deleteOrder(Order theOrder);
}
