package org.zapovednik.authservice.service;

import org.zapovednik.authservice.dto.request.LoginRequestDto;
import org.zapovednik.authservice.dto.request.RefreshTokenRequestDto;
import org.zapovednik.authservice.dto.request.RegisterRequestDto;
import org.zapovednik.authservice.dto.response.JwtResponseDto;
import org.zapovednik.authservice.dto.response.UserResponseDto;

public interface AuthenticationService {
    JwtResponseDto login(final LoginRequestDto loginRequestDto);
    UserResponseDto register(final RegisterRequestDto registerRequestDto, final Long adminId);
    JwtResponseDto refresh(RefreshTokenRequestDto refreshTokenRequestDto);
    void logout(RefreshTokenRequestDto refreshTokenRequestDto);
}