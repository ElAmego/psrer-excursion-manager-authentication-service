package org.zapovednik.authservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zapovednik.authservice.dto.request.LoginRequestDto;
import org.zapovednik.authservice.dto.request.RefreshTokenRequestDto;
import org.zapovednik.authservice.dto.response.JwtResponseDto;
import org.zapovednik.authservice.dto.response.UserResponseDto;
import org.zapovednik.authservice.service.AuthenticationService;
import org.zapovednik.authservice.service.UserService;

@RestController
@RequestMapping("/api/user/authentication")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<JwtResponseDto> login(@Valid @RequestBody final LoginRequestDto requestDto) {
        final JwtResponseDto responseDto = authenticationService.login(requestDto);

        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("/validate")
    public ResponseEntity<UserResponseDto> validate(final Authentication authentication) {
        final UserResponseDto responseDto = userService.validate(authentication);

        return ResponseEntity.ok(responseDto);
    }

    @PostMapping("/refresh")
    public ResponseEntity<JwtResponseDto> refresh(@Valid @RequestBody final RefreshTokenRequestDto requestDto) {
        final JwtResponseDto responseDto = authenticationService.refresh(requestDto);

        return ResponseEntity.ok(responseDto);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@Valid @RequestBody final RefreshTokenRequestDto requestDto) {
        authenticationService.logout(requestDto);

        return ResponseEntity.noContent().build();
    }
}