package com.marcapo.template.service;

public class InvalidPasswordException extends Throwable {
    public InvalidPasswordException(String wrongPassword) {
        super(wrongPassword);
    }
}
