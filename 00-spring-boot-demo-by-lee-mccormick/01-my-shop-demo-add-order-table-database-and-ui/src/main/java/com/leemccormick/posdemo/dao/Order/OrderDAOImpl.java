package com.leemccormick.posdemo.dao.Order;

import com.leemccormick.posdemo.entity.Order;
import com.leemccormick.posdemo.entity.OrderItem;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
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
        System.out.println("About to save order in OrderTestDAOImpl using entityManager : " + theOrder);
            entityManager.persist(theOrder);
    }

    @Override
    @Transactional
    public void saveItemToOrder(int theOrderId, OrderItem theOrderItem) {
        System.out.println("saveItemToOrder theOrderId : " + theOrderId);
        System.out.println("saveItemToOrder OrderItem : " + theOrderItem);
        entityManager.persist(theOrderItem);

    }

    // READ
    @Override
    public List<Order> findOrdersForTheCustomer(String theCustomerId) {
     // 1) Create query with JOIN FETCH
//        TypedQuery<Order> query = entityManager.createQuery(
//                "select o from Order s "
//                        + "JOIN FETCH s.courses "
//                        + "where s.id = :data",
//                Order.class
//        );

//        TypedQuery<Order> query = entityManager.createQuery(
//                "select o from Orders o "
//                        + "JOIN FETCH o.items "
//                        + "where o.customerId = :data",
//                Order.class
//        );


        TypedQuery<Order> query = entityManager.createQuery(
                "SELECT o FROM Order o " +
                        // If you want to fetch associated items, you can use a LEFT JOIN FETCH
                        // "LEFT JOIN FETCH o.items " +
                        "WHERE o.customerId = :customerId",
                Order.class
        );

//
//        SELECT *
//                FROM orders
//        WHERE customerId = <given_customer_id>;

        // 2) Set Parameter
        query.setParameter("customerId", theCustomerId);

        System.out.println("findOrdersForTheCustomer findOrdersForTheCustomer findOrdersForTheCustomer " + theCustomerId);
        // 3) Execute the query
        return query.getResultList();
    }

    @Override
    public List<Order> findOrdersForTheCustomerByOrderStatus(String theCustomerId, String theStatus) {
        TypedQuery<Order> query = entityManager.createQuery(
                "SELECT o FROM Order o " +
                        // If you want to fetch associated items, you can use a LEFT JOIN FETCH
                        // "LEFT JOIN FETCH o.items " +
                        "WHERE o.customerId = :customerId AND o.status = :orderStatus",
                Order.class
        );

//
//        SELECT *
//                FROM orders
//        WHERE customerId = <given_customer_id>;

        // 2) Set Parameter
        query.setParameter("customerId", theCustomerId);
        query.setParameter("orderStatus", theStatus);
        System.out.println("findOrdersForTheCustomerByOrderStatus" + theCustomerId + theStatus);
        // 3) Execute the query
        return query.getResultList();
    }
}
