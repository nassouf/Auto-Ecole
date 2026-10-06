package org.example.entities.exceptions;

public class WrongSecurityCodeException extends RuntimeException {
    public WrongSecurityCodeException() {
        super("Wrong Security Code");
    }
}
