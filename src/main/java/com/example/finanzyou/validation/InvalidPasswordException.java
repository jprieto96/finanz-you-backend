package com.example.finanzyou.validation;

public class InvalidPasswordException extends ValidationException {

    private static final String MESSAGE = "Invalid password";

    public InvalidPasswordException() {
        super(MESSAGE);
    }
}
