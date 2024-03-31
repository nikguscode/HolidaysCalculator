package com.nikguscode.calculator.calculator.model;

import static com.nikguscode.calculator.calculator.model.RequestStatus.*;

import java.text.DecimalFormat;
import java.time.LocalDate;
import org.springframework.stereotype.Service;

@Service
public class PayoutCalculator {
    public String calculate(double salary, int holidayDuration) {
        return roundValue(salary * holidayDuration);
    }

    public String calculate(double salary, int holidayDuration, int date, int month, int year) {
        double result = salary * getWorkingDays(holidayDuration, date, month, year);
        return roundValue(result);
    }

    private String roundValue(double result) {
        return new DecimalFormat("#.##").format(result);
    }

    private int getWorkingDays(int holidayDuration, int date, int month, int year) {
        int workingDays = 0;
        LocalDate localDate = LocalDate.of(year, month, date);

        for (int currentDay = 0; currentDay < holidayDuration; currentDay++) {
            if (DateTypeChecker.isWorkingDay(localDate).equals(PAYOUT_DAY)) {
                workingDays++;
            }

            localDate = localDate.plusDays(1);
        }

        return workingDays;
    }
}
