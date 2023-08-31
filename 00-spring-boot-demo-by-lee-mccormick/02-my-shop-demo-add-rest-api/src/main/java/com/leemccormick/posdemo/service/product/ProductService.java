package com.leemccormick.posdemo.service.product;

import com.leemccormick.posdemo.entity.Product;

import java.util.List;

public interface ProductService {
    List<Product> findAllProduct();

    List<Product> findProductsForCustomer();

    void deleteById(int theId);

    Product findById(int theId);

    void save(Product theProduct);

    void update(Product theProduct);
}
