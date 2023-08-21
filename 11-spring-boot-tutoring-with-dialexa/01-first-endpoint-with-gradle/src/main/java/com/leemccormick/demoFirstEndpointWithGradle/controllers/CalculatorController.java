package com.leemccormick.demoFirstEndpointWithGradle.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/calculator")
public class CalculatorController {
    @GetMapping()
    public String calculatorWelcome() {
        return "Welcome to the calculator controller";
    }

    @GetMapping("/plus")
    public String calculatorPlus() {
        return "Welcome to plus+++";
    }

    @GetMapping("/subtract")
    public String calculatorSubtract() {
        return "Welcome to subtract---";
    }
}
