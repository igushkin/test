package ru.practicum.shareit.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.security.InvalidParameterException;

@ControllerAdvice
public class ExceptionController {
    @ExceptionHandler({Exception.class})
    public ResponseEntity handleException(Exception e) {
        var msg = new ExceptionMsg("Unknown state: UNSUPPORTED_STATUS");
        return new ResponseEntity(msg, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({NotFoundException.class})
    public ResponseEntity handleException(NotFoundException e) {
        return new ResponseEntity(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({InvalidParameterException.class})
    public ResponseEntity handleException(RuntimeException e) {
        return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({ConsistencyException.class})
    public ResponseEntity handleException(ConsistencyException e) {
        return new ResponseEntity(e.getMessage(), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler({DuplicateKeyException.class})
    public ResponseEntity handleException(DuplicateKeyException e) {
        return new ResponseEntity(e.getMessage(), HttpStatus.CONFLICT);
        //return e.getMessage();
    }
}