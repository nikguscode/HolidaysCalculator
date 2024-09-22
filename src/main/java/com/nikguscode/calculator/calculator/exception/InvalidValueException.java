package com.nikguscode.calculator.calculator.exception;

import lombok.extern.log4j.Log4j2;

import static com.nikguscode.calculator.calculator.controller.Constants.*;

@Log4j2
public class InvalidValueException extends Exception {
    public InvalidValueException(double salary) {
        super("Некорректное значение: " + salary + ", зарплата не может быть отрицательной. " + URL_BAD_REQUEST);
    }

    public InvalidValueException(int vacationDuration) {
        super(DATE_PERIOD_BAD_REQUEST);
    }
}