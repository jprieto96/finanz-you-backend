package com.example.finanzyou.validation;

public class InvalidDniExistsException extends ValidationException{
    private static final String MESSAGE = "DNI already exists in BBDD";

    public InvalidDniExistsException() {
        super(MESSAGE);
    }
}
