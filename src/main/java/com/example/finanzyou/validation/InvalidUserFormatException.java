package com.example.finanzyou.validation;

public class InvalidUserFormatException extends ValidationException {

    private static final String MESSAGE = "Username contains invalid characters";

    public InvalidUserFormatException() {
        super(MESSAGE);
    }
}
