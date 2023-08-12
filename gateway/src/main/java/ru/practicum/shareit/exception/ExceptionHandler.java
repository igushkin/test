package ru.practicum.shareit.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice
public class ExceptionHandler {
    @org.springframework.web.bind.annotation.ExceptionHandler({IllegalArgumentException.class})
    public ResponseEntity handleException(Exception e) {
        //var msg = new ExceptionMsg("Unknown state: UNSUPPORTED_STATUS");
        return new ResponseEntity(new ExceptionMsg(e.getMessage()), HttpStatus.BAD_REQUEST);
    }
}