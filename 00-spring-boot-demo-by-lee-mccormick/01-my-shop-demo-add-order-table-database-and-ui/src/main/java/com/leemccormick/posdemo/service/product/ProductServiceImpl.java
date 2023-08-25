package com.leemccormick.posdemo.service.product;

import com.leemccormick.posdemo.dao.ProductRepository;
import com.leemccormick.posdemo.entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

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

    @Override
    public Product findById(int theId) {
        Optional<Product> result = productRepository.findById(theId);

        Product theProduct = null;

        if (result.isPresent()) {
            theProduct = result.get();
        } else {
            // we didn't find the product
            throw new RuntimeException("Did not find product with id - " + theId);
        }

        return theProduct;
    }

    @Override
    public void save(Product theProduct) {
        theProduct.setCreatedDateTime(new Date());
        productRepository.save(theProduct);
    }

    @Override
    public void update(Product theProduct) {
        Product existingProduct = findById(theProduct.getId());
        theProduct.setCreatedBy(existingProduct.getCreatedBy());
        theProduct.setCreatedDateTime(existingProduct.getCreatedDateTime());
        theProduct.setUpdatedDateTime(new Date());
        productRepository.save(theProduct);
    }
}
