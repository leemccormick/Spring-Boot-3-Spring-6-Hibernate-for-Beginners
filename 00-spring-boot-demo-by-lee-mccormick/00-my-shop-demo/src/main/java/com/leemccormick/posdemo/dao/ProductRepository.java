package com.leemccormick.posdemo.dao;

import com.leemccormick.posdemo.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Integer> { }
