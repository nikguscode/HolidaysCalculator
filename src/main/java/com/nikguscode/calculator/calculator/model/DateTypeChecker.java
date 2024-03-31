package com.nikguscode.calculator.calculator.model;

import static com.nikguscode.calculator.calculator.model.DateType.*;

import java.time.LocalDate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class DateTypeChecker {
    public static DateType isWorkingDay(LocalDate date) {
        return checkWeekday(date);
    }

    private static DateType checkWeekday(LocalDate date) {
        return DateStorage.isWorkingDay(date).equals(NOT_PAYOUT_DAY)
                ? NOT_PAYOUT_DAY
                : PAYOUT_DAY;
    }
}
