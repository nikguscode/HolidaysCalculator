[1mdiff --git a/src/main/java/com/nikguscode/calculator/calculator/controller/CalculateController.java b/src/main/java/com/nikguscode/calculator/calculator/controller/CalculateController.java[m
[1mindex f1d94fb..6aa937a 100644[m
[1m--- a/src/main/java/com/nikguscode/calculator/calculator/controller/CalculateController.java[m
[1m+++ b/src/main/java/com/nikguscode/calculator/calculator/controller/CalculateController.java[m
[36m@@ -14,12 +14,12 @@[m [mpublic class CalculateController {[m
         this.localPayoutCalculator = localPayoutCalculator;[m
     }[m
 [m
[31m-//    @GetMapping("/calculate")[m
[31m-//    private String getData(@RequestParam double salary, @RequestParam(name = "duration") int holidayDuration) {[m
[31m-//        return localPayoutCalculator.calculate(salary, holidayDuration);[m
[31m-//    }[m
[31m-[m
     @GetMapping("/calculate")[m
[32m+[m[32m    private String getData(@RequestParam double salary, @RequestParam(name = "duration") int holidayDuration) {[m
[32m+[m[32m        return localPayoutCalculator.calculate(salary, holidayDuration);[m
[32m+[m[32m    }[m
[32m+[m
[32m+[m[32m    @GetMapping("/calculate-by-date")[m
     private String getData(@RequestParam double salary, @RequestParam(name = "duration") int holidayDuration,[m
                            @RequestParam int date, @RequestParam int month, @RequestParam int year) {[m
         return localPayoutCalculator.calculate(salary, holidayDuration, date, month, year);[m
[1mdiff --git a/src/main/java/com/nikguscode/calculator/calculator/model/DateStorage.java b/src/main/java/com/nikguscode/calculator/calculator/model/DateStorage.java[m
[1mindex 5199043..ae3f7bc 100644[m
[1m--- a/src/main/java/com/nikguscode/calculator/calculator/model/DateStorage.java[m
[1m+++ b/src/main/java/com/nikguscode/calculator/calculator/model/DateStorage.java[m
[36m@@ -1,5 +1,7 @@[m
 package com.nikguscode.calculator.calculator.model;[m
 [m
[32m+[m[32mimport static com.nikguscode.calculator.calculator.model.enumiration.DateType.*;[m
[32m+[m
 import java.time.LocalDate;[m
 import java.util.HashMap;[m
 import java.util.Map;[m
[36m@@ -11,19 +13,25 @@[m [mpublic class DateStorage {[m
 [m
     static {[m
         holidaysMap = new HashMap<>();[m
[31m-        holidaysMap.put("09/05", "holiday");[m
[31m-        holidaysMap.put("01/01", "holiday");[m
[31m-        holidaysMap.put("02/01", "holiday");[m
[31m-        holidaysMap.put("03/01", "holiday");[m
[31m-        holidaysMap.put("04/01", "holiday");[m
[31m-        holidaysMap.put("05/01", "holiday");[m
[31m-        holidaysMap.put("06/01", "holiday");[m
[31m-        holidaysMap.put("07/01", "holiday");[m
[31m-        holidaysMap.put("08/01", "holiday");[m
[31m-        holidaysMap.put("01/05", "holiday");[m
[31m-        holidaysMap.put("12/06", "holiday");[m
[31m-        holidaysMap.put("04/11", "holiday");[m
[31m-        holidaysMap.put("23/02", "holiday");[m
[31m-        holidaysMap.put("08/03", "holiday");[m
[32m+[m[32m        holidaysMap.put("9/5", "holiday");[m
[32m+[m[32m        holidaysMap.put("1/1", "holiday");[m
[32m+[m[32m        holidaysMap.put("2/1", "holiday");[m
[32m+[m[32m        holidaysMap.put("3/1", "holiday");[m
[32m+[m[32m        holidaysMap.put("4/1", "holiday");[m
[32m+[m[32m        holidaysMap.put("5/1", "holiday");[m
[32m+[m[32m        holidaysMap.put("6/1", "holiday");[m
[32m+[m[32m        holidaysMap.put("7/1", "holiday");[m
[32m+[m[32m        holidaysMap.put("8/1", "holiday");[m
[32m+[m[32m        holidaysMap.put("1/5", "holiday");[m
[32m+[m[32m        holidaysMap.put("12/6", "holiday");[m
[32m+[m[32m        holidaysMap.put("4/11", "holiday");[m
[32m+[m[32m        holidaysMap.put("23/2", "holiday");[m
[32m+[m[32m        holidaysMap.put("8/3", "holiday");[m
[32m+[m[32m    }[m
[32m+[m
[32m+[m[32m    public static RequestStatus isWorkingDay(LocalDate date) {[m
[32m+[m[32m        String query = date.getDayOfMonth() + "/" + date.getMonthValue();[m
[32m+[m
[32m+[m[32m        return holidaysMap.get(query) == null ? WORKING_DAY : HOLIDAY_DAY;[m
     }[m
 }[m
[1mdiff --git a/src/main/java/com/nikguscode/calculator/calculator/model/DateTypeChecker.java b/src/main/java/com/nikguscode/calculator/calculator/model/DateTypeChecker.java[m
[1mindex 343c729..9b9ae7e 100644[m
[1m--- a/src/main/java/com/nikguscode/calculator/calculator/model/DateTypeChecker.java[m
[1m+++ b/src/main/java/com/nikguscode/calculator/calculator/model/DateTypeChecker.java[m
[36m@@ -4,10 +4,7 @@[m [mimport static com.nikguscode.calculator.calculator.model.enumiration.DateType.*;[m
 [m
 import java.time.DayOfWeek;[m
 import java.time.LocalDate;[m
[31m-import java.util.Optional;[m
[31m-[m
 import lombok.extern.slf4j.Slf4j;[m
[31m-import org.springframework.http.HttpStatus;[m
 import org.springframework.http.ResponseEntity;[m
 import org.springframework.stereotype.Service;[m
 import org.springframework.web.client.HttpClientErrorException;[m
[36m@@ -23,50 +20,41 @@[m [mpublic class DateTypeChecker {[m
 [m
     private static RequestStatus connectToExternalApi(LocalDate date) {[m
         RestTemplate restTemplate = new RestTemplate();[m
[31m-        String apiUrl = UriComponentsBuilder.fromUriString("https://isdayoff.ru/api/getdata")[m
[32m+[m[32m        String apiUrl = UriComponentsBuilder.fromUriString("https://isdayoff.ru/api/getdata/")[m
                 .queryParam("day", date.getDayOfMonth())[m
                 .queryParam("month", date.getMonthValue())[m
                 .queryParam("year", date.getYear())[m
                 .build()[m
                 .toUriString();[m
[32m+[m
[32m+[m[32m        return handleRequest(restTemplate, apiUrl, date);[m
[32m+[m[32m    }[m
[32m+[m
[32m+[m[32m    private static RequestStatus handleRequest(RestTemplate restTemplate, String apiUrl, LocalDate date) {[m
         try {[m
             ResponseEntity<String> response = restTemplate.getForEntity(apiUrl, String.class);[m
[31m-            return handleRequest(response, date);[m
[32m+[m[32m            return checkWeekday(response);[m
         } catch (HttpClientErrorException.BadRequest e) {[m
             log.warn("Ошибка при передаче даты, рассчёт с данными из DateStorage для: {}", date);[m
             return checkWeekday(date);[m
[32m+[m[32m        } catch (Exception e) {[m
[32m+[m[32m            log.warn("Необрабатываемая ошибка");[m
[32m+[m[32m            return checkWeekday(date);[m
         }[m
     }[m
 [m
[31m-    private static RequestStatus handleRequest(ResponseEntity<String> response, LocalDate date) {[m
[31m-        HttpStatus httpStatus = (HttpStatus) response.getStatusCode();[m
[31m-[m
[31m-        switch (httpStatus) {[m
[31m-            case OK:[m
[31m-[m
[31m-                switch (Optional.ofNullable(response.getBody()).orElse("API_ERROR")) {[m
[31m-                    case ("0"):[m
[31m-                    case ("2"):[m
[31m-                    case ("4"):[m
[31m-                        return WORKING_DAY;[m
[31m-                    case ("1"):[m
[31m-                        return HOLIDAY_DAY;[m
[31m-                }[m
[31m-[m
[31m-            case BAD_REQUEST:[m
[31m-                log.warn(response.getBody() != null && response.getBody().equals("100")[m
[31m-                        ? "Ошибка при передаче даты, рассчёт с данными из DateStorage"[m
[31m-                        : "API ERROR, рассчёт с данными из DateStorage");[m
[31m-                return checkWeekday(date);[m
[31m-            default:[m
[31m-                log.warn("Необрабатываемый HTTP код, рассчёт с данными из DateStorage");[m
[31m-                return checkWeekday(date);[m
[31m-        }[m
[32m+[m[32m    private static RequestStatus checkWeekday(ResponseEntity<String> response) {[m
[32m+[m[32m        return response.getBody() != null && response.getBody().equals("1")[m
[32m+[m[32m                ? HOLIDAY_DAY[m
[32m+[m[32m                : WORKING_DAY;[m
     }[m
 [m
     private static RequestStatus checkWeekday(LocalDate date) {[m
         DayOfWeek dayOfWeek = date.getDayOfWeek();[m
[31m-        return DateStorage.holidaysMap.get() DataStorage dayOfWeek.equals(DayOfWeek.SATURDAY) || dayOfWeek.equals(DayOfWeek.SUNDAY)[m
[32m+[m
[32m+[m[32m        return (DateStorage.isWorkingDay(date).equals(HOLIDAY_DAY)[m
[32m+[m[32m                    || dayOfWeek.equals(DayOfWeek.SATURDAY)[m
[32m+[m[32m                    || dayOfWeek.equals(DayOfWeek.SUNDAY))[m
                 ? HOLIDAY_DAY[m
                 : WORKING_DAY;[m
     }[m
