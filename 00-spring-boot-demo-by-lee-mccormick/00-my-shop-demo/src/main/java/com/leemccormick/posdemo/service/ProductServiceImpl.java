package com.leemccormick.posdemo.service;

import com.leemccormick.posdemo.dao.ProductRepository;
import com.leemccormick.posdemo.entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {
    private ProductRepository productRepository;

    @Autowired
    public ProductServiceImpl(ProductRepository theProductRepository) {
        productRepository = theProductRepository;
    }

    @Override
    public List<Product> findAllProduct() {
        return productRepository.findAll();
    }

    @Override
    public void deleteById(int theId) {
        productRepository.deleteById(theId);
    }
}
