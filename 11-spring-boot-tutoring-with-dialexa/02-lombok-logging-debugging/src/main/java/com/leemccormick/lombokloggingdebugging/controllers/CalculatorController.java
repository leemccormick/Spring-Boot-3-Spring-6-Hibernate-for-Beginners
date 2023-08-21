package com.leemccormick.lombokloggingdebugging.controllers;

import com.leemccormick.lombokloggingdebugging.models.Numbers;
import com.leemccormick.lombokloggingdebugging.models.NumbersLombok;
import com.leemccormick.lombokloggingdebugging.services.CalculatorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/calculator")
@Slf4j
public class CalculatorController {

    @Autowired
    private CalculatorService calculatorService;

    @GetMapping()
    public String calculatorWelcome() {
        return "Welcome to the calculator controller";
    }

    @PostMapping("/plusints")
    public int plusInt(@RequestBody Numbers numbers) {
        log.info(String.format("Received request to plus %s by %s without lombok", numbers.getA(), numbers.getB()));

        int result = 0;

        try {
            result = calculatorService.plusIntNumbers(numbers);
        } catch (Exception ex) {
            log.error(String.format("An error occurred: %s", ex));
            throw ex;
        }

        return result;
    }

    @PostMapping("/subtractints")
    public int subtractInt(@RequestBody Numbers numbers) {
        log.info(String.format("Received request to subtract %s by %s without lombok", numbers.getA(), numbers.getB()));

        int result = 0;

        try {
            result = calculatorService.subtractIntNumbers(numbers);
        } catch (Exception ex) {
            log.error(String.format("An error occurred: %s", ex));
            throw ex;
        }

        return result;
    }

    @PostMapping("/divideints")
    public int divideInt(@RequestBody Numbers numbers) {
        log.info(String.format("Received request to divide %s by %s without lombok", numbers.getA(), numbers.getB()));

        int result = 0;

        try {
            result = calculatorService.divideIntNumbers(numbers);
        } catch (Exception ex) {
            log.error(String.format("An error occurred: %s", ex));
            throw ex;
        }

        return result;
    }

    @PostMapping("/multiplyints")
    public int multiplyInt(@RequestBody Numbers numbers) {
        log.info(String.format("Received request to  multiply %s by %s without lombok", numbers.getA(), numbers.getB()));

        int result = 0;

        try {
            result = calculatorService.multiplyIntNumbers(numbers);
        } catch (Exception ex) {
            log.error(String.format("An error occurred: %s", ex));
            throw ex;
        }

        return result;
    }

    @PostMapping("/plusintslombok")
    public int plusIntLombok(@RequestBody NumbersLombok numbersLombok) {
        log.info(String.format("Received request to plus %s by %s with lombok", numbersLombok.getA(), numbersLombok.getB()));

        int result = 0;

        try {
            result = calculatorService.plusIntNumbersLombok(numbersLombok);
        } catch (Exception ex) {
            log.error(String.format("An error occurred: %s", ex));
            throw ex;
        }

        return result;
    }

    @PostMapping("/subtractintslombok")
    public int subtractIntLombok(@RequestBody NumbersLombok numbersLombok) {
        log.info(String.format("Received request to subtract %s by %s with lombok", numbersLombok.getA(), numbersLombok.getB()));

        int result = 0;

        try {
            result = calculatorService.subtractIntNumbersLombok(numbersLombok);
        } catch (Exception ex) {
            log.error(String.format("An error occurred: %s", ex));
            throw ex;
        }

        return result;
    }

    @PostMapping("/divideintslombok")
    public int divideIntLombok(@RequestBody NumbersLombok numbersLombok) {
        log.info(String.format("Received request to divide %s by %s with lombok", numbersLombok.getA(), numbersLombok.getB()));

        int result = 0;

        try {
            result = calculatorService.divideIntNumbersLombok(numbersLombok);
        } catch (Exception ex) {
            log.error(String.format("An error occurred: %s", ex));
            throw ex;
        }

        return result;
    }

    @PostMapping("/multiplyintslombok")
    public int multiplyIntLombok(@RequestBody NumbersLombok numbersLombok) {
        log.info(String.format("Received request to  multiply %s by %s with lombok", numbersLombok.getA(), numbersLombok.getB()));

        int result = 0;

        try {
            result = calculatorService.multiplyIntNumbersLombok(numbersLombok);
        } catch (Exception ex) {
            log.error(String.format("An error occurred: %s", ex));
            throw ex;
        }

        return result;
    }
}