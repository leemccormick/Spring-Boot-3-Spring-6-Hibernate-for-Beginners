package com.leemccormick.posdemo.service.order;

import com.leemccormick.posdemo.dao.Order.OrderRepository;
import com.leemccormick.posdemo.dao.Order.OrderDAO;
import com.leemccormick.posdemo.entity.Order;
import com.leemccormick.posdemo.entity.OrderItem;
import com.leemccormick.posdemo.entity.OrderStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {
//    private OrderService orderService;
    private OrderRepository orderRepository;
    private OrderDAO orderDAO;
//    @Autowired
//    public OrderServiceImpl(OrderRepository theOrderRepository) {
//        orderRepository = theOrderRepository;
//    }

    @Autowired
    public OrderServiceImpl(OrderRepository theOrderRepository,
                            OrderDAO theOrderDAO) {
        orderRepository = theOrderRepository;
        orderDAO = theOrderDAO;

    }
    // CREATE
//    @Override
//    public Order addNewOrder(Order theOrder) {
//        orderRepository.save(theOrder);
//        return theOrder;
//    }

    @Override
    public Order addNewOrder(Order theOrder) {
        orderDAO.saveNewOrder(theOrder);
        System.out.println("SAVE NEW ORDER addNewOrderTest" + theOrder);
        return theOrder;
    }

    @Override
    public void addNewItemToTheOrder(int theOrderId, OrderItem theOrderItem) {
        orderDAO.saveItemToOrder(theOrderId, theOrderItem);
    }

    // READ
    @Override
    public List<Order> findAllOrders() {
        return null;
    }

    // UPDATE
    @Override
    public Order findOrderById(int theId) {
        return null;
    }

    @Override
    public Order findPendingOrderForTheCustomer(String customerId) {
        List<Order> pendingOrdersByTheCustomer = orderDAO.findOrdersForTheCustomerByOrderStatus(customerId, OrderStatus.PENDING.getValue());//orderDAO.findOrdersForTheCustomer(customerId);

        if (pendingOrdersByTheCustomer.isEmpty()) {
            System.out.println("findPendingOrderForTheCustomer :  at line 65 Creating new order orders");

          //  Order newPendingOrder = new Order();

            return null;
        } else {

            Order firstPendingOrder = null;

            for (Order order : pendingOrdersByTheCustomer) {
                System.out.println("order : " + order);
                System.out.println("order getStatus : " + order.getStatus());
                System.out.println("OrderStatus.PENDING.getValue() : " + OrderStatus.PENDING.getValue());


//                System.out.println("Oorder.getStatus() == OrderStatus.PENDING.getValue() : " + order.getStatus() == OrderStatus.PENDING.getValue());

                if (order.getStatus().equalsIgnoreCase(OrderStatus.PENDING.getValue())) {
             //   if (order.getStatus().trim().toLowerCase() == OrderStatus.PENDING.getValue().trim().toLowerCase()) {
                // if (order.getStatus() == "Pending") {
                    firstPendingOrder = order;
                    break; // Exit loop once a match is found
                }
            }
            System.out.println("firstPendingOrder" + firstPendingOrder);
            return firstPendingOrder;
        }
    }

    @Override
    public Order updateOrder(Order theOrder) {
        return null;
    }

    // DELETE
    @Override
    public void deleteOrder(Order theOrder) {

    }
}
