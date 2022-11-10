package com.example.finanzyou.validation;

public class InvalidEmptyNameException extends ValidationException {

    private static final String MESSAGE = "Empty username";

    public InvalidEmptyNameException() {
        super(MESSAGE);
    }
}
