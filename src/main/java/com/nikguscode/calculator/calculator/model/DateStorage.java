package com.nikguscode.calculator.calculator.model;

import static com.nikguscode.calculator.calculator.model.DateType.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class DateStorage {
    public static Map<String, String> holidaysMap;

    static {
        holidaysMap = new HashMap<>();
        holidaysMap.put("9/5", "holiday");
        holidaysMap.put("1/1", "holiday");
        holidaysMap.put("2/1", "holiday");
        holidaysMap.put("3/1", "holiday");
        holidaysMap.put("4/1", "holiday");
        holidaysMap.put("5/1", "holiday");
        holidaysMap.put("6/1", "holiday");
        holidaysMap.put("7/1", "holiday");
        holidaysMap.put("8/1", "holiday");
        holidaysMap.put("1/5", "holiday");
        holidaysMap.put("12/6", "holiday");
        holidaysMap.put("4/11", "holiday");
        holidaysMap.put("23/2", "holiday");
        holidaysMap.put("8/3", "holiday");
    }

    public static DateType isWorkingDay(LocalDate date) {
        String query = date.getDayOfMonth() + "/" + date.getMonthValue();

        return holidaysMap.get(query) == null ? PAYOUT_DAY : NOT_PAYOUT_DAY;
    }
}
