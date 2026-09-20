package org.zapovednik.authservice.service;

import org.zapovednik.authservice.dto.request.LoginRequestDto;
import org.zapovednik.authservice.dto.request.RefreshTokenRequestDto;
import org.zapovednik.authservice.dto.request.RegisterRequestDto;
import org.zapovednik.authservice.dto.response.JwtResponseDto;

public interface AuthenticationService {
        JwtResponseDto login(final LoginRequestDto requestDto);
        Long register(final RegisterRequestDto requestDto);
        JwtResponseDto refresh(final RefreshTokenRequestDto requestDto);
        void logout(final RefreshTokenRequestDto requestDto);
}