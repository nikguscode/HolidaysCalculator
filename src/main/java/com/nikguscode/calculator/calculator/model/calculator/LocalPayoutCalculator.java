package com.nikguscode.calculator.calculator.model.calculator;

import com.nikguscode.calculator.calculator.exception.InvalidValueException;
import com.nikguscode.calculator.calculator.model.HolidaysDateStorage;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.stream.Stream;

@Service
public class LocalPayoutCalculator implements PayoutCalculator {
    public static final double DAYS_PER_MONTH = 29.3;

    @Override
    public long calculate(double salary, int vacationDuration) throws InvalidValueException {
        validateValues(salary, vacationDuration);
        return Math.round(calculateDailySalary(salary) * vacationDuration);
    }

    @Override
    public long calculate(double salary, int vacationDuration, LocalDate startVacationDate) throws InvalidValueException {
        validateValues(salary, vacationDuration);
        return Math.round(calculateDailySalary(salary) * countPayoutDays(vacationDuration, startVacationDate));
    }

    private void validateValues(double salary, int vacationDuration) throws InvalidValueException {
        if (salary <= 0) {
            throw new InvalidValueException(salary);
        } else if (vacationDuration <= 0) {
            throw new InvalidValueException(vacationDuration);
        }
    }

    private double calculateDailySalary(double salary) {
        return salary / DAYS_PER_MONTH;
    }

    private long countPayoutDays(int vacationDuration, LocalDate startVacationDate) {
        return Stream.iterate(0, n -> n + 1)
                .limit(vacationDuration)
                .filter(day -> HolidaysDateStorage.isPayableDay(startVacationDate.plusDays(day)))
                .count();
    }
}