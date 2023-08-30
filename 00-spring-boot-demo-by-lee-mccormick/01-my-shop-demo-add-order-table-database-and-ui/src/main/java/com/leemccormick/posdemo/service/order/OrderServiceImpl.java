package com.leemccormick.posdemo.service.order;

import com.leemccormick.posdemo.dao.Order.OrderItemRepository;
import com.leemccormick.posdemo.dao.Order.OrderRepository;
import com.leemccormick.posdemo.dao.Order.OrderDAO;
import com.leemccormick.posdemo.entity.Order;
import com.leemccormick.posdemo.entity.OrderItem;
import com.leemccormick.posdemo.entity.OrderStatus;
import com.leemccormick.posdemo.entity.Product;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class OrderServiceImpl implements OrderService {
    private OrderRepository orderRepository;
    private OrderItemRepository orderItemRepository;
    private OrderDAO orderDAO;

    @Autowired
    public OrderServiceImpl(OrderRepository theOrderRepository,
                            OrderItemRepository theOrderItemRepository,
                            OrderDAO theOrderDAO) {
        orderRepository = theOrderRepository;
        orderItemRepository = theOrderItemRepository;
        orderDAO = theOrderDAO;
    }

    // CREATE
    @Override
    public Order addNewOrder(Order theOrder, String userId) {
        theOrder.setCustomerId(userId);
        theOrder.setTotalAmount(0.0);
        theOrder.setStatus(OrderStatus.PENDING.getValue());
        theOrder.setCreatedDateTime(new Date());
        theOrder.setUpdatedDateTime(new Date());
        theOrder.setCreatedBy(userId);
        theOrder.setUpdatedBy(userId);
        orderDAO.saveNewOrder(theOrder);
        return orderRepository.save(theOrder);
    }

    @Override
    public Order addNewItemToTheOrder(int theOrderId, OrderItem theOrderItem, String userId) {
        orderDAO.saveItemToOrder(theOrderId, theOrderItem);
        return updateTotalPrice(theOrderId, userId);
    }

    @Override
    public Order addNewItemToTheOrder(int theOrderId, Product theProduce, String userId) {
        OrderItem newOrderItem = new OrderItem();
        newOrderItem.setOrderId(theOrderId);
        newOrderItem.setProduct(theProduce);
        newOrderItem.setQuantity(1);
        double subtotalForItems = theProduce.getPrice() * 1;
        newOrderItem.setSubtotal(subtotalForItems);
        orderDAO.saveItemToOrder(theOrderId, newOrderItem);
        return updateTotalPrice(theOrderId, userId);
    }

    // READ
    @Override
    public List<Order> findAllOrders() {
        return orderRepository.findAll();
    }

    @Override
    public List<Order> findOrdersForCustomer(String customerId) {
        return orderDAO.findOrdersForTheCustomer(customerId);
    }

    @Override
    public Order findOrderById(int theId) {
        Optional<Order> result = orderRepository.findById(theId);

        Order theOrder = null;

        if (result.isPresent()) {
            theOrder = result.get();
        } else {
            // we didn't find the product
            throw new RuntimeException("Did not find order with id - " + theId);
        }
        return theOrder;
    }

    @Override
    public OrderItem findOrderItemById(int theOrderItemId) {
        Optional<OrderItem> result = orderItemRepository.findById(theOrderItemId);

        OrderItem theOrderItem = null;

        if (result.isPresent()) {
            theOrderItem = result.get();
        } else {
            throw new RuntimeException("Did not find order item with id - " + theOrderItemId);
        }

        return theOrderItem;
    }

    @Override
    public Order findPendingOrderForTheCustomer(String customerId) {
        List<Order> pendingOrdersByTheCustomer = orderDAO.findOrdersForTheCustomerByOrderStatus(customerId, OrderStatus.PENDING.getValue());//orderDAO.findOrdersForTheCustomer(customerId);

        if (pendingOrdersByTheCustomer.isEmpty()) {
            return null;
        } else {
            Order firstPendingOrder = null;

            for (Order order : pendingOrdersByTheCustomer) {
                if (order.getStatus().equalsIgnoreCase(OrderStatus.PENDING.getValue())) {
                    firstPendingOrder = order;
                    break; // Exit loop once a match is found
                }
            }

            assert firstPendingOrder != null;
            if (firstPendingOrder.getTotalAmount() == 0 && (firstPendingOrder.getItems() != null)) {
                firstPendingOrder = updateTotalPrice(firstPendingOrder.getId(), customerId);
            }

            return firstPendingOrder;
        }
    }

    @Override
    public double findTotalSales(List<Order> theListOfOrder) {
        double nReturnTotalSales = 0;
        for (Order order : theListOfOrder) {
            nReturnTotalSales += order.getTotalAmount();
        }
        return nReturnTotalSales;
    }

    // UPDATE
    @Override
    public Order updateOrder(Order theOrder, String userId) {
        theOrder.setUpdatedBy(userId);
        theOrder.setUpdatedDateTime(new Date());
        orderDAO.updateOrder(theOrder);
        return theOrder;
    }

    @Override
    public OrderItem updateOrderItem(OrderItem theOrderItem) {
        orderDAO.updateOrderItem(theOrderItem);
        return theOrderItem;
    }

    @Override
    public Order updateOrderWithItem(Order theOrder, OrderItem theOrderItem, String userId) {
        orderDAO.updateOrderItem(theOrderItem);
        return updateTotalPrice(theOrder.getId(), userId);
    }

    @Override
    public Order addItemToOrderByIds(int theOrderId, int theOrderItemId) {
        OrderItem existingOrderItem = findOrderItemById(theOrderItemId);
        int newQuantity = existingOrderItem.getQuantity() + 1;
        double newSubtotal = newQuantity * existingOrderItem.getProduct().getPrice();
        existingOrderItem.setQuantity(newQuantity);
        existingOrderItem.setSubtotal(newSubtotal);
        Order existingOrder = findOrderById(theOrderId);
        return updateOrderWithItem(existingOrder, existingOrderItem, existingOrder.getCustomerId());
    }

    @Override
    public Order subtractItemToOrderByIds(int theOrderId, int theOrderItemId) {
        OrderItem existingOrderItem = findOrderItemById(theOrderItemId);

        if (existingOrderItem.getQuantity() > 0) {
            int newQuantity = existingOrderItem.getQuantity() - 1;
            double newSubtotal = newQuantity * existingOrderItem.getProduct().getPrice();
            existingOrderItem.setQuantity(newQuantity);
            existingOrderItem.setSubtotal(newSubtotal);
            updateQuantityAndCheckForDeletion(existingOrderItem, newQuantity);
        }

        Order existingOrder = findOrderById(theOrderId);
        return updateOrderWithItem(existingOrder, existingOrderItem, existingOrder.getCustomerId());
    }

    private Order updateTotalPrice(int theOrderId, String userId) {
        Order currentOrder = findOrderById(theOrderId);

        // Find total price from items
        double totalPrice = 0;
        if (currentOrder.getItems() != null) {
            if (!currentOrder.getItems().isEmpty()) {
                for (OrderItem item : currentOrder.getItems()) {
                    totalPrice += item.getSubtotal();
                }
            }
        }

        currentOrder.setUpdatedBy(userId);
        currentOrder.setUpdatedDateTime(new Date());
        currentOrder.setTotalAmount(totalPrice);
        orderDAO.updateOrder(currentOrder);
        return currentOrder;
    }

    private void updateQuantityAndCheckForDeletion(OrderItem orderItem, int newQuantity) {
        orderItem.setQuantity(newQuantity);
        if (newQuantity <= 0) {
            deleteOrderItem(orderItem);
        } else {
            orderItemRepository.save(orderItem);
        }
    }

    // DELETE
    @Override
    public void deleteOrder(Order theOrder) {
        orderRepository.delete(theOrder);
    }

    @Override
    public void deleteOrderItem(OrderItem theOrderOrderItem) {
        try {
            orderDAO.deleteOrderItem(theOrderOrderItem);
        } catch (Exception exception) {
            log.error(String.format("OrderService : Exception deleteOrderItem is : %s -->  ", exception));
        }
    }
}
