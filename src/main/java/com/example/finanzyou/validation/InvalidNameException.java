package com.example.finanzyou.validation;

public class InvalidNameException extends ValidationException {

    private static final String MESSAGE = "Username already exists in BBDD";

    public InvalidNameException() {
        super(MESSAGE);
    }
}
