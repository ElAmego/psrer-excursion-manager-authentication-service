package org.zapovednik.authservice.exception.custom;

public class TokenNotFoundException extends RuntimeException {
    public TokenNotFoundException(final String message) {
        super(message);
    }
}