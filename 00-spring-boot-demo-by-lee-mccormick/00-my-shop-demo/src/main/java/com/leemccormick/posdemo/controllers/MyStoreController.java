package com.leemccormick.posdemo.controllers;

import com.leemccormick.posdemo.entity.Product;
import com.leemccormick.posdemo.service.ProductService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
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

    @GetMapping("/showProductFormForAdd")
    public String showProductFormForAdd(Model theModel) {

        // create model attribute to bind form data
        Product theProduct = new Product();
        String title = "Add A New Product";
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
        theModel.addAttribute("product", theProduct);
        theModel.addAttribute("productFormTitle", title);

        // send over to our form
        return "/product-form";
    }

    @PostMapping("/saveProduct")
    public String saveProduct(@ModelAttribute("product") Product theProduct, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "/product-form"; // Return to the form with error messages
        } else {
            // save the product
            productService.save(theProduct);
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
}
