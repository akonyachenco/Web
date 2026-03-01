package ru.ssau.todo.exception;

public class DeletedTimeException extends RuntimeException {
    public DeletedTimeException(String message) {
        super(message);
    }
}
