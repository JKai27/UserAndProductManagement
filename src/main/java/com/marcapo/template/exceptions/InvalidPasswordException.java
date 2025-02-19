package com.marcapo.template.exceptions;

public class InvalidPasswordException extends Exception {
    public InvalidPasswordException(String wrongPassword) {
        super(wrongPassword);
    }
}
