package ru.ssau.todo.exception;

public class InvalidTokenFormatException extends RuntimeException {
    public InvalidTokenFormatException(String message) {
        super(message);
    }

    public InvalidTokenFormatException() {
        super("Invalid token format");
    }
}
