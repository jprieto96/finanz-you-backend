package com.example.finanzyou.validation;

public class InvalidDniException extends ValidationException{
    private static final String MESSAGE = "Invalid format for this DNI";

    public InvalidDniException() {
        super(MESSAGE);
    }
}
