package org.zapovednik.authservice.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.zapovednik.authservice.dto.response.UserResponseDto;

public interface UserService {
    UserResponseDto validate(final Authentication authentication);
    Page<UserResponseDto> findAll(final Pageable pageable);
}