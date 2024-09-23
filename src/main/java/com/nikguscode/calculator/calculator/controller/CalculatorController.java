package com.nikguscode.calculator.calculator.controller;

import com.nikguscode.calculator.calculator.dto.ErrorDto;
import com.nikguscode.calculator.calculator.exception.InvalidValueException;
import com.nikguscode.calculator.calculator.model.calculator.PayoutCalculator;
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
 * для обработки запросов к этому эндпоинту</p>
 */
@RestController
public class CalculatorController {
    private final PayoutCalculator payoutCalculator;

    public CalculatorController(@Qualifier("localPayoutCalculator") PayoutCalculator payoutCalculator) {
        this.payoutCalculator = payoutCalculator;
    }

    /**
     * <p>
     * Данный метод является обработчиком эндпоинта. Он обрабатывает только успешные запросы, все
     * исключения обрабатываются в {@link CalculatorController#handleInvalidValueAndDateTimeException(Exception)
     * handleInvalidValueAndDateTimeException()}. Для выбора стратегии рассчёта отпускных используются следующие методы: <br/>
     * <ul>
     *     <li>{@link CalculatorController#isValidDate(Integer, LocalDate, LocalDate) isValidDurationAndDate()}</li>
     *     <li>{@link CalculatorController#isValidDurationAndStartDate(Integer, LocalDate, LocalDate)  isValidDurationAndStartDate()}</li>
     *     <li>{@link CalculatorController#isValidDurationOnly(Integer, LocalDate, LocalDate)   isValidDurationOnly()}</li>
     * </ul>
     * </p>
     *
     * @param salary ежемесечная зарплата пользователя
     * @param vacationDuration продолжительность отпуска
     * @param startVacationDate дата начала отпуска
     * @param endVacationDate дата окончания отпуска
     * @return отпускные пользователя в виде целого числа
     * @throws InvalidValueException обработка некорректных значений для зарплаты и продолжительности отпуска
     * @throws DateTimeException обработка некорректных дат, пример: 30-02-2024
     */
    @GetMapping("/calculator")
    public ResponseEntity<Long> handle(@RequestParam double salary,
                                       @RequestParam(required = false) Integer vacationDuration,
                                       @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE, pattern = DATE_PATTERN)
                                           LocalDate startVacationDate,
                                       @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE, pattern = DATE_PATTERN)
                                           LocalDate endVacationDate) throws InvalidValueException, DateTimeException {
        if (isValidDate(vacationDuration, startVacationDate, endVacationDate)) {
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

    /**
     * <p>
     * Данный метод предназначен для перехвата исключений. Перехватывает следующие исключения:
     * <ul>
     *     <li>{@link InvalidValueException}</li>
     *     <li>{@link DateTimeException}</li>
     *     <li>{@link MissingServletRequestParameterException}</li>
     * </ul>
     * </p>
     *
     * @param ex выбрасываемое исключение
     * @return {@link ErrorDto}, который содержит код ответа и сообщение с информацией для клиента
     */
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

    /**
     * Данный метод предназначен для стратегии, при которой пользователь выполняет рассчёт отпускных, используя даты, а
     * не продолжительность отпуска. Данная стратегия учитывает неоплачиваемые (праздничные) дни
     *
     * @param vacationDuration продолжительность отпуска
     * @param startVacationDate дата начала отпуска
     * @param endVacationDate дата окончания отпуска
     * @return true, если пользователь указал дату начала отпуска и дату окончания отпуска, иначе false
     */
    private boolean isValidDate(Integer vacationDuration, LocalDate startVacationDate, LocalDate endVacationDate) {
        return vacationDuration == null && startVacationDate != null && endVacationDate != null;
    }

    /**
     * Данный метод предназначен для стратегии, при которой пользователь выполняет рассчёт отпускных, используя дату
     * начала отпуска, а также его продолжительность. Данная стратегия учитывает неоплачиваемые (праздничные) дни
     *
     * @param vacationDuration продолжительность отпуска
     * @param startVacationDate дата начала отпуска
     * @param endVacationDate дата окончания отпуска
     * @return true, если пользователь указал дату начала отпуска и его длительность, иначе false
     */
    private boolean isValidDurationAndStartDate(Integer vacationDuration, LocalDate startVacationDate, LocalDate endVacationDate) {
        return vacationDuration != null && startVacationDate != null && endVacationDate == null;
    }

    /**
     * Данный метод предназначен для стратегии, при которой пользователь выполняет рассчёт отпускных, используя только
     * продолжительность отпуска
     *
     * @param vacationDuration продолжительность отпуска
     * @param startVacationDate дата начала отпуска
     * @param endVacationDate дата окончания отпуска
     * @return true, если пользователь указал длительность отпуска, иначе false
     */
    private boolean isValidDurationOnly(Integer vacationDuration, LocalDate startVacationDate, LocalDate endVacationDate) {
        return vacationDuration != null && startVacationDate == null && endVacationDate == null;
    }
}