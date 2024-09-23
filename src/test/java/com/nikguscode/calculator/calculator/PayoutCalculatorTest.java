package com.nikguscode.calculator.calculator;

import com.nikguscode.calculator.calculator.exception.InvalidValueException;
import com.nikguscode.calculator.calculator.model.calculator.PayoutCalculator;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

/**
 * {@link PayoutCalculatorTest#test1()}, {@link PayoutCalculatorTest#test2()}: проверяют стратегию без учёта праздничных дней <br/>
 * {@link PayoutCalculatorTest#test3()}, {@link PayoutCalculatorTest#test4()}: проверяют стратегию с учётом праздничных дней
 */
@Log4j2
@SpringBootTest
public class PayoutCalculatorTest {
    PayoutCalculator calculator;

    public PayoutCalculatorTest(@Qualifier("localPayoutCalculator") PayoutCalculator calculator) {
        this.calculator = calculator;
    }

    @Test
    @DisplayName("Correct values | Duration only")
    void test1() throws InvalidValueException {
        double salary = 25000.0;
        int vacationDuration = 5;
        Assertions.assertEquals(4266, calculator.calculate(salary, vacationDuration));
    }

    @Test
    @DisplayName("Incorrect values | Duration only")
    void test2() {
        double salary = 0;
        int vacationDuration = 5;
        Assertions.assertThrows(InvalidValueException.class, () -> calculator.calculate(salary, vacationDuration));
    }

    @Test
    @DisplayName("Correct values | Duration and start date")
    void test3() throws InvalidValueException {
        double salary = 50000.0;
        int vacationDuration = 9;
        LocalDate startVacationDate = LocalDate.of(2025, 1, 1);
        Assertions.assertEquals(1706, calculator.calculate(salary, vacationDuration, startVacationDate));
    }

    @Test
    @DisplayName("Incorrect values | Duration and start date")
    void test4() {
        double salary = 50000.0;
        int vacationDuration = -2;
        LocalDate startVacationDate = LocalDate.of(2025, 1, 1);
        Assertions.assertThrows(InvalidValueException.class, () -> calculator.calculate(salary, vacationDuration, startVacationDate));
    }
}
