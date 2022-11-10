package com.example.finanzyou.validation;

public class InvalidPasswordFormatException extends ValidationException {

    private static final String MESSAGE = "Invalid password format";

    public InvalidPasswordFormatException() {
        super(MESSAGE);
    }
}
