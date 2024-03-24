package com.nikguscode.calculator.calculator;

import com.nikguscode.calculator.calculator.model.DateTypeChecker;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
public class ApiConnectionTest {
    static List<LocalDate> holidaysList = new ArrayList<>(
            Arrays.asList(
                    LocalDate.of(2024, 5, 9),
                    LocalDate.of(2024, 5, 1),
                    LocalDate.of(2024, 1, 1)
            )
    );

    @Test
    void testConnection() {
        RestTemplate restTemplate = new RestTemplate();

        for (int i = 0; i < 3; i++) {
            int day = holidaysList.get(i).getDayOfMonth();
            int month = holidaysList.get(i).getMonthValue();
            int year = holidaysList.get(i).getYear();

            String apiUrl = UriComponentsBuilder.fromUriString("https://isdayoff.ru/api/getdata")
                    .queryParam("day", day)
                    .queryParam("month", month)
                    .queryParam("year", year)
                    .build()
                    .toUriString();
            ResponseEntity<String> response = restTemplate.getForEntity(apiUrl, String.class);
            Assertions.assertEquals("1", response.getBody());

            log.info("Date: {}/{}/{}. Response code: {}", day, month, year, response.getBody());
        }
    }
}
