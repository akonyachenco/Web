package ru.ssau.todo.exception;

public class DuplicateUsernameException extends RuntimeException {
    public DuplicateUsernameException(String username) {
        super(String.format("Username %s already exists", username));
    }
}
