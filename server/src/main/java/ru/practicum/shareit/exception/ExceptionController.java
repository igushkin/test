package ru.practicum.shareit.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.security.InvalidParameterException;

@ControllerAdvice
@Slf4j
public class ExceptionController {
    @ExceptionHandler({Exception.class})
    public ResponseEntity handleException(Exception e) {
        log.error("Обработана ошибка: {}. Причина: {}", "getBookingsByOwnerId", e.getClass().getName(), e.getMessage());
        var msg = new ExceptionMsg("Unknown state: UNSUPPORTED_STATUS");
        return new ResponseEntity(msg, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({NotFoundException.class})
    public ResponseEntity handleException(NotFoundException e) {
        log.error("Обработана ошибка: {}. Причина: {}", "getBookingsByOwnerId", e.getClass().getName(), e.getMessage());
        return new ResponseEntity(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({InvalidParameterException.class})
    public ResponseEntity handleException(RuntimeException e) {
        log.error("Обработана ошибка: {}. Причина: {}", "getBookingsByOwnerId", e.getClass().getName(), e.getMessage());
        return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({ConsistencyException.class})
    public ResponseEntity handleException(ConsistencyException e) {
        log.error("Обработана ошибка: {}. Причина: {}", "getBookingsByOwnerId", e.getClass().getName(), e.getMessage());
        return new ResponseEntity(e.getMessage(), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler({DuplicateKeyException.class})
    public ResponseEntity handleException(DuplicateKeyException e) {
        log.error("Обработана ошибка: {}. Причина: {}", "getBookingsByOwnerId", e.getClass().getName(), e.getMessage());
        return new ResponseEntity(e.getMessage(), HttpStatus.CONFLICT);
    }
}