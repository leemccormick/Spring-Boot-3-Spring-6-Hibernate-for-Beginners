package com.leemccormick.lombokloggingdebugging.services;

import com.leemccormick.lombokloggingdebugging.models.Numbers;
import com.leemccormick.lombokloggingdebugging.models.NumbersLombok;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j // @Slf4j --> for using log.info() --> Need to add dependency
public class CalculatorService {
    public int plusIntNumbers(Numbers numbers) {
        log.info(String.format("Attempting to plus %s by %s without lombok", numbers.getA(), numbers.getB()));
        return numbers.getA() + numbers.getB();
    }

    public int subtractIntNumbers(Numbers numbers) {
        log.info(String.format("Attempting to subtract %s by %s without lombok", numbers.getA(), numbers.getB()));
        return numbers.getA() - numbers.getB();
    }

    public int divideIntNumbers(Numbers numbers) {
        log.info(String.format("Attempting to divide %s by %s without lombok", numbers.getA(), numbers.getB()));
        return numbers.getA() / numbers.getB();
    }

    public int multiplyIntNumbers(Numbers numbers) {
        log.info(String.format("Attempting to multiply %s by %s without lombok", numbers.getA(), numbers.getB()));
        return numbers.getA() * numbers.getB();
    }

    public int plusIntNumbersLombok(NumbersLombok numbersLombok) {
        log.info(String.format("Attempting to plus %s by %s with lombok", numbersLombok.getA(), numbersLombok.getB()));
        return numbersLombok.getA() + numbersLombok.getB();
    }

    public int subtractIntNumbersLombok(NumbersLombok numbersLombok) {
        log.info(String.format("Attempting to subtract %s by %s with lombok", numbersLombok.getA(), numbersLombok.getB()));
        return numbersLombok.getA() - numbersLombok.getB();
    }

    public int divideIntNumbersLombok(NumbersLombok numbersLombok) {
        log.info(String.format("Attempting to divide %s by %s with lombok", numbersLombok.getA(), numbersLombok.getB()));
        return numbersLombok.getA() / numbersLombok.getB();
    }

    public int multiplyIntNumbersLombok(NumbersLombok numbersLombok) {
        log.info(String.format("Attempting to multiply %s by %s with lombok", numbersLombok.getA(), numbersLombok.getB()));
        return numbersLombok.getA() * numbersLombok.getB();
    }
}
