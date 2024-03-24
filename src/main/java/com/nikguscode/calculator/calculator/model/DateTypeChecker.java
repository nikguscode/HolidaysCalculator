package com.nikguscode.calculator.calculator.model;

import static com.nikguscode.calculator.calculator.model.RequestStatus.*;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Optional;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
@Slf4j
public class DateTypeChecker {
    public static RequestStatus isWorkingDay(LocalDate date) {
        return connectToExternalApi(date);
    }

    private static RequestStatus connectToExternalApi(LocalDate date) {
        RestTemplate restTemplate = new RestTemplate();
        String apiUrl = UriComponentsBuilder.fromUriString("https://isdayoff.ru/api/getdata")
                .queryParam("day", date.getDayOfMonth())
                .queryParam("month", date.getMonthValue())
                .queryParam("year", date.getYear())
                .build()
                .toUriString();
        try {
            ResponseEntity<String> response = restTemplate.getForEntity(apiUrl, String.class);
            return handleRequest(response, date);
        } catch (HttpClientErrorException.BadRequest e) {
            log.warn("Ошибка при передаче даты, рассчёт с данными из DateStorage для: {}", date);
            return checkWeekday(date);
        }
    }

    private static RequestStatus handleRequest(ResponseEntity<String> response, LocalDate date) {
        HttpStatus httpStatus = (HttpStatus) response.getStatusCode();

        switch (httpStatus) {
            case OK:

                switch (Optional.ofNullable(response.getBody()).orElse("API_ERROR")) {
                    case ("0"):
                    case ("2"):
                    case ("4"):
                        return WORKING_DAY;
                    case ("1"):
                        return HOLIDAY_DAY;
                }

            case BAD_REQUEST:
                log.warn(response.getBody() != null && response.getBody().equals("100")
                        ? "Ошибка при передаче даты, рассчёт с данными из DateStorage"
                        : "API ERROR, рассчёт с данными из DateStorage");
                return checkWeekday(date);
            default:
                log.warn("Необрабатываемый HTTP код, рассчёт с данными из DateStorage");
                return checkWeekday(date);
        }
    }

    private static RequestStatus checkWeekday(LocalDate date) {
        DayOfWeek dayOfWeek = date.getDayOfWeek();
        return DateStorage.holidaysMap.get() DataStorage dayOfWeek.equals(DayOfWeek.SATURDAY) || dayOfWeek.equals(DayOfWeek.SUNDAY)
                ? HOLIDAY_DAY
                : WORKING_DAY;
    }
}
