package org.zapovednik.authservice.service.impl;

import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zapovednik.authservice.configuration.constant.HttpConstant;
import org.zapovednik.authservice.dto.request.LoginRequestDto;
import org.zapovednik.authservice.dto.request.RefreshTokenRequestDto;
import org.zapovednik.authservice.dto.request.RegisterRequestDto;
import org.zapovednik.authservice.dto.response.JwtResponseDto;
import org.zapovednik.authservice.exception.custom.AccountBlockedException;
import org.zapovednik.authservice.exception.custom.TokenExpiredException;
import org.zapovednik.authservice.exception.custom.TokenRevokedException;
import org.zapovednik.authservice.exception.custom.UserAlreadyExistsException;
import org.zapovednik.authservice.exception.custom.UserNotFoundException;
import org.zapovednik.authservice.model.entity.RefreshToken;
import org.zapovednik.authservice.model.entity.User;
import org.zapovednik.authservice.model.entity.type.UserRole;
import org.zapovednik.authservice.model.repository.UserRepository;
import org.zapovednik.authservice.security.JwtService;
import org.zapovednik.authservice.service.AuthenticationService;
import org.zapovednik.authservice.service.RefreshTokenService;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public JwtResponseDto login(final LoginRequestDto requestDto) {
        final String login = requestDto.getLogin();
        final User user = userRepository.findByLogin(login)
                .orElseThrow(() -> new BadCredentialsException("Invalid login or password"));

        if (!user.getIsActive()) {
            throw new AccountBlockedException("Account is blocked");
        }

        final String password = requestDto.getPassword();
        final String userPasswordHash = user.getPasswordHash();

        if (!passwordEncoder.matches(password, userPasswordHash)) {
            throw new BadCredentialsException("Invalid login or password");
        }

        final String accessToken = jwtService.generateAccessToken(user.getId(), login, user.getUserRole());
        final String refreshToken = refreshTokenService.create(user.getId());

        return JwtResponseDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType(HttpConstant.BEARER_VALUE)
                .build();
    }

    @Override
    @Transactional
    public Long register(final RegisterRequestDto requestDto) {
        final String login = requestDto.getLogin();

        if (userRepository.existsByLogin(login)) {
            throw new UserAlreadyExistsException("User with login '" + login + "' already exists");
        }

        final User user = User.builder()
                .login(login)
                .passwordHash(passwordEncoder.encode(requestDto.getPassword()))
                .userRole(UserRole.valueOf(requestDto.getUserRole().toUpperCase()))
                .isActive(true)
                .build();

        final User savedUser = userRepository.save(user);

        return savedUser.getId();
    }

    @Override
    @Transactional
    public JwtResponseDto refresh(final RefreshTokenRequestDto requestDto) {
        final String token = requestDto.getRefreshToken();
        final RefreshToken refreshToken = refreshTokenService.findByToken(token);

        if (refreshToken.getRevoked()) {
            throw new TokenRevokedException("Refresh token has been revoked");
        }

        if (refreshToken.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new TokenExpiredException("Refresh token has expired");
        }

        final Long userId = refreshToken.getUserId();
        final User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        if (!user.getIsActive()) {
            throw new AccountBlockedException("Account is blocked");
        }

        refreshTokenService.revoke(token);

        final String newAccessToken = jwtService.generateAccessToken(user.getId(), user.getLogin(), user.getUserRole());
        final String newRefreshToken = refreshTokenService.create(user.getId());

        return JwtResponseDto.builder()
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken)
                .tokenType(HttpConstant.BEARER_VALUE)
                .build();
    }

    @Override
    @Transactional
    public void logout(final RefreshTokenRequestDto refreshTokenRequestDto) {
        final String token = refreshTokenRequestDto.getRefreshToken();
        final RefreshToken refreshToken = refreshTokenService.findByToken(token);

        if (!refreshToken.getRevoked()) {
            refreshTokenService.revoke(token);
        }
    }
}