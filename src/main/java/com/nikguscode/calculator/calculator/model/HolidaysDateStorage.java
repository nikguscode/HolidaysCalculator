package com.nikguscode.calculator.calculator.model;


import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Service;

@Service
public final class HolidaysDateStorage {
    private HolidaysDateStorage() {}

    private static final Set<String> holidaysSet = new HashSet<>();

    static {
        holidaysSet.add("9/5");
        holidaysSet.add("1/1");
        holidaysSet.add("2/1");
        holidaysSet.add("3/1");
        holidaysSet.add("4/1");
        holidaysSet.add("5/1");
        holidaysSet.add("6/1");
        holidaysSet.add("7/1");
        holidaysSet.add("8/1");
        holidaysSet.add("1/5");
        holidaysSet.add("12/6");
        holidaysSet.add("4/11");
        holidaysSet.add("23/2");
        holidaysSet.add("8/3");
    }

    public static boolean isPayableDay(LocalDate date) {
        String holidayKey = date.getDayOfMonth() + "/" + date.getMonthValue();
        return !holidaysSet.contains(holidayKey);
    }
}