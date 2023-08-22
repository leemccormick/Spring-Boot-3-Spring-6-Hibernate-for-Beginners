package com.leemccormick.posdemo.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MyStoreController {
    @GetMapping("/")
    public String showHome() {
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
}
