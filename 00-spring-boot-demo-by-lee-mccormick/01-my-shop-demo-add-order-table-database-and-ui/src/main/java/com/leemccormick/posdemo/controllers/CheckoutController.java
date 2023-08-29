package com.leemccormick.posdemo.controllers;

import com.leemccormick.posdemo.entity.Order;
import com.leemccormick.posdemo.entity.OrderItem;
import com.leemccormick.posdemo.entity.OrderStatus;
import com.leemccormick.posdemo.service.order.OrderService;
import com.leemccormick.posdemo.service.product.ProductService;
import com.leemccormick.posdemo.service.user.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

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

    @GetMapping("/checkOut/addItem")
    public String addItemToOrder(@RequestParam("orderId") int theOrderId,
                                 @RequestParam("orderItemId") int theOrderItemId
    ) {
        Order updatedOrder = orderService.addItemToOrderByIds(theOrderId, theOrderItemId);
        log.info(String.format("CHECK OUT PAGE | addItemToOrder --> updatedOrder is : %s : ", updatedOrder));
        return "redirect:/checkOut";
    }

    @GetMapping("/checkOut/subtractItem")
    public String subtractItemToOrder(@RequestParam("orderId") int theOrderId,
                                      @RequestParam("orderItemId") int theOrderItemId
    ) {
        Order updatedOrder = orderService.subtractItemToOrderByIds(theOrderId, theOrderItemId);
        log.info(String.format("CHECK OUT PAGE | subtractItemToOrder -->  updatedOrder is : %s : ", updatedOrder));
        return "redirect:/checkOut";
    }

    @GetMapping("/checkOut/deleteItem")
    public String deleteItemToOrder(@RequestParam("orderItemId") int theOrderItemId) {
        OrderItem itemToDelete = orderService.findOrderItemById(theOrderItemId);
        orderService.deleteOrderItem(itemToDelete);
        log.info(String.format("CHECK OUT PAGE | deleteOrderItem is : %s : ", itemToDelete));
        return "redirect:/checkOut";
    }

    @GetMapping("/checkOut/confirm")
    public String confirmOrderAndPay(@RequestParam("orderId") int theOrderId, Model theModel) {
        Order existingOrder = orderService.findOrderById(theOrderId);

        if (existingOrder.getTotalAmount() > 0) {
            existingOrder.setStatus(OrderStatus.PROCESSING.getValue());
            Order processingOrder = orderService.updateOrder(existingOrder, existingOrder.getCustomerId());

            theModel.addAttribute("processingOrder", processingOrder);

            log.info(String.format("CHECK OUT PAGE | Confirm And Pay --> Processing Order is : %s : ", processingOrder));
            return "/thankyou";
        } else {
            log.error("CHECK OUT PAGE ERROR --> Total amount must be grater than 0 to continue processing order.");
            return "redirect:/checkOut"; // Return to the form with error messages
        }
    }
}
