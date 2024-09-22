package com.nikguscode.calculator.calculator.model.calculator;

import com.nikguscode.calculator.calculator.exception.InvalidValueException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public interface PayoutCalculator {
    long calculate(double salary, int vacationDuration) throws InvalidValueException;
    long calculate(double salary, int vacationDuration, LocalDate startVacationDate) throws InvalidValueException;
}
