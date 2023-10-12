package com.leemccormick.posdemo.service.order;

import com.leemccormick.posdemo.dao.Order.OrderItemRepository;
import com.leemccormick.posdemo.dao.Order.OrderRepository;
import com.leemccormick.posdemo.dao.Order.OrderDAO;
import com.leemccormick.posdemo.dao.ProductRepository;
import com.leemccormick.posdemo.entity.*;
import com.leemccormick.posdemo.service.user.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
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
    private UserService userService;

    @Autowired
    public OrderServiceImpl(OrderRepository theOrderRepository,
                            OrderItemRepository theOrderItemRepository,
                            OrderDAO theOrderDAO,
                            ProductRepository theProductRepository,
                            UserService theUserService
    ) {
        orderRepository = theOrderRepository;
        orderItemRepository = theOrderItemRepository;
        orderDAO = theOrderDAO;
        productRepository = theProductRepository;
        userService = theUserService;
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

        if (theProduce.getQuantity() > 0) {
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

    @Override
    public Order addNewItemToCart(int theOrderId, Product theProduce, String userId) {
        OrderItem newOrderItem = new OrderItem();

        if (theProduce.getQuantity() > 0) {
            newOrderItem.setProductId(theProduce.getId());
            newOrderItem.setOrderId(theOrderId);
            newOrderItem.setProduct(theProduce);
            newOrderItem.setQuantity(1);
            double subtotalForItems = theProduce.getPrice() * 1;
            newOrderItem.setSubtotal(subtotalForItems);

            Order nReturnOrder = orderDAO.saveItemToTheOrder(theOrderId, newOrderItem);

            // Find total price from items
            double totalPrice = 0;
            if (nReturnOrder.getItems() != null) {
                if (!nReturnOrder.getItems().isEmpty()) {
                    for (OrderItem item : nReturnOrder.getItems()) {
                        totalPrice += item.getSubtotal();
                    }
                }
            }

            nReturnOrder.setUpdatedBy(userId);
            nReturnOrder.setUpdatedDateTime(new Date());
            nReturnOrder.setTotalAmount(totalPrice);
            orderDAO.updateOrder(nReturnOrder);
            return nReturnOrder;
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
    public List<Order> findAllOrdersByOrderStatus(String orderStatus) {
        List<Order> allOrders = findAllOrders();
        List<Order> nReturnOrders = new ArrayList<>();
        for (Order order : allOrders) {
            if (order.getStatus().equalsIgnoreCase(orderStatus)) {
                nReturnOrders.add(order);
            }
        }
        return nReturnOrders;
    }

    @Override
    public List<Order> findOrdersForCustomer(String customerId) {
        return orderDAO.findOrdersForTheCustomer(customerId);
    }

    @Override
    public List<Order> findOrdersByOrderStatusForTheCustomer(String orderStatus, String customerId) {
        List<Order> allOrdersForTheCustomer = findOrdersForCustomer(customerId);
        List<Order> nReturnOrders = new ArrayList<>();
        for (Order order : allOrdersForTheCustomer) {
            if (order.getStatus().equalsIgnoreCase(orderStatus)) {
                nReturnOrders.add(order);
            }
        }
        return nReturnOrders;
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
    public Order updateItemQuantityInTheCart(int theOrderId, int theOrderItemId, int theQuantity, String userId) {
        try {
            if (theQuantity > 0) {
                OrderItem existingOrderItem = findOrderItemById(theOrderItemId);
                Order existingOrder = findOrderById(theOrderId);
                Optional<Product> existingProduct = productRepository.findById(existingOrderItem.getProduct().getId());

                if (existingOrder.getCustomerId().equals(userId)) {
                    if (existingOrder.getStatus().equals(OrderStatus.PENDING.getValue())) {
                        if (existingProduct.isPresent()) {
                            Product theProduct = existingProduct.get();
                            // Make sure produce's quantity is more than theQuantity. and theQuantity is more than 0
                            if (theProduct.getQuantity() > 0) {
                                if (theProduct.getQuantity() >= theQuantity) {
                                    double newSubtotal = theQuantity * existingOrderItem.getProduct().getPrice();
                                    existingOrderItem.setQuantity(theQuantity);
                                    existingOrderItem.setSubtotal(newSubtotal);
                                    return updateOrderWithItem(existingOrder, existingOrderItem, existingOrder.getCustomerId());
                                } else {
                                    String errorMessage = "The quantity you order is more than the item's quantity we have in stock.";
                                    errorMessage += "\n" + theProduct.getName() + " has " + theProduct.getQuantity() + " items.";
                                    throw new RuntimeException(errorMessage);
                                }
                            } else {
                                throw new RuntimeException(theProduct.getName() + " is out of stock.");
                            }
                        } else {
                            throw new RuntimeException("Unable to find product detail to continue...");
                        }
                    } else {
                        throw new RuntimeException("Only allowed to modify the order with pending status.");
                    }
                } else {
                    throw new RuntimeException("Only allowed to customer to modify their own order.");
                }
            } else {
                throw new RuntimeException("The quantity must be greater than 0.");
            }
        } catch (Exception exception) {
            throw new RuntimeException(exception.getMessage());
        }
    }

    @Override
    public Order checkOut(Order theOrder) {
        try {
            // Update Produce Quantity
            List<OrderItem> newOrderItems = new ArrayList<>();
            for (OrderItem item : theOrder.getItems()) {
                OrderItem updatedItem = updateEachProductQuantityWhenCustomerCheckoutWithItem(item.getProductId(), item);
                newOrderItems.add(updatedItem);
            }

            // Update Order Status and return the updated order
            theOrder.setItems(newOrderItems);
            theOrder.setStatus(OrderStatus.PROCESSING.getValue());
            Order theLastUpdatedOrder = updateTotalPrice(theOrder);
            return updateOrder(theLastUpdatedOrder, theOrder.getCustomerId());
        } catch (Exception exception) {
            throw new RuntimeException(exception.getMessage());
        }
    }

    @Override
    public Order validateAndCheckOut(Order theOrder, Authentication authentication) {
        Order existingOrder = findOrderById(theOrder.getId());
        if (existingOrder == null) {
            throw new RuntimeException("Unable to find order with id  " + theOrder.getId());
        } else {
            if (theOrder.getCustomerId().equals(authentication.getName())) {
                if (theOrder.getStatus().equals(OrderStatus.PENDING.getValue()) &&
                        theOrder.getStatus().equals(existingOrder.getStatus())
                ) {
                    ErrorResponse validationResponse = validateOrderBeforeProcessing(theOrder);
                    if (validationResponse.getHasError()) {
                        StringBuilder errorMessages = new StringBuilder("Error checking out : ");
                        for (String message : validationResponse.getErrorMessages()) {
                            errorMessages.append(" ").append(message);
                        }
                        throw new RuntimeException(errorMessages.toString());
                    } else {
                        return checkOut(theOrder);
                    }
                } else {
                    throw new RuntimeException("The order status is " + theOrder.getStatus() + ". Only pending order is allow to process check out.");
                }
            } else {
                throw new RuntimeException("Only current user who created this order are allow to check out.");
            }
        }
    }

    @Override
    public Order updateOrderStatus(int theOrderId, Authentication authentication, OrderStatus status) {
        try {
            if (userService.hasAdminRole(authentication) || userService.hasSaleRole(authentication)) {
                Order existingOrder = findOrderById(theOrderId);
                if (existingOrder != null) {
                    if (existingOrder.getStatus().equals(OrderStatus.PENDING.getValue())) {
                        throw new RuntimeException("Unable to update pending orders.");
                    } else if (existingOrder.getStatus().equals(status.getValue())) {
                        throw new RuntimeException("The status is the same as existing status.");
                    } else {
                        if (status.getValue().equals(OrderStatus.CANCELLED.getValue())) {
                            if (userService.hasAdminRole(authentication)) {
                                existingOrder.setStatus(OrderStatus.CANCELLED.getValue());
                                return updateOrder(existingOrder, authentication.getName());
                            } else {
                                throw new RuntimeException("Only admin allowed to cancel the order.");
                            }
                        } else {
                            existingOrder.setStatus(status.getValue());
                            return updateOrder(existingOrder, authentication.getName());
                        }
                    }
                } else {
                    throw new RuntimeException("Unable to find order with id  " + theOrderId);
                }
            } else {
                throw new RuntimeException("Only sales and admins are able to update order status.");
            }
        } catch (Exception exception) {
            throw new RuntimeException(exception.getMessage());
        }
    }

    private OrderItem updateEachProductQuantityWhenCustomerCheckoutWithItem(int theProductId, OrderItem item) {
        Optional<Product> existingProduct = productRepository.findById(theProductId);
        if (existingProduct.isPresent()) {
            Product theProduct = existingProduct.get();

            if (item.getQuantity() > theProduct.getQuantity()) {
                String errorMessage = "The quantity you order is more than the item's quantity we have in stock.";
                errorMessage += "\n" + theProduct.getName() + " has " + theProduct.getQuantity() + " items.";
                throw new RuntimeException(errorMessage);
            } else {
                int newProductQuantity = theProduct.getQuantity() - item.getQuantity();
                theProduct.setQuantity(newProductQuantity);
                item.setProduct(theProduct);
                productRepository.save(theProduct);

                if (item.getSubtotal() == item.getQuantity() * theProduct.getPrice()) {
                    return item;
                } else {
                    double newSubtotal = item.getQuantity() * theProduct.getPrice();
                    item.setSubtotal(newSubtotal);
                    return item;
                }
            }
        } else {
            throw new RuntimeException("Unable to find product with id : " + theProductId);
        }
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

    private Order updateTotalPrice(Order currentOrder) {
        // Find total price from items
        double totalPrice = 0;
        if (currentOrder.getItems() != null) {
            if (!currentOrder.getItems().isEmpty()) {
                for (OrderItem item : currentOrder.getItems()) {
                    totalPrice += item.getSubtotal();
                }
            }
        }

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
            throw new RuntimeException(exception.getMessage());
        }
    }

    @Override
    public Order deleteItemInTheCart(int theOrderItemId, int theOrderId, Authentication authentication) {
        try {
            Order existingOrder = findOrderById(theOrderId);
            if (existingOrder.getCustomerId().equals(authentication.getName()) || userService.hasAdminRole(authentication)) {
                if (existingOrder.getStatus().equals(OrderStatus.PENDING.getValue())) {
                    OrderItem itemToDelete = findOrderItemById(theOrderItemId);
                    deleteOrderItem(itemToDelete);
                    return updateTotalPrice(theOrderId, authentication.getName());
                } else {
                    throw new RuntimeException("Only allowed to delete the order item on pending order.");
                }
            } else {
                throw new RuntimeException("Only allowed admin and the customer to delete the item on their own order.");
            }
        } catch (Exception exception) {
            log.error(String.format("OrderService : Exception deleteOrderItem is : %s -->  ", exception));
            throw new RuntimeException(exception.getMessage());
        }
    }

    @Override
    public void deleteOrder(int theOrderId, Authentication authentication) {
        try {
            if (userService.hasAdminRole(authentication)) {
                Order existingOrder = findOrderById(theOrderId);
                if (existingOrder != null) {
                    if (!existingOrder.getStatus().equals(OrderStatus.PENDING.getValue())) {
                        throw new RuntimeException("Only allow to delete the pending order. This order status is " + existingOrder.getStatus());
                    } else {
                        deleteOrder(existingOrder);
                    }
                } else {
                    throw new RuntimeException("Unable to find order with id  " + theOrderId);
                }
            } else {
                throw new RuntimeException("Only admins are able to delete orders.");
            }
        } catch (Exception exception) {
            throw new RuntimeException(exception.getMessage());
        }
    }
}