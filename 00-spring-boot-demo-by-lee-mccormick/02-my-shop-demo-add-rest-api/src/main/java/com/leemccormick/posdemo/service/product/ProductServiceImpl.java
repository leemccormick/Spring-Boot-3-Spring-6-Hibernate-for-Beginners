package com.leemccormick.posdemo.service.product;

import com.leemccormick.posdemo.dao.ProductRepository;
import com.leemccormick.posdemo.entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
    public List<Product> findProductsForCustomer() {
        List<Product> listOfProduct = findAllProduct();
        List<Product> listOfProductToShowCustomer = new ArrayList<>();

        if (!listOfProduct.isEmpty()) {
            for (Product theProduct : listOfProduct) {
                if (theProduct.getQuantity() > 0) {
                    listOfProductToShowCustomer.add(theProduct);
                }
            }
        }

        return listOfProductToShowCustomer;
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
        if (theProduct.getName().isEmpty() || theProduct.getName().length() < 2) {
            throw new RuntimeException("Invalid Product Name ! Product name should be greater than 2 characters. Product Name is : " + theProduct.getName());
        } else if (theProduct.getDescription().isEmpty() || theProduct.getDescription().length() < 2) {
            throw new RuntimeException("Invalid Product Description ! Product Description should be greater than 2 characters. Product Description is : " + theProduct.getDescription());
        } else if (theProduct.getPrice() <= 0 || theProduct.getPrice().isNaN()) {
            throw new RuntimeException("Invalid Product Price ! Product Price should be greater than $0. Product Price is : " + theProduct.getPrice());
        } else if (theProduct.getQuantity() < 0) {
            throw new RuntimeException("Invalid Product Quantity ! Product Quantity should be greater than 0. Product Quantity is : " + theProduct.getQuantity());
        } else {
            List<Product> listOfProduct = findAllProduct();
            if (!listOfProduct.isEmpty()) {
                for (Product existingtProduct : listOfProduct) {
                    if (existingtProduct.getName().equals(theProduct.getName())) {
                        throw new RuntimeException("Invalid Product Name ! This product name is already existing in database. Product Name is : " + theProduct.getName() + " , and Product ID : " + existingtProduct.getId());
                    }
                }
            }
            theProduct.setCreatedDateTime(new Date());
            productRepository.save(theProduct);
        }
    }

    @Override
    public void update(Product theProduct) {
        if (theProduct.getName().isEmpty() || theProduct.getName().length() < 2) {
            throw new RuntimeException("Invalid Product Name ! Product name should be greater than 2 characters. Product Name is : " + theProduct.getName());
        } else if (theProduct.getDescription().isEmpty() || theProduct.getDescription().length() < 2) {
            throw new RuntimeException("Invalid Product Description ! Product Description should be greater than 2 characters. Product Description is : " + theProduct.getDescription());
        } else if (theProduct.getPrice() <= 0 || theProduct.getPrice().isNaN()) {
            throw new RuntimeException("Invalid Product Price ! Product Price should be greater than $0. Product Price is : " + theProduct.getPrice());
        } else if (theProduct.getQuantity() < 0) {
            throw new RuntimeException("Invalid Product Quantity ! Product Quantity should be greater than 0. Product Quantity is : " + theProduct.getQuantity());
        } else {
            Product existingProduct = findById(theProduct.getId());
            theProduct.setCreatedBy(existingProduct.getCreatedBy());
            theProduct.setCreatedDateTime(existingProduct.getCreatedDateTime());
            theProduct.setUpdatedDateTime(new Date());
            productRepository.save(theProduct);
        }
    }
}