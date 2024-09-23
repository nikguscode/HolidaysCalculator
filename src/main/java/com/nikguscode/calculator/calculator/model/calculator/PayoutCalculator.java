package com.nikguscode.calculator.calculator.model.calculator;

import com.nikguscode.calculator.calculator.exception.InvalidValueException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

/**
 * Данный интерфейс предназначен для абстракции при рассчёте отпускных дней, содержит единстенную реализацию:
 * {@link LocalPayoutCalculator LocalPayoutCalculator}
 */
@Service
public interface PayoutCalculator {
    long calculate(double salary, int vacationDuration) throws InvalidValueException;
    long calculate(double salary, int vacationDuration, LocalDate startVacationDate) throws InvalidValueException;
}
