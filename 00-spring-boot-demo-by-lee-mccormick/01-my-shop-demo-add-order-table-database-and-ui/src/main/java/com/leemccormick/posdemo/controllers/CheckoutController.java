package com.leemccormick.posdemo.controllers;


import com.leemccormick.posdemo.entity.OrderItem;
import com.leemccormick.posdemo.service.order.OrderService;
import com.leemccormick.posdemo.service.product.ProductService;
import com.leemccormick.posdemo.service.user.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Slf4j
public class CheckoutController {
    private ProductService productService;
    private UserService userService;
    private OrderService orderService;

    public CheckoutController(ProductService theProductService,
                              UserService theUserService,
                              OrderService theOrderService
    ) {
        productService = theProductService;
        userService = theUserService;
        orderService = theOrderService;
    }
}
