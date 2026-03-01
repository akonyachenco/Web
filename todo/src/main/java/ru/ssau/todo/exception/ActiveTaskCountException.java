package ru.ssau.todo.exception;

public class ActiveTaskCountException extends RuntimeException {
    public ActiveTaskCountException(String message) {
        super(message);
    }
}
