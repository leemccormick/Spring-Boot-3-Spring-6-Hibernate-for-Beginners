package com.leemccormick.posdemo.controllers;

import com.leemccormick.posdemo.entity.Order;
import com.leemccormick.posdemo.entity.OrderItem;
import com.leemccormick.posdemo.entity.Product;
import com.leemccormick.posdemo.service.order.OrderService;
import com.leemccormick.posdemo.service.product.ProductService;
import com.leemccormick.posdemo.service.user.UserService;
import jakarta.persistence.Column;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

enum SaveMode {
    ADD, UPDATE
}

@Controller
@Slf4j // @Slf4j --> for using log.info() --> Need to add dependency
public class MyStoreController {
    private ProductService productService;
    private UserService userService;
    private OrderService orderService;
    private SaveMode saveMode = SaveMode.ADD;
    private List<Product> listOfProduct = new ArrayList<>();
    private Order currentOrder = null;

    public MyStoreController(ProductService theProductService,
                             UserService theUserService,
                             OrderService theOrderService
    ) {
        productService = theProductService;
        userService = theUserService;
        orderService = theOrderService;
    }

    @GetMapping("/")
    public String showHome(Authentication authentication, Model model) {
        List<Product> listOfProducts = productService.findAllProduct();
        String currentUserId = authentication.getName();
        String authenticationRoles = authentication.getAuthorities().toString();
        String username = userService.findUserFullName(currentUserId);
        boolean hasCustomerRole = authenticationRoles.toLowerCase().contains("Customer".toLowerCase());
        boolean hasSaleRole = authenticationRoles.toLowerCase().contains("Sale".toLowerCase());
        boolean hasAdminRole = authenticationRoles.toLowerCase().contains("Admin".toLowerCase());

        String roles = "";
        if (hasCustomerRole) {
            roles += "Customer";
        }

        if (hasSaleRole) {
            if (roles.isEmpty()) {
                roles = "Sale";
            } else {
                roles += ", Sale";
            }

        }

        if (hasAdminRole) {
            if (roles.isEmpty()) {
                roles = "Admin";
            } else {
                roles += ", Admin";
            }
        }

        model.addAttribute("products", listOfProducts);
        model.addAttribute("username", username);
        model.addAttribute("roles", roles);
        model.addAttribute("hasAdminRole", hasAdminRole);
        model.addAttribute("hasSaleRole", hasSaleRole);
        model.addAttribute("hasCustomerRole", hasCustomerRole);
        listOfProduct = listOfProducts;
        log.info(String.format("HOME PAGE : List Of Product : %s --> %s", listOfProducts.size(), listOfProducts));


        int itemsInCurrentOrder = 0;
        String cartDescription = "Your cart is empty, Let's start shopping :)";
        boolean shouldShowCheckoutButton = false;

        // TO GET ORDER OR CREATE NEW ONE
        if (hasCustomerRole) {
            currentOrder = orderService.findPendingOrderForTheCustomer(currentUserId);
            if (currentOrder != null && !currentOrder.getItems().isEmpty()) {
                log.info(String.format("HOME PAGE : My Order is  : %s --> %s", currentOrder, currentOrder.getItems()));

                for (OrderItem item : currentOrder.getItems()) {
                    itemsInCurrentOrder += item.getQuantity();
                }

                if (itemsInCurrentOrder > 0) {
                    cartDescription = "You have " + itemsInCurrentOrder + " items in the cart.";
                    shouldShowCheckoutButton = true;
                }
            }
        }

        model.addAttribute("cartDescription", cartDescription);
        model.addAttribute("shouldShowCheckoutButton", shouldShowCheckoutButton);
        
        log.info(String.format("HOME PAGE : Current Order is  : %s --> ", currentOrder));
        return "home";
    }

    // Add Request Mapping for /sellers to show sellers.html
    @GetMapping("/sellers")
    public String showSellers() {
        return "sellers";
    }

    // Add Request Mapping for /systems to show systems.html
    @GetMapping("/systems")
    public String showSystems() {
        return "systems";
    }

    @GetMapping("/showProductFormForAdd")
    public String showProductFormForAdd(Model theModel, Authentication authentication) {
        // create model attribute to bind form data
        Product theProduct = new Product();
        String title = "Add A New Product";
        saveMode = SaveMode.ADD;

        theModel.addAttribute("product", theProduct);
        theModel.addAttribute("productFormTitle", title);

        log.info(String.format("PRODUCT-FORM PAGE TO ADD : Product and Title are  : %s --> %s", theProduct, title));
        return "/product-form";
    }

    @GetMapping("/showProductFormForUpdate")
    public String showProductFormForUpdate(@RequestParam("productId") int theId,
                                           Model theModel) {
        // get the product from the service
        Product theProduct = productService.findById(theId);
        String title = "Update Product Info";

        // set product as a model attribute to pre-populate the form
        saveMode = SaveMode.UPDATE;
        theModel.addAttribute("product", theProduct);
        theModel.addAttribute("productFormTitle", title);

        // send over to our form
        log.info(String.format("PRODUCT-FORM PAGE TO UPDATE : Product and Title are  : %s --> %s", theProduct, title));
        return "/product-form";
    }

    @PostMapping("/saveProduct")
    public String saveProduct(@ModelAttribute("product") Product theProduct, BindingResult bindingResult, Authentication authentication) {
        if (bindingResult.hasErrors()) {
            log.error("PRODUCT-FORM PAGE ERROR --> Something wrong while saving the produce!");
            return "/product-form"; // Return to the form with error messages
        } else {
            switch (saveMode) {
                case ADD:
                    theProduct.setCreatedBy(authentication.getName());
                    productService.save(theProduct);
                    log.info(String.format("PRODUCT-FORM PAGE TO HOME : AFTER SUCCESSFULLY SAVE PRODUCT : Product is : %s ", theProduct));
                    break;
                case UPDATE:
                    theProduct.setUpdatedBy(authentication.getName());
                    productService.update(theProduct);
                    log.info(String.format("PRODUCT-FORM PAGE TO HOME : AFTER SUCCESSFULLY UPDATE PRODUCT : Product is : %s ", theProduct));
                    break;
                default:
                    log.error("PRODUCT-FORM PAGE ERROR --> Something wrong while saving the produce!");
                    break;
            }

            // use a redirect to prevent duplicate submissions
            return "redirect:/";
        }
    }

    // Delete Product
    @GetMapping("/deleteProduct")
    public String delete(@RequestParam("productId") int theId) {
        // delete the product
        productService.deleteById(theId);

        // redirect to / --> Home Page
        log.info(String.format("HOME PAGE : AFTER SUCCESSFULLY DELETE PRODUCT : Product ID to delete is : %s ", theId));
        return "redirect:/";
    }

    @GetMapping("/checkOut")
    public String checkOut(Authentication authentication, Model theModel) {
        // TODO: Work on checkOut function
        return "redirect:/";
    }

    @GetMapping("/addToCart")
    public String addItemToCard(@RequestParam("productId") int theProductId, Authentication authentication) {
        Product theProduct = productService.findById(theProductId);

        if (currentOrder != null) {
            if (currentOrder.getItems().isEmpty()) {
                currentOrder = orderService.addNewItemToTheOrder(currentOrder.getId(), theProduct, authentication.getName());
                log.info(String.format("HOME PAGE : AFTER SUCCESSFULLY ADD TO CART : Current Order  is : %s --> ", currentOrder));
            } else {
                boolean isTheNewItem = true;

                OrderItem orderItemToUpdate = new OrderItem();

                for (OrderItem item : currentOrder.getItems()) {
                    if (item.getProductId() == theProduct.getId()) {
                        isTheNewItem = false;
                        orderItemToUpdate = item;
                        break;
                    }
                }

                if (isTheNewItem) {
                    currentOrder = orderService.addNewItemToTheOrder(currentOrder.getId(), theProduct, authentication.getName());
                    log.info(String.format("HOME PAGE : AFTER SUCCESSFULLY ADD TO CART : Current Order  is : %s --> ", currentOrder));
                } else {
                    int newQuantity = orderItemToUpdate.getQuantity() + 1;
                    double newSubtotal = newQuantity * theProduct.getPrice();
                    orderItemToUpdate.setQuantity(newQuantity);
                    orderItemToUpdate.setSubtotal(newSubtotal);
                    currentOrder = orderService.updateOrderWithItem(currentOrder, orderItemToUpdate, authentication.getName());
                    log.info(String.format("HOME PAGE : AFTER SUCCESSFULLY ADD TO CART : Current Order  is : %s --> ", currentOrder));
                }
            }
        } else { // Create new Order and add the first item, if current order is null
            Order savedOrder = orderService.addNewOrder(new Order(), authentication.getName());
            currentOrder = orderService.addNewItemToTheOrder(savedOrder.getId(), theProduct, authentication.getName());
            log.info(String.format("HOME PAGE : AFTER SUCCESSFULLY ADD TO CART : Current Order  is : %s --> ", currentOrder));
        }

        return "redirect:/";
    }
}
