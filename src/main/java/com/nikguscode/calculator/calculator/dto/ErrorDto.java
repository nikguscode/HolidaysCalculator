package com.nikguscode.calculator.calculator.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * <p>
 * Данный класс предназначе для формирования ответа пользователю, в случае, если
 * {@link com.nikguscode.calculator.calculator.controller.CalculatorController#handleInvalidValueAndDateTimeException(Exception)
 * handleInvalidValueAndDateTimeException()} перехватывает одно из следующих исключений:
 * <ul>
 *     <li>{@link com.nikguscode.calculator.calculator.exception.InvalidValueException InvalidValueException}</li>
 *     <li>{@link java.time.DateTimeException DateTimeException}</li>
 *     <li>{@link org.springframework.web.bind.MissingServletRequestParameterException MissingServletRequetsParameterException}</li>
 * </ul>
 * </p>
 */
@Getter
@AllArgsConstructor
public class ErrorDto {
    private final int statusCode;
    private final String message;
}