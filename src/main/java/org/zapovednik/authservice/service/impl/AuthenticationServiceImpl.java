package org.zapovednik.authservice.service.impl;

import java.time.LocalDateTime;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.zapovednik.authservice.constant.HttpConstant;
import org.zapovednik.authservice.dto.request.LoginRequestDto;
import org.zapovednik.authservice.dto.request.RefreshTokenRequestDto;
import org.zapovednik.authservice.dto.request.RegisterRequestDto;
import org.zapovednik.authservice.dto.response.JwtResponseDto;
import org.zapovednik.authservice.dto.response.UserResponseDto;
import org.zapovednik.authservice.exception.custom.AccountBlockedException;
import org.zapovednik.authservice.exception.custom.TokenExpiredException;
import org.zapovednik.authservice.exception.custom.TokenRevokedException;
import org.zapovednik.authservice.exception.custom.UserAlreadyExistsException;
import org.zapovednik.authservice.exception.custom.UserNotFoundException;
import org.zapovednik.authservice.model.entity.RefreshToken;
import org.zapovednik.authservice.model.entity.type.LogAction;
import org.zapovednik.authservice.model.entity.User;
import org.zapovednik.authservice.model.entity.type.UserRole;
import org.zapovednik.authservice.model.mapper.UserMapper;
import org.zapovednik.authservice.model.repository.UserRepository;
import org.zapovednik.authservice.security.JwtService;
import org.zapovednik.authservice.service.AuditLogService;
import org.zapovednik.authservice.service.AuthenticationService;
import org.zapovednik.authservice.service.RefreshTokenService;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final AuditLogService auditLogService;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public JwtResponseDto login(final LoginRequestDto loginRequestDto) {
        final String login = loginRequestDto.getLogin();
        final Optional<User> userOptional = userRepository.findByLogin(login);

        if (userOptional.isEmpty()) {
            throw new BadCredentialsException("Invalid login or password");
        }

        final User user = userOptional.get();

        if (!user.getIsActive()) {
            throw new AccountBlockedException("Account is blocked");
        }

        final String loginRequestDtoPassword = loginRequestDto.getPassword();
        final String userPasswordHash = user.getPasswordHash();

        if (!passwordEncoder.matches(loginRequestDtoPassword, userPasswordHash)) {
            throw new BadCredentialsException("Invalid login or password");
        }

        final String accessToken = jwtService.generateAccessToken(user.getId(), login, user.getUserRole());
        final String refreshToken = refreshTokenService.create(user.getId());

        auditLogService.log(user.getId(), LogAction.LOGIN.name(), "User logged in");

        return JwtResponseDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType(HttpConstant.BEARER_VALUE)
                .build();
    }

    @Override
    public UserResponseDto register(final RegisterRequestDto registerRequestDto, final Long adminId) {
        final String login = registerRequestDto.getLogin();

        if (userRepository.existsByLogin(login)) {
            throw new UserAlreadyExistsException("User with login '" + login + "' already exists");
        }

        final User user = User.builder()
                .login(login)
                .passwordHash(passwordEncoder.encode(registerRequestDto.getPassword()))
                .userRole(UserRole.valueOf(registerRequestDto.getUserRole().toUpperCase()))
                .isActive(true)
                .build();

        final User savedUser = userRepository.save(user);

        auditLogService.log(adminId, LogAction.REGISTER.name(), "Created user: " + savedUser.getLogin());

        return userMapper.toDto(savedUser);
    }

    @Override
    public JwtResponseDto refresh(final RefreshTokenRequestDto refreshTokenRequestDto) {
        final String token = refreshTokenRequestDto.getRefreshToken();
        final RefreshToken refreshToken = refreshTokenService.findByToken(token);

        if (refreshToken.getRevoked()) {
            throw new TokenRevokedException("Refresh token has been revoked");
        }

        if (refreshToken.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new TokenExpiredException("Refresh token has expired");
        }

        final Optional<User> optionalUser = userRepository.findById(refreshToken.getUserId());

        if (optionalUser.isEmpty()) {
            throw new UserNotFoundException("User not found");
        }

        final User user = optionalUser.get();

        if (!user.getIsActive()) {
            throw new AccountBlockedException("Account is blocked");
        }

        refreshTokenService.revoke(token);

        final String newAccessToken = jwtService.generateAccessToken(user.getId(), user.getLogin(), user.getUserRole());
        final String newRefreshToken = refreshTokenService.create(user.getId());

        auditLogService.log(user.getId(), LogAction.REFRESH.name(), "Token refreshed");

        return JwtResponseDto.builder()
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken)
                .tokenType(HttpConstant.BEARER_VALUE)
                .build();
    }

    @Override
    public void logout(final RefreshTokenRequestDto refreshTokenRequestDto) {
        final String token = refreshTokenRequestDto.getRefreshToken();
        final RefreshToken refreshToken = refreshTokenService.findByToken(token);

        if (!refreshToken.getRevoked()) {
            refreshTokenService.revoke(token);
            auditLogService.log(refreshToken.getUserId(), LogAction.LOGOUT.name(), "User logged out");
        }
    }
}