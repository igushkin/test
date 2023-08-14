package ru.practicum.shareit.exception;

public class DuplicateKeyException extends RuntimeException {
    public DuplicateKeyException(String msg) {
        super(msg);
    }
}
