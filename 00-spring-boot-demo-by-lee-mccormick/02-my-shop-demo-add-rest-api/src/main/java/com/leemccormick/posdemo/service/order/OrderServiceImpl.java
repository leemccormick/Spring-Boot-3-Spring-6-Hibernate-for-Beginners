package com.leemccormick.posdemo.service.order;

import com.leemccormick.posdemo.dao.Order.OrderItemRepository;
import com.leemccormick.posdemo.dao.Order.OrderRepository;
import com.leemccormick.posdemo.dao.Order.OrderDAO;
import com.leemccormick.posdemo.dao.ProductRepository;
import com.leemccormick.posdemo.entity.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class OrderServiceImpl implements OrderService {
    private OrderRepository orderRepository;
    private OrderItemRepository orderItemRepository;
    private OrderDAO orderDAO;
    private ProductRepository productRepository;

    @Autowired
    public OrderServiceImpl(OrderRepository theOrderRepository,
                            OrderItemRepository theOrderItemRepository,
                            OrderDAO theOrderDAO,
                            ProductRepository theProductRepository
    ) {
        orderRepository = theOrderRepository;
        orderItemRepository = theOrderItemRepository;
        orderDAO = theOrderDAO;
        productRepository = theProductRepository;
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

        if (theProduce.getQuantity() > 1) {
            newOrderItem.setOrderId(theOrderId);
            newOrderItem.setProduct(theProduce);
            newOrderItem.setQuantity(1);
            double subtotalForItems = theProduce.getPrice() * 1;
            newOrderItem.setSubtotal(subtotalForItems);
            orderDAO.saveItemToOrder(theOrderId, newOrderItem);
            return updateTotalPrice(theOrderId, userId);
        } else {
            return findOrderById(theOrderId);
        }
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

    @Override
    public ErrorResponse validateOrderBeforeProcessing(Order theOrder) {
        ErrorResponse newErrorResponse = new ErrorResponse();
        boolean hasError = false;

        if (theOrder.getTotalAmount() <= 0) {
            hasError = true;
            newErrorResponse.addErrorMessage("Total amount must be grater than 0 to continue processing order.");
        }

        for (OrderItem item : theOrder.getItems()) {
            if (item.getQuantity() > 0) {
                if (item.getProduct().getQuantity() < item.getQuantity()) {
                    newErrorResponse.addErrorMessage("Out of stock. we only have " + item.getProduct().getQuantity() + " " + item.getProduct().getName() + " left, make sure you have correct quantity before processing.");
                }
            } else {
                hasError = true;
                newErrorResponse.addErrorMessage("Order item quantity can not be 0, please check " + item.getProduct().getName() + " , make sure you have correct quantity or delete this item before processing.");
            }
        }

        newErrorResponse.setHasError(hasError);
        return newErrorResponse;
    }

    @Override
    public SaleInfo findSaleInfo() {
        SaleInfo info = new SaleInfo();

        // Get Data
        List<Order> allOrders = findAllOrders();
        List<Product> allProducts = productRepository.findAll();

        // Assign Data
        info.setCountOfAllOrders(allOrders.size());
        info.setCountOfProducts(allProducts.size());

        // Calculate All Orders Summary
        for (Order theOrder : allOrders) {
            info.setTotalAmountSalesOfAllOrders(info.getTotalAmountSalesOfAllOrders() + theOrder.getTotalAmount());

            if (theOrder.getStatus().equals(OrderStatus.PENDING.getValue())) {
                info.setCountOfPendingOrders(info.getCountOfPendingOrders() + 1);
                info.setTotalAmountSalesOfPendingOrders(info.getTotalAmountSalesOfPendingOrders() + theOrder.getTotalAmount());
            }

            if (theOrder.getStatus().equals(OrderStatus.PROCESSING.getValue())) {
                info.setCountOfProcessingOrders(info.getCountOfProcessingOrders() + 1);
                info.setTotalAmountSalesOfProcessingOrders(info.getTotalAmountSalesOfProcessingOrders() + theOrder.getTotalAmount());
            }

            if (theOrder.getStatus().equals(OrderStatus.SHIPPED.getValue())) {
                info.setCountOfShippedOrders(info.getCountOfShippedOrders() + 1);
                info.setTotalAmountSalesOfShippedOrders(info.getTotalAmountSalesOfShippedOrders() + theOrder.getTotalAmount());
            }

            if (theOrder.getStatus().equals(OrderStatus.DELIVERED.getValue())) {
                info.setCountOfDeliveredOrders(info.getCountOfDeliveredOrders() + 1);
                info.setTotalAmountSalesOfDeliveredOrders(info.getTotalAmountSalesOfDeliveredOrders() + theOrder.getTotalAmount());
            }

            if (theOrder.getStatus().equals(OrderStatus.CANCELLED.getValue())) {
                info.setCountOfCancelledOrders(info.getCountOfCancelledOrders() + 1);
                info.setTotalAmountSalesOfCancelledOrders(info.getTotalAmountSalesOfCancelledOrders() + theOrder.getTotalAmount());
            }
        }

        // Calculate All Products Summary
        for (Product theProduce : allProducts) {
            if (theProduce.getQuantity() == 0) {
                info.setCountOfProductsOutOfStock(info.getCountOfProductsOutOfStock() + 1);
            } else {
                info.setCountOfProductsItem(info.getCountOfProductsItem() + theProduce.getQuantity());

                double totalProductAmount = theProduce.getPrice() * theProduce.getQuantity();
                info.setTotalAmountOfProducts(info.getTotalAmountOfProducts() + totalProductAmount);
            }
        }

        return info;
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
        Order existingOrder = findOrderById(theOrderId);
        int newQuantity = existingOrderItem.getQuantity() + 1;

        if (existingOrderItem.getProduct().getQuantity() >= newQuantity) {
            double newSubtotal = newQuantity * existingOrderItem.getProduct().getPrice();
            existingOrderItem.setQuantity(newQuantity);
            existingOrderItem.setSubtotal(newSubtotal);
            return updateOrderWithItem(existingOrder, existingOrderItem, existingOrder.getCustomerId());
        } else {
            return existingOrder;
        }
    }

    @Override
    public Order subtractItemToOrderByIds(int theOrderId, int theOrderItemId) {
        OrderItem existingOrderItem = findOrderItemById(theOrderItemId);
        Order existingOrder = findOrderById(theOrderId);

        if (existingOrderItem.getQuantity() > 0) {
            int newQuantity = existingOrderItem.getQuantity() - 1;
            double newSubtotal = newQuantity * existingOrderItem.getProduct().getPrice();
            existingOrderItem.setQuantity(newQuantity);
            existingOrderItem.setSubtotal(newSubtotal);
            updateQuantityAndCheckForDeletion(existingOrderItem, newQuantity);
            return updateOrderWithItem(existingOrder, existingOrderItem, existingOrder.getCustomerId());
        } else {
            return existingOrder;
        }
    }

    @Override
    public Order checkOut(Order theOrder) {
        // Update Produce Quantity
        for (OrderItem item : theOrder.getItems()) {
            Product theProduct = item.getProduct();
            int newProductQuantity = theProduct.getQuantity() - item.getQuantity();
            theProduct.setQuantity(newProductQuantity);
            productRepository.save(theProduct);
        }

        // Update Order Status and return the updated order
        theOrder.setStatus(OrderStatus.PROCESSING.getValue());
        return updateOrder(theOrder, theOrder.getCustomerId());
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
