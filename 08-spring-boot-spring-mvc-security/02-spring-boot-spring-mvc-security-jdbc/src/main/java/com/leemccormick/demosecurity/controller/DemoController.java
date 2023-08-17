package com.leemccormick.demosecurity.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DemoController {
    @GetMapping("/")
    public String showHome() {
        return "home";
    }

    // Add Request Mapping for /leaders to show leaders.html
    @GetMapping("/leaders")
    public String showLeaders() {
        return "leaders";
    }

    // Add Request Mapping for /systems to show systems.html
    @GetMapping("/systems")
    public String showSystems() {
        return "systems";
    }
}
