package org.zapovednik.authservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zapovednik.authservice.dto.request.RegisterRequestDto;
import org.zapovednik.authservice.dto.response.UserResponseDto;
import org.zapovednik.authservice.service.AuthenticationService;
import org.zapovednik.authservice.service.UserService;

@RestController
@RequestMapping("/api/admin/authentication")
@RequiredArgsConstructor
public class AdminController {
    private final AuthenticationService authenticationService;
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<Long> register(@Valid @RequestBody final RegisterRequestDto requestDto) {
        final Long userId = authenticationService.register(requestDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(userId);
    }

    @GetMapping("/users")
    public ResponseEntity<Page<UserResponseDto>> getUsers(final Pageable pageable) {
        final Page<UserResponseDto> responseDtoList = userService.findAll(pageable);

        return ResponseEntity.ok(responseDtoList);
    }
}