package ru.ssau.todo.exception;

public class ExpiredTokenException extends RuntimeException {
    public ExpiredTokenException(String message) {
        super(message);
    }

    public ExpiredTokenException() {
        super("Expired token");
    }
}
