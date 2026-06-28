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
import org.zapovednik.authservice.dto.response.AuditLogResponseDto;
import org.zapovednik.authservice.dto.response.UserResponseDto;
import org.zapovednik.authservice.service.AuditLogService;
import org.zapovednik.authservice.service.AuthenticationService;
import org.zapovednik.authservice.service.UserService;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AuthenticationService authenticationService;
    private final AuditLogService auditLogService;
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<UserResponseDto> register(@Valid @RequestBody final RegisterRequestDto registerRequestDto) {
        final Long adminId = userService.getCurrentUser().getId();
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(authenticationService.register(registerRequestDto, adminId));
    }

    @GetMapping("/logs")
    public ResponseEntity<Page<AuditLogResponseDto>> getLogs(final Pageable pageable) {
        return ResponseEntity.ok(auditLogService.findAll(pageable));
    }

    @GetMapping("/users")
    public ResponseEntity<Page<UserResponseDto>> getUsers(final Pageable pageable) {
        return ResponseEntity.ok(userService.findAll(pageable));
    }
}