package org.zapovednik.authservice.service;

import org.zapovednik.authservice.model.entity.RefreshToken;

public interface RefreshTokenService {
    String create(final Long userId);
    RefreshToken findByToken(final String token);
    void revoke(final String token);
    void revokeAllByUserId(final Long userId);
}