package com.leemccormick.posdemo.controllers;

import com.leemccormick.posdemo.entity.Product;
import com.leemccormick.posdemo.service.ProductService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class MyStoreController {

    private ProductService productService;

    public MyStoreController(ProductService theProductService) {
        productService = theProductService;
    }

    @GetMapping("/")
    public String showHome(Authentication authentication, Model model) {
        List<Product> listOfProducts = productService.findAllProduct();
        System.out.println("listOfProducts" + listOfProducts);
        model.addAttribute("products", listOfProducts);

        String authenticationUsername = authentication.getName();
        String authenticationRoles = authentication.getAuthorities().toString();
        String username = authenticationUsername.substring(0, 1).toUpperCase() + authenticationUsername.substring(1);

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

        model.addAttribute("username", username);
        model.addAttribute("roles", roles);
        model.addAttribute("hasAdminRole", hasAdminRole);
        model.addAttribute("hasSaleRole", hasSaleRole);
        model.addAttribute("hasCustomerRole", hasCustomerRole);

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

    // Delete Product
    @GetMapping("/deleteProduct")
    public String delete(@RequestParam("productId") int theId) {

        // delete the product
        productService.deleteById(theId);

        // redirect to / --> Home Page
        return "redirect:/";

    }
}
