package org.zapovednik.authservice.exception.custom;

public class AccountBlockedException extends RuntimeException {
    public AccountBlockedException(final String message) {
        super(message);
    }
}