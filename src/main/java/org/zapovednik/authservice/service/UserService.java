package org.zapovednik.authservice.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.zapovednik.authservice.dto.response.UserResponseDto;
import org.zapovednik.authservice.model.entity.User;

public interface UserService {
    User findByLogin(final String login);
    User getCurrentUser();
    Page<UserResponseDto> findAll(final Pageable pageable);
}