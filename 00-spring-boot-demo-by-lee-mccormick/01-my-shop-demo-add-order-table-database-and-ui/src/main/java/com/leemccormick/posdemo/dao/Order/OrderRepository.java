package com.leemccormick.posdemo.dao.Order;

import com.leemccormick.posdemo.entity.Order;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

//public interface OrderDAO {
//
//}
public interface OrderRepository extends JpaRepository<Order, Integer> {

}
