package ru.ssau.todo.exception;

public class InvalidTokenSignatureException extends RuntimeException {
    public InvalidTokenSignatureException(String message) {
        super(message);
    }

    public InvalidTokenSignatureException() {
        super("Invalid token signature");
    }
}
