package org.zapovednik.authservice.exception.custom;

public class TokenRevokedException extends RuntimeException {
    public TokenRevokedException(final String message) {
        super(message);
    }
}