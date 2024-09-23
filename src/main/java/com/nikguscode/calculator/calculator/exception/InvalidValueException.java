package com.nikguscode.calculator.calculator.exception;

import java.time.LocalDate;

import static com.nikguscode.calculator.calculator.controller.Constants.*;

/**
 * <p>Данный класс реализует пользовательское исключение, которое выбрасывается в том случае, если значения передаваемые
 * клиентом в {@link com.nikguscode.calculator.calculator.controller.CalculatorController#handle(double, Integer, LocalDate, LocalDate)
 * handle()} являются некорректными.</p>
 *
 * <p>
 * Данный класс имеет два перегруженных конструктора:
 * <ul>
 *     <li>{@link InvalidValueException#InvalidValueException(double)} при некорректном значении зарплаты</li>
 *     <li>{@link InvalidValueException#InvalidValueException(int)} при некорректном интервале отпуска</li>
 * </ul>
 * </p>
 * Исключение выбрасывается в классе: {@link com.nikguscode.calculator.calculator.model.calculator.PayoutCalculator PayoutCalculator}
 */
public class InvalidValueException extends Exception {
    public InvalidValueException(double salary) {
        super("Некорректное значение: " + salary + ", зарплата не может быть отрицательной. " + URL_BAD_REQUEST);
    }

    public InvalidValueException(int vacationDuration) {
        super(DATE_PERIOD_BAD_REQUEST);
    }
}