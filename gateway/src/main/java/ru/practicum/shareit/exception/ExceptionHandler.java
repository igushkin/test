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
/*
    @org.springframework.web.bind.annotation.ExceptionHandler({NotFoundException.class})
    public ResponseEntity handleException(NotFoundException e) {
        return new ResponseEntity(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler({InvalidParameterException.class})
    public ResponseEntity handleException(RuntimeException e) {
        return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler({ConsistencyException.class})
    public ResponseEntity handleException(ConsistencyException e) {
        return new ResponseEntity(e.getMessage(), HttpStatus.FORBIDDEN);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler({DuplicateKeyException.class})
    public ResponseEntity handleException(DuplicateKeyException e) {
        return new ResponseEntity(e.getMessage(), HttpStatus.CONFLICT);
    }*/
}