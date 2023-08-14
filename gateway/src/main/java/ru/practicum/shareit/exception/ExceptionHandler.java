package ru.practicum.shareit.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;

import java.security.InvalidParameterException;

@ControllerAdvice
@Slf4j
public class ExceptionHandler {
    @org.springframework.web.bind.annotation.ExceptionHandler({IllegalArgumentException.class})
    public ResponseEntity handleException(Exception e) {
        log.error("Обработана ошибка: {}. Причина: {}", "getBookingsByOwnerId", e.getClass().getName(), e.getMessage());
        return new ResponseEntity(new ExceptionMsg(e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler({InvalidParameterException.class})
    public ResponseEntity handleException(RuntimeException e) {
        log.error("Обработана ошибка: {}. Причина: {}", "getBookingsByOwnerId", e.getClass().getName(), e.getMessage());
        return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
}