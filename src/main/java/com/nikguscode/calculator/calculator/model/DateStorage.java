package com.nikguscode.calculator.calculator.model;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class DateStorage {
    public static Map<String, String> holidaysMap;

    static {
        holidaysMap = new HashMap<>();
        holidaysMap.put("09/05", "holiday");
        holidaysMap.put("01/01", "holiday");
        holidaysMap.put("02/01", "holiday");
        holidaysMap.put("03/01", "holiday");
        holidaysMap.put("04/01", "holiday");
        holidaysMap.put("05/01", "holiday");
        holidaysMap.put("06/01", "holiday");
        holidaysMap.put("07/01", "holiday");
        holidaysMap.put("08/01", "holiday");
        holidaysMap.put("01/05", "holiday");
        holidaysMap.put("12/06", "holiday");
        holidaysMap.put("04/11", "holiday");
        holidaysMap.put("23/02", "holiday");
        holidaysMap.put("08/03", "holiday");
    }
}
