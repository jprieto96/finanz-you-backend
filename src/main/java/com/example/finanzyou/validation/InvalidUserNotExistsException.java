package com.example.finanzyou.validation;

public class InvalidUserNotExistsException extends ValidationException {

    private static final String MESSAGE = "User does not exist in BBDD";

    public InvalidUserNotExistsException() {
        super(MESSAGE);
    }

}
