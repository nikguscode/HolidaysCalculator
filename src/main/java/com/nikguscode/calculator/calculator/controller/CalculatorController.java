package com.nikguscode.calculator.calculator.controller;

import com.nikguscode.calculator.calculator.dto.ErrorDto;
import com.nikguscode.calculator.calculator.exception.InvalidValueException;
import com.nikguscode.calculator.calculator.model.calculator.PayoutCalculator;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.Period;

import static com.nikguscode.calculator.calculator.controller.Constants.*;

/**
 * <p>Контроллер, реализующий единственный эндпоинт программы и метод {@link CalculatorController#handle(double, Integer, LocalDate, LocalDate) handle()}
 * для обработки запросов к этому эндпоинту.</p>
 *
 * <p>Метод {@link CalculatorController#handleInvalidValueAndDateTimeException(Exception)} отвечает за возврат
 * корректного ответа, понятного пользователю API.</p>
 *
 * <p>Контроллер включает методы для обработки различных сценариев работы эндпоинта в зависимости от параметров запроса:</p>
 * <ul>
 *     <li>{@link CalculatorController#isValidDurationAndDate(Integer, LocalDate, LocalDate) isValidDurationAndDate()}</li>
 *     <li>{@link CalculatorController#isValidDurationAndStartDate(Integer, LocalDate, LocalDate) isValidDurationAndStartDate()}</li>
 *     <li>{@link CalculatorController#isValidDurationOnly(Integer, LocalDate, LocalDate) isValidDurationOnly()}</li>
 * </ul>
 */

@RestController
@Log4j2
public class CalculatorController {
    private final PayoutCalculator payoutCalculator;

    public CalculatorController(@Qualifier("localPayoutCalculator") PayoutCalculator payoutCalculator) {
        this.payoutCalculator = payoutCalculator;
    }

    @GetMapping("/calculator")
    public ResponseEntity<Long> handle(@RequestParam double salary,
                                       @RequestParam(required = false) Integer vacationDuration,
                                       @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE, pattern = DATE_PATTERN)
                                           LocalDate startVacationDate,
                                       @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE, pattern = DATE_PATTERN)
                                           LocalDate endVacationDate) throws InvalidValueException, DateTimeException {
        if (isValidDurationAndDate(vacationDuration, startVacationDate, endVacationDate)) {
            vacationDuration = Period.between(startVacationDate, endVacationDate).getDays();
            return ResponseEntity.ok(payoutCalculator.calculate(salary, vacationDuration + 1, startVacationDate));
        } else if (isValidDurationAndStartDate(vacationDuration, startVacationDate, endVacationDate)) {
            return ResponseEntity.ok(payoutCalculator.calculate(salary, vacationDuration, startVacationDate));
        } else if (isValidDurationOnly(vacationDuration, startVacationDate, endVacationDate)) {
            return ResponseEntity.ok(payoutCalculator.calculate(salary, vacationDuration));
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @ExceptionHandler({ InvalidValueException.class, DateTimeException.class, MissingServletRequestParameterException.class })
    public ResponseEntity<ErrorDto> handleInvalidValueAndDateTimeException(Exception ex) {
        String errorMessage;

        if (ex instanceof InvalidValueException) {
            errorMessage = ex.getMessage();
        } else if (ex instanceof DateTimeException){
            errorMessage = DATE_BAD_REQUEST;
        } else {
            errorMessage = UNDEFINED_BAD_REQUEST;
        }

        ErrorDto errorDto = new ErrorDto(HttpStatus.BAD_REQUEST.value(), errorMessage);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDto);
    }

    private boolean isValidDurationAndDate(Integer vacationDuration, LocalDate startVacationDate, LocalDate endVacationDate) {
        return vacationDuration == null && startVacationDate != null && endVacationDate != null;
    }

    private boolean isValidDurationAndStartDate(Integer vacationDuration, LocalDate startVacationDate, LocalDate endVacationDate) {
        return vacationDuration != null && startVacationDate != null && endVacationDate == null;
    }

    private boolean isValidDurationOnly(Integer vacationDuration, LocalDate startVacationDate, LocalDate endVacationDate) {
        return vacationDuration != null && startVacationDate == null && endVacationDate == null;
    }
}
