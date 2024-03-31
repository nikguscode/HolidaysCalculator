package com.nikguscode.calculator.calculator;

import static com.nikguscode.calculator.calculator.model.RequestStatus.*;

import com.nikguscode.calculator.calculator.model.DateTypeChecker;
import com.nikguscode.calculator.calculator.model.PayoutCalculator;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@Slf4j
@SpringBootTest
public class CalculatorTest {
    private final PayoutCalculator calculator;

    @Autowired
    public CalculatorTest(PayoutCalculator calculator) {
        this.calculator = calculator;
    }

    static List<LocalDate> holidaysList = new ArrayList<>(
            Arrays.asList(
                    LocalDate.of(2024, 5, 9),
                    LocalDate.of(2024, 5, 1),
                    LocalDate.of(2024, 1, 1)
            )
    );

    @Test
    @DisplayName("DateType")
    void isPayoutDay() {
        for (int i = 0; i < 3; i++) {
            int day = holidaysList.get(i).getDayOfMonth();
            int month = holidaysList.get(i).getMonthValue();
            int year = holidaysList.get(i).getYear();

            Assertions.assertEquals(NOT_PAYOUT_DAY, DateTypeChecker.isWorkingDay(holidaysList.get(i)));
            log.info(
                    "Date: {}/{}/{}. Response code: {}",
                    day,
                    month,
                    year,
                    DateTypeChecker.isWorkingDay(holidaysList.get(i))
            );
        }
    }

    @Test
    @DisplayName("Calculate")
    void testCalculation() {
        double salary = 100;
        int holidayDuration = 10;

        List<LocalDate> date = new ArrayList<>(List.of(
                LocalDate.of(2024, 5, 1),
                LocalDate.of(2024, 1, 1),
                LocalDate.of(2024, 6, 21),
                LocalDate.of(2024, 11, 1),
                LocalDate.of(2024, 8, 21)
        ));

        for (LocalDate localDate : date) {
            String calculateResult = calculator.calculate(
                    salary,
                    holidayDuration,
                    localDate.getDayOfMonth(),
                    localDate.getMonthValue(),
                    localDate.getYear()
            );

            log.info("Payout: {}", calculateResult);
        }
    }
}
