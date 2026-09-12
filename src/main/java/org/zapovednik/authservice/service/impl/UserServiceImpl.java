package org.zapovednik.authservice.service.impl;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
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
    public User findByLogin(final String login) {
        final Optional<User> userOptional = userRepository.findByLogin(login);

        if (userOptional.isEmpty()) {
            throw new UserNotFoundException("User not found: " + login);
        }

        return userOptional.get();
    }

    @Override
    public User getCurrentUser() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return findByLogin(authentication.getName());
    }

    @Override
    public Page<UserResponseDto> findAll(final Pageable pageable) {
        return userRepository.findAll(pageable)
                .map(userMapper::toDto);
    }
}