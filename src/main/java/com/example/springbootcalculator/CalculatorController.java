package com.example.springbootcalculator;

import org.springframework.web.bind.annotation.*;

@RestController
public class CalculatorController {

    @GetMapping("/calculate")
    public String calculate(@RequestParam String expression) {

        try {

            Calculator calc = new Calculator();

            calc.setExpression(expression);

            calc.calculate();

            return String.valueOf(calc.getResult());

        } catch (Exception e) {

            return "Error: " + e.getMessage();
        }
    }
}