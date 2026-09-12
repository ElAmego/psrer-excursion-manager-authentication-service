package org.zapovednik.authservice.exception.custom;

public class TokenExpiredException extends RuntimeException {
    public TokenExpiredException(final String message) {
        super(message);
    }
}