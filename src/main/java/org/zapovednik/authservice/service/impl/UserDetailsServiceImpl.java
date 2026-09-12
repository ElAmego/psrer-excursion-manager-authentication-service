package org.zapovednik.authservice.service.impl;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.zapovednik.authservice.model.entity.User;
import org.zapovednik.authservice.model.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(@NonNull final String username) throws UsernameNotFoundException {
        final Optional<User> userOptional = userRepository.findByLogin(username);

        if (userOptional.isPresent()) {
            final User user = userOptional.get();

            return org.springframework.security.core.userdetails.User.builder()
                    .username(user.getLogin())
                    .password(user.getPasswordHash())
                    .roles(user.getUserRole().name())
                    .disabled(!user.getIsActive())
                    .build();
        } else {
            throw new UsernameNotFoundException("User not found: " + username);
        }
    }
}