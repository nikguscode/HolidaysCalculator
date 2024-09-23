package com.nikguscode.calculator.calculator.model.calculator;

import com.nikguscode.calculator.calculator.exception.InvalidValueException;
import com.nikguscode.calculator.calculator.model.HolidaysDateStorage;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.stream.Stream;

/**
 * Класс в котором осуществляются все вычисления отпускных, при помощи внутреннего хранилища {@link HolidaysDateStorage},
 * которое содержит даты праздников
 */
@Service
public class LocalPayoutCalculator implements PayoutCalculator {
    public static final double DAYS_PER_MONTH = 29.3;

    /**
     * Данный метод предназначен для стратегии рассчёта, при которой праздничные дни не учитываются
     *
     * @param salary ежемесечная зарплата пользователя
     * @param vacationDuration продолжительность отпуска
     * @return отпускные пользователя в виде целого числа
     * @throws InvalidValueException обработка некорректных значения для зарплаты и продолжительности отпуска
     */
    @Override
    public long calculate(double salary, int vacationDuration) throws InvalidValueException {
        validateValues(salary, vacationDuration);
        return Math.round(calculateDailySalary(salary) * vacationDuration);
    }

    /**
     * Данный метод предназначен для стратегии рассчёта, при которой праздничные дни учитываются
     *
     * @param salary ежемесечная зарплата пользователя
     * @param vacationDuration продолжительность отпуска
     * @return отпускные пользователя в виде целого числа
     * @throws InvalidValueException обработка некорректных значения для зарплаты и продолжительности отпуска
     */
    @Override
    public long calculate(double salary, int vacationDuration, LocalDate startVacationDate) throws InvalidValueException {
        validateValues(salary, vacationDuration);
        return Math.round(calculateDailySalary(salary) * countPayoutDays(vacationDuration, startVacationDate));
    }

    /**
     * Данный метод предназначен для проверки зарплаты и длительности отпуска на корректность, в случае, если значения
     * не удовлетворяют требованиям выбрасывается исключение {@link InvalidValueException}
     *
     * @param salary ежемесечная зарплата пользователя
     * @param vacationDuration продолжительность отпуска
     * @throws InvalidValueException обработка некорректных значения для зарплаты и продолжительности отпуска
     */
    private void validateValues(double salary, int vacationDuration) throws InvalidValueException {
        if (salary <= 0) {
            throw new InvalidValueException(salary);
        } else if (vacationDuration <= 0) {
            throw new InvalidValueException(vacationDuration);
        }
    }

    /**
     * Данный метод предназначен для вычисления среднедневного заработка, который используется при вычислении отпускных
     *
     * @param salary ежемесечная зарплата пользователя
     * @return среднедневной заработок
     */
    private double calculateDailySalary(double salary) {
        return salary / DAYS_PER_MONTH;
    }

    /**
     * Данный метод предназначен для подсчёта оплачиваемых дней, праздничные дни не являются оплачиваемыми. Проверка является
     * ли указанная дата оплачиваемой осуществляется при помощи {@link HolidaysDateStorage#isPayableDay(LocalDate)
     * isPayableDay()}
     *
     * @param vacationDuration продолжительность отпуска
     * @param startVacationDate дата начала отпуска
     * @return количество оплачиваемых дней в отпуске
     */
    private long countPayoutDays(int vacationDuration, LocalDate startVacationDate) {
        return Stream.iterate(0, n -> n + 1)
                .limit(vacationDuration)
                .filter(day -> HolidaysDateStorage.isPayableDay(startVacationDate.plusDays(day)))
                .count();
    }
}