package org.zapovednik.authservice.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zapovednik.authservice.dto.response.UserResponseDto;
import org.zapovednik.authservice.exception.custom.UserNotFoundException;
import org.zapovednik.authservice.model.entity.User;
import org.zapovednik.authservice.model.mapper.UserMapper;
import org.zapovednik.authservice.model.repository.UserRepository;
import org.zapovednik.authservice.service.UserService;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    @Transactional(readOnly = true)
    public UserResponseDto validate(final Authentication authentication) {
        final String login = authentication.getName();
        final String role = authentication.getAuthorities().iterator().next().getAuthority();

        final User user = userRepository.findByLogin(login)
                .orElseThrow(() -> new UserNotFoundException("User not found: " + login));

        return UserResponseDto.builder()
                .id(user.getId())
                .login(user.getLogin())
                .userRole(role != null ? role.replace("ROLE_", "") : null)
                .isActive(true)
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserResponseDto> findAll(final Pageable pageable) {
        return userRepository.findAll(pageable)
                .map(userMapper::toDto);
    }
}