package com.nikguscode.calculator.calculator.model;


import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * Хранилище дат праздников, содержит единственный метод {@link HolidaysDateStorage#isPayableDay(LocalDate) isPayableDay()}
 * для обращения к {@link HolidaysDateStorage#holidaysSet holidaySet}
 */
@Service
public final class HolidaysDateStorage {
    private static final Set<String> holidaysSet = new HashSet<>();
    private HolidaysDateStorage() {}

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

    /**
     * Метод, проверяющий, есть ли дата в {@link HolidaysDateStorage#holidaysSet holidaysSet}
     *
     * @param date дата, которую необходимо проверить
     * @return true если дата не содержится в {@link HolidaysDateStorage#holidaysSet holidaysSet}, иначе false
     */
    public static boolean isPayableDay(LocalDate date) {
        String holidayKey = date.getDayOfMonth() + "/" + date.getMonthValue();
        return !holidaysSet.contains(holidayKey);
    }
}