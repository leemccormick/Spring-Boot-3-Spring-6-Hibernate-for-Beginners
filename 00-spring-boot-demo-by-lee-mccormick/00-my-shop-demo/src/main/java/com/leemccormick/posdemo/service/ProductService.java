package com.leemccormick.posdemo.service;

import com.leemccormick.posdemo.entity.Product;

import java.util.List;

public interface ProductService {
    List<Product> findAllProduct();
    void deleteById(int theId);
    Product findById(int theId);
    void save(Product theProduct);
}
