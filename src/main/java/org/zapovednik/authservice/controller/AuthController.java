package org.zapovednik.authservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zapovednik.authservice.dto.request.LoginRequestDto;
import org.zapovednik.authservice.dto.request.RefreshTokenRequestDto;
import org.zapovednik.authservice.dto.response.JwtResponseDto;
import org.zapovednik.authservice.service.AuthenticationService;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    public ResponseEntity<JwtResponseDto> login(@Valid @RequestBody final LoginRequestDto loginRequestDto) {
        return ResponseEntity.ok(authenticationService.login(loginRequestDto));
    }

    @PostMapping("/refresh")
    public ResponseEntity<JwtResponseDto> refresh(@Valid @RequestBody final RefreshTokenRequestDto refreshTokenRequestDto) {
        return ResponseEntity.ok(authenticationService.refresh(refreshTokenRequestDto));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@Valid @RequestBody final RefreshTokenRequestDto refreshTokenRequestDto) {
        authenticationService.logout(refreshTokenRequestDto);
        return ResponseEntity.noContent().build();
    }
}