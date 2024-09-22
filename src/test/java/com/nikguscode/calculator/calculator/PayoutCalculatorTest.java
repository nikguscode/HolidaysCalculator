package com.nikguscode.calculator.calculator;

import com.nikguscode.calculator.calculator.exception.InvalidValueException;
import com.nikguscode.calculator.calculator.model.HolidaysDateStorage;
import com.nikguscode.calculator.calculator.model.calculator.PayoutCalculator;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.util.Map.entry;

@Log4j2
@SpringBootTest
public class PayoutCalculatorTest {
    static Map<LocalDate, Boolean> holidaysList = Map.ofEntries(
            entry(LocalDate.of(2024, 5, 9), false), // holiday
            entry(LocalDate.of(2024, 5, 2), true) // payout day
    );
    static List<Integer> vacationDurationList = new ArrayList<>(
            List.of(
                    6
            )
    );
    static List<Double> salaryList = new ArrayList<>();

    static {
        salaryList.add(50000.0);
        salaryList.add(120000.0);
    }

    PayoutCalculator calculator;

    public PayoutCalculatorTest(@Qualifier("localPayoutCalculator") PayoutCalculator calculator) {
        this.calculator = calculator;
    }

    @Test
    @DisplayName("Payout day test")
    void isPayoutDay() {
        log.info("TRUE: Payable, FALSE: Non-payable");

        for (Map.Entry<LocalDate, Boolean> date : holidaysList.entrySet()) {
            Assertions.assertEquals(date.getValue(), HolidaysDateStorage.isPayableDay(date.getKey()));
            log.info(
                    "Date: {}, Excepted value: {}, Actual value: {}",
                    date.getKey(),
                    date.getValue(),
                    HolidaysDateStorage.isPayableDay(date.getKey())
            );
        }
    }


    @Test
    @DisplayName("Calculate without date")
    void calculateWithoutDate() throws InvalidValueException {
        Assertions.assertEquals(10239, calculator.calculate(salaryList.get(0), vacationDurationList.get(0)));
        log.info("Excepted: {}, actual: {}", 10239, calculator.calculate(
                        salaryList.get(0),
                        vacationDurationList.get(0)
                )
        );
    }

    void calculateWithOnlyStartDate() {

    }

    void calculateWithAllDate() {

    }
}
