package com.leemccormick.posdemo.controllers;

import com.leemccormick.posdemo.entity.OrderItem;
import com.leemccormick.posdemo.entity.Product;
import com.leemccormick.posdemo.service.order.OrderService;
import com.leemccormick.posdemo.service.product.ProductService;
import com.leemccormick.posdemo.service.user.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/mystoredemo")
@Slf4j
public class MyStoreRestController {
    private OrderService orderService;
    private ProductService productService;
    private UserService userService;

    @Autowired
    public MyStoreRestController(OrderService theOrderService,
                                 ProductService theProductService,
                                 UserService theUserService
    ) {
        orderService = theOrderService;
        productService = theProductService;
        userService = theUserService;
    }

    @GetMapping("/products")
    public ResponseEntity<List<Product>> findProducts(Authentication authentication) {
        try {
            String authenticationRoles = authentication.getAuthorities().toString();
            boolean hasSaleRole = userService.hasSaleRole(authenticationRoles);
            boolean hasAdminRole = userService.hasAdminRole(authenticationRoles);

            List<Product> listOfProduct = new ArrayList<>();
            if (hasSaleRole || hasAdminRole) {
                listOfProduct = productService.findAllProduct();
            } else {
                listOfProduct = productService.findProductsForCustomer();
            }

            log.info(String.format("API | GET --> /products  | Success with list of product count is :  %s --> %s", listOfProduct.size(), listOfProduct));
            return ResponseEntity.ok(listOfProduct);
        } catch (Exception exception) {
            log.error(String.format("API | GET --> /products  : Error Exception is  : %s --> ", exception));
            return ResponseEntity.ofNullable(null);
        }
    }

    @PostMapping("/products")
    public ResponseEntity<String> addProduct(@RequestBody Product theProduct, Authentication authentication) {
        try {
            theProduct.setCreatedBy(authentication.getName());
            productService.save(theProduct);
            log.info(String.format("API | POST --> /products  | Success with New Product is :  %s --> ", theProduct));
            return ResponseEntity.ok("Successfully Add New Product : " + theProduct);
        } catch (Exception exception) {
            String errorMessage = "Failed to add new product. Error Exception is " + exception.getMessage();
            log.error(String.format("API | POST --> /products  : Error Exception is  : %s --> ", errorMessage));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage);
        }
    }

    @PutMapping("/products")
    public ResponseEntity<Product> updateProduct(@RequestBody Product theProduct, Authentication authentication) {
        try {
            Product existingProduct = productService.findById(theProduct.getId());
            theProduct.setUpdatedBy(authentication.getName());

            if (theProduct.getName() == null || theProduct.getName().isEmpty()) {
                theProduct.setName(existingProduct.getName());
            }

            if (theProduct.getDescription() == null || theProduct.getDescription().isEmpty()) {
                theProduct.setDescription(existingProduct.getDescription());
            }

            if (theProduct.getPrice() == null || theProduct.getPrice().isNaN()) {
                theProduct.setPrice(existingProduct.getPrice());
            }

            productService.update(theProduct);
            log.info(String.format("API | PUT --> /products  | Success with New Product is :  %s --> ", theProduct));
            return ResponseEntity.ok(theProduct);
        } catch (Exception exception) {
            log.error(String.format("API | PUT --> /products  : Error Exception is  : %s --> ", exception.getMessage()));
            return (ResponseEntity<Product>) ResponseEntity.internalServerError();
        }
    }

    @DeleteMapping("/products/{productId}")
    public ResponseEntity<Boolean> deleteProduct(@PathVariable int productId) {
        try {
            productService.deleteById(productId);
            log.info(String.format("API | DELETE --> /products  | Success with Deleted Product Id is :  %s --> ", productId));
            return ResponseEntity.ok(true);
        } catch (Exception exception) {
            log.error(String.format("API | DELETE --> /products  : Error Exception is  : %s --> ", exception.getMessage()));
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(false);
        }
    }
}
