package org.zapovednik.authservice.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.zapovednik.authservice.model.entity.type.UserRole;

public interface JwtService {
    String generateAccessToken(final Long userId, final String login, final UserRole userRole);
    String extractLogin(final String token);
    boolean isTokenValid(final String token, final UserDetails userDetails);
}