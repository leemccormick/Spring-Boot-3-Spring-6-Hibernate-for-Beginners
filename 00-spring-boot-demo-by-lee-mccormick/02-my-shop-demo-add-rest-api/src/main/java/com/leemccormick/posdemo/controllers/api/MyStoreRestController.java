package com.leemccormick.posdemo.controllers.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.leemccormick.posdemo.aspect.ApiErrorException;
import com.leemccormick.posdemo.entity.*;
import com.leemccormick.posdemo.service.order.OrderService;
import com.leemccormick.posdemo.service.product.ProductService;
import com.leemccormick.posdemo.service.user.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/mystoredemo")
@Slf4j
public class MyStoreRestController {
    private OrderService orderService;
    private ProductService productService;
    private UserService userService;
    private ObjectMapper objectMapper;

    @Autowired
    public MyStoreRestController(OrderService theOrderService,
                                 ProductService theProductService,
                                 UserService theUserService,
                                 ObjectMapper theObjectMapper
    ) {
        orderService = theOrderService;
        productService = theProductService;
        userService = theUserService;
        objectMapper = theObjectMapper;
    }

    @GetMapping("/authentication")
    public ResponseEntity<UserDetail> authenticationUser(Authentication authentication) {
        try {
            String currentUserId = authentication.getName();
            UserDetail myUserDetails = userService.findUserDetailById(currentUserId);
            if (myUserDetails != null) {
                log.info(String.format("API | GET --> /users  | Success with my user's details is :  %s --> ", myUserDetails));
                return ResponseEntity.ok(myUserDetails);
            } else {
                String errorMessage = "An error occurred : Unable to find user in database";
                log.error(String.format("API | GET --> /users  : Error Message is  : %s --> ", errorMessage));
                throw new ApiErrorException(errorMessage, HttpStatus.NOT_FOUND);
            }
        } catch (Exception exception) {
            log.error(String.format("API | GET --> /products  : Error Exception is  : %s --> ", exception));
            return ResponseEntity.ofNullable(null);
        }
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

    @GetMapping("/products/productDetails")
    public ResponseEntity<Product> findProduct(@RequestParam("productId") int theProductId) {
        try {
            Product theProduct = productService.findById(theProductId);
            log.info(String.format("API | GET --> /products with productId param  : Success with Product is  : %s --> ", theProduct));
            return ResponseEntity.ok(theProduct);
        } catch (Exception exception) {
            String errorMessage = "Failed to add new product. Error Exception is " + exception.getMessage();
            log.error(String.format("API | GET --> /products with productId param  : Error Exception is  : %s -->", errorMessage));
            return ResponseEntity.ofNullable(null);
        }
    }

    @Transactional
    @PostMapping(value = "/products", produces = "application/json")
    public ResponseEntity<String> addProduct(@RequestBody Product theProduct, Authentication authentication) throws JsonProcessingException {
        try {
            theProduct.setCreatedBy(authentication.getName());
            productService.save(theProduct);
            ApiResponse response = new ApiResponse(false, "Successfully Added New Product.");
            Map<String, Object> combinedData = new HashMap<>();
            combinedData.put("status", response.getStatus());
            combinedData.put("code", response.getCode());
            combinedData.put("message", response.getMessage());
            combinedData.put("product", theProduct);
            log.info(String.format("API | POST --> /products  | Success with New Product is :  %s --> ", theProduct));
            return ResponseEntity.ok(objectMapper.writeValueAsString(combinedData));
        } catch (Exception exception) {
            String errorMessage = "Failed to add new product. Error Exception is " + exception.getMessage();
            ApiResponse errorResponse = new ApiResponse(true, HttpStatus.INTERNAL_SERVER_ERROR.value(), errorMessage);
            log.error(String.format("API | POST --> /products  : Error Exception is  : %s --> ", errorMessage));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(objectMapper.writeValueAsString(errorResponse));
        }
    }

    @Transactional
    @PutMapping(value = "/products", produces = "application/json")
    public ResponseEntity<String> updateProduct(@RequestBody Product theProduct, Authentication authentication) throws JsonProcessingException {
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
            ApiResponse response = new ApiResponse(false, "Successfully Updated Product.");
            Map<String, Object> combinedData = new HashMap<>();
            combinedData.put("status", response.getStatus());
            combinedData.put("code", response.getCode());
            combinedData.put("message", response.getMessage());
            combinedData.put("product", theProduct);
            log.info(String.format("API | PUT --> /products  | Success with New Product is :  %s --> ", theProduct));
            return ResponseEntity.ok(objectMapper.writeValueAsString(combinedData));
        } catch (Exception exception) {
            String errorMessage = "Failed to update product. Error Exception is " + exception.getMessage();
            ApiResponse errorResponse = new ApiResponse(true, HttpStatus.INTERNAL_SERVER_ERROR.value(), errorMessage);
            log.error(String.format("API | PUT --> /products  : Error Exception is  : %s --> ", exception.getMessage()));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(objectMapper.writeValueAsString(errorResponse));
        }
    }

    @DeleteMapping("/products/{productId}")
    public ResponseEntity<ApiResponse> deleteProduct(@PathVariable int productId) {
        try {
            productService.deleteById(productId);
            log.info(String.format("API | DELETE --> /products  | Success with Deleted Product Id is :  %s --> ", productId));
            ApiResponse successResponse = new ApiResponse(false);
            return ResponseEntity.ok(successResponse);
        } catch (Exception exception) {
            log.error(String.format("API | DELETE --> /products  : Error Exception is  : %s --> ", exception.getMessage()));
            HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
            ApiResponse errorResponse = new ApiResponse(true, status.value(), exception.getMessage());
            return ResponseEntity.status(status).body(errorResponse);
        }
    }

    // ONLY FOR SALE AND ADMIN
    @GetMapping("/info")
    public ResponseEntity<SaleInfo> findSaleInfo(Authentication authentication) {
        try {
            SaleInfo info = orderService.findSaleInfo();
            log.info(String.format("API | GET --> /info  | Success with SaleInfo is :  %s --> ", info));
            return ResponseEntity.ok(info);
        } catch (Exception exception) {
            log.error(String.format("API | GET --> /info  : Error Exception is  : %s --> ", exception.getMessage()));
            return ResponseEntity.ofNullable(null);
        }
    }

    // ONLY FOR ADMIN
    @GetMapping("/users")
    public ResponseEntity<List<UserDetail>> findAllUsersWithDetails(Authentication authentication) {
        try {
            List<UserDetail> allUserDetails = userService.findAllUsersWithDetails();
            log.info(String.format("API | GET --> /users  | Success with users details is :  %s --> ", allUserDetails));
            return ResponseEntity.ok(allUserDetails);
        } catch (Exception exception) {
            log.error(String.format("API | GET --> /users  : Error Exception is  : %s --> ", exception.getMessage()));
            return ResponseEntity.ofNullable(null);
        }
    }

    // ONLY FOR ADMIN, SALE AND CURRENT USER --> User should be able to see their own profile
    @GetMapping("/users/userDetails")
    public ResponseEntity<UserDetail> findUserDetails(@RequestParam("userId") String theUserId, Authentication authentication) {
        try {
            String currentUserId = authentication.getName();
            String authenticationRoles = authentication.getAuthorities().toString();
            boolean hasCustomerRole = userService.hasCustomerRole(authenticationRoles);
            boolean hasSaleRole = userService.hasSaleRole(authenticationRoles);
            boolean hasAdminRole = userService.hasAdminRole(authenticationRoles);

            if (hasSaleRole || hasAdminRole) {
                // See User Details
                UserDetail theUserDetails = userService.findUserDetailById(theUserId);
                if (theUserDetails != null) {
                    log.info(String.format("API | GET --> /users  | Success with user's details is :  %s --> ", theUserDetails));
                    return ResponseEntity.ok(theUserDetails);
                } else {
                    String errorMessage = "An error occurred : Unable to find user in database with user id : " + theUserId;
                    log.error(String.format("API | GET --> /users  : Error Message is  : %s --> ", errorMessage));
                    throw new ApiErrorException(errorMessage, HttpStatus.NOT_FOUND);
                }
            } else {
                if (hasCustomerRole && currentUserId.equals(theUserId)) {
                    // See own profile
                    UserDetail myUserDetails = userService.findUserDetailById(theUserId);
                    if (myUserDetails != null) {
                        log.info(String.format("API | GET --> /users  | Success with my user's details is :  %s --> ", myUserDetails));
                        return ResponseEntity.ok(myUserDetails);
                    } else {
                        String errorMessage = "An error occurred : Unable to find user in database";
                        log.error(String.format("API | GET --> /users  : Error Message is  : %s --> ", errorMessage));
                        throw new ApiErrorException(errorMessage, HttpStatus.NOT_FOUND);
                    }
                } else {
                    String errorMessage = "An error occurred : Unable to see other user's details.";
                    log.error(String.format("API | GET --> /users  : Error Message is  : %s --> ", errorMessage));
                    throw new ApiErrorException(errorMessage, HttpStatus.METHOD_NOT_ALLOWED);
                }
            }
        } catch (Exception exception) {
            log.error(String.format("API | GET --> /users  : Error Exception is  : %s --> ", exception.getMessage()));
            throw new ApiErrorException(exception.getMessage());
        }
    }
}