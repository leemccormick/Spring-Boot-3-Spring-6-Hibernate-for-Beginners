package com.leemccormick.posdemo.dao.Order;

import com.leemccormick.posdemo.entity.Order;
import com.leemccormick.posdemo.entity.OrderItem;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Slf4j
public class OrderDAOImpl implements OrderDAO {
    private EntityManager entityManager;

    @Autowired
    public OrderDAOImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    // CREATE
    @Override
    @Transactional
    public void saveNewOrder(Order theOrder) {
        entityManager.persist(theOrder);
    }

    @Override
    @Transactional
    public void saveItemToOrder(int theOrderId, OrderItem theOrderItem) {
        entityManager.persist(theOrderItem);
    }

    @Override
    @Transactional
    public Order saveItemToTheOrder(int theOrderId, OrderItem theOrderItem) {
        Order theOrder = findTheOrderById(theOrderId);
        theOrder.addItem(theOrderItem);
        entityManager.merge(theOrder);
        return theOrder;
    }

    // READ
    @Override
    public List<Order> findOrdersForTheCustomer(String theCustomerId) {
        // 1) Create query with JOIN FETCH
        TypedQuery<Order> query = entityManager.createQuery(
                "SELECT o FROM Order o " +
                        "WHERE o.customerId = :customerId",
                Order.class
        );

        // 2) Set Parameter
        query.setParameter("customerId", theCustomerId);

        // 3) Execute the query
        return query.getResultList();
    }

    @Override
    public List<Order> findOrdersForTheCustomerByOrderStatus(String theCustomerId, String theStatus) {
        TypedQuery<Order> query = entityManager.createQuery(
                "SELECT o FROM Order o " +
                        "WHERE o.customerId = :customerId AND o.status = :orderStatus",
                Order.class
        );

        // 2) Set Parameter
        query.setParameter("customerId", theCustomerId);
        query.setParameter("orderStatus", theStatus);

        // 3) Execute the query
        return query.getResultList();
    }

    @Override
    public Order findTheOrderById(int theOrderId) {
        // 1) Create query with JOIN FETCH
        TypedQuery<Order> query = entityManager.createQuery(
                "SELECT o FROM Order o " +
                        "WHERE o.id = :theOrderId",
                Order.class
        );

        // 2) Set Parameter
        query.setParameter("theOrderId", theOrderId);

        // 3) Execute the query
        return query.getSingleResult();
    }

    // UPDATE
    @Override
    @Transactional
    public void updateOrder(Order theOrder) {
        entityManager.merge(theOrder);
    }

    @Override
    @Transactional
    public void updateOrderItem(OrderItem theOrderItem) {
        entityManager.merge(theOrderItem);
    }

    // DELETE
    @Override
    @Transactional
    public void deleteOrderItem(OrderItem theOrderOrderItem) {
        entityManager.remove(theOrderOrderItem);
    }
}
