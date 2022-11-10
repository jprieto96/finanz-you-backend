package com.example.finanzyou.validation;

public class InvalidUserNameFormatException extends ValidationException {

    private static final String MESSAGE = "Username contains invalid characters";

    public InvalidUserNameFormatException() {
        super(MESSAGE);
    }
}
