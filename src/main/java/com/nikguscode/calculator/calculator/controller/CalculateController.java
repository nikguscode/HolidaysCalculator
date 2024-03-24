package com.nikguscode.calculator.calculator.controller;

import com.nikguscode.calculator.calculator.model.PayoutCalculator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
public class CalculateController {
    private final PayoutCalculator payoutCalculator;

    @Autowired
    public CalculateController(PayoutCalculator payoutCalculator) {
        this.payoutCalculator = payoutCalculator;
    }

//    @GetMapping("/calculate")
//    private String getData(@RequestParam double salary, @RequestParam(name = "duration") int holidayDuration) {
//        return payoutCalculator.calculate(salary, holidayDuration);
//    }

    @GetMapping("/calculate")
    private String getData(@RequestParam double salary, @RequestParam(name = "duration") int holidayDuration,
                           @RequestParam int date, @RequestParam int month, @RequestParam int year) {
        return payoutCalculator.calculate(salary, holidayDuration, date, month, year);
    }
}
