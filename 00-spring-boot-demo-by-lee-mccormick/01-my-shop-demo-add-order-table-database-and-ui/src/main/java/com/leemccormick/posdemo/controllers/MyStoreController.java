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
      //  String userId = authenticationUsername.substring(0, 1).toUpperCase() + authenticationUsername.substring(1);
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
        log.info(String.format("List Of Product : %s --> %s", listOfProducts.size(), listOfProducts));


        int itemsInCurrentOrder = 0;
        String cartDescription = "Your cart is empty, Let's start shopping :)";
        boolean shouldShowCheckoutButton = false;

        // TO GET ORDER OR CREATE NEW ONE
        if (hasCustomerRole) {
            currentOrder = orderService.findPendingOrderForTheCustomer(currentUserId);
            if (currentOrder != null) {
                log.info(String.format("My Order is  : %s --> %s", currentOrder, currentOrder.getItems()));

                for (OrderItem item : currentOrder.getItems()) {
                    itemsInCurrentOrder += item.getQuantity();
                }

                if (itemsInCurrentOrder > 0) {
                    cartDescription = "You have " + itemsInCurrentOrder + " items in the cart.";
                    shouldShowCheckoutButton = true;
                }
            //    itemsInCurrentOrder = currentOrder.getItems().size();
            }
        }

        model.addAttribute("cartDescription", cartDescription);
        model.addAttribute("shouldShowCheckoutButton", shouldShowCheckoutButton);
       // boolean shouldShowCheckOutButton =
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
        return "/product-form";
    }

    @PostMapping("/saveProduct")
    public String saveProduct(@ModelAttribute("product") Product theProduct, BindingResult bindingResult, Authentication authentication) {
        if (bindingResult.hasErrors()) {
            return "/product-form"; // Return to the form with error messages
        } else {
            switch (saveMode) {
                case ADD:
                    theProduct.setCreatedBy(authentication.getName());
                    productService.save(theProduct);
                    break;
                case UPDATE:
                    theProduct.setUpdatedBy(authentication.getName());
                    productService.update(theProduct);
                    break;
                default:
                    log.error("Something wrong while saving the produce!");
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
        return "redirect:/";
    }

//    /*
//        (1, 'Pending'),
//    (2, 'Processing'),
//    (3, 'Shipped'),
//    (4, 'Delivered'),
//    (5, 'Cancelled');
//    */
    @GetMapping("/checkOut")
    public String checkOut(Authentication authentication, Model theModel) {
        // create model attribute to bind form data
//        Order theOrder = new Order();
//        theOrder.setCustomerId(authentication.getName());
//        theOrder.setTotalAmount(20.0);
//        theOrder.setStatus("Pending");
//        theOrder.setCreatedDateTime(new Date());
//        theOrder.setUpdatedDateTime(new Date());
//        theOrder.setCreatedBy(authentication.getName());
//        theOrder.setUpdatedBy(authentication.getName());


        Product theProductForTempOrderItem1 = listOfProduct.get(0);
        Product theProductForTempOrderItem2 = listOfProduct.get(2);
        Product theProductForTempOrderItem3 = listOfProduct.get(3);

      //  addItemToOrder(theOrder, theProductForTempOrderItem1, 2);
//        addItemToOrder(theOrder, theProductForTempOrderItem2, 1);
//        addItemToOrder(theOrder, theProductForTempOrderItem3, 3);


     //   orderService.addNewOrder(theOrder);
//        orderService.addNewOrderTest(theOrder);
        addItemToOrderById(5,theProductForTempOrderItem1,2);
        log.info(String.format("Finished adding item to  theProductForTempOrderItem1: %s --> ", theProductForTempOrderItem3));

        addItemToOrderById(5,theProductForTempOrderItem2,1);
        log.info(String.format("Finished adding item to  theProductForTempOrderItem2: %s --> ", theProductForTempOrderItem2));

        addItemToOrderById(5,theProductForTempOrderItem3,3);
        log.info(String.format("Finished adding item to  theProductForTempOrderItem3: %s --> ", theProductForTempOrderItem3));

//        theModel.addAttribute("order", theOrder);
//        log.info(String.format("New Order : %s --> ", theOrder));
     //   log.info(String.format("Finished adding item to  theProductForTempOrderItem1: %s --> ", theProductForTempOrderItem3));
        return "redirect:/";
    }


    @GetMapping("/addToCart")
    public String addItemToCard(@RequestParam("productId") int theProductId,
                                Model theModel) {

        Product theProduct = productService.findById(theProductId);

        if (currentOrder != null) {
            if (currentOrder.getItems().isEmpty()) {
                // Add new items to currentOrder
                OrderItem newItem = new OrderItem();
                addItemToCurrentOrder(theProduct, 1);

            } else {
                boolean isTheNewItem = true;
                for (OrderItem item : currentOrder.getItems()) {
                    if (item.getProductId() == theProductId) {
                        isTheNewItem = false;
                        item.setQuantity(item.getQuantity() + 1);
                        // Function to update orderItem;
                        break;
                    }
                }

                if (isTheNewItem) {
                    // Add new items to currentOrder
                    addItemToCurrentOrder(theProduct, 1);
                }
            }
        } else {
            // Create new Order and add the first item.
        }
      ///  String title = "Update Product Info";

        return "redirect:/";
    }

    private void addItemToCurrentOrder(Product product, int quantity) {
        OrderItem tempOrderItem = new OrderItem();

        tempOrderItem.setOrderId(currentOrder.getId());
        tempOrderItem.setProductId(product.getId());
        tempOrderItem.setQuantity(quantity);

        double subtotalForItems = product.getPrice() * quantity;
        tempOrderItem.setSubtotal(subtotalForItems);

        //  theOrder.addItem(tempOrderItem);

        orderService.addNewItemToTheOrder(currentOrder.getId(), tempOrderItem);
    }
    private void addItemToOrder(Order theOrder, Product product, int quantity) {
        OrderItem tempOrderItem = new OrderItem();

        tempOrderItem.setOrderId(theOrder.getId());
        tempOrderItem.setProductId(product.getId());
        tempOrderItem.setQuantity(quantity);

        double subtotalForItems = product.getPrice() * quantity;
        tempOrderItem.setSubtotal(subtotalForItems);

        theOrder.addItem(tempOrderItem);
    }


    private void addItemToOrderById(int theOrderId, Product product, int quantity) {
        OrderItem tempOrderItem = new OrderItem();

        tempOrderItem.setOrderId(theOrderId);
        tempOrderItem.setProductId(product.getId());
        tempOrderItem.setQuantity(quantity);

        double subtotalForItems = product.getPrice() * quantity;
        tempOrderItem.setSubtotal(subtotalForItems);

      //  theOrder.addItem(tempOrderItem);

        orderService.addNewItemToTheOrder(theOrderId, tempOrderItem);
    }
}
