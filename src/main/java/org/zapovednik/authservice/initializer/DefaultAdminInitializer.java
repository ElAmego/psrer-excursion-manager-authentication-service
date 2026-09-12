package org.zapovednik.authservice.initializer;

import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.zapovednik.authservice.model.entity.User;
import org.zapovednik.authservice.model.entity.type.UserRole;
import org.zapovednik.authservice.model.repository.UserRepository;

@Component
@RequiredArgsConstructor
public class DefaultAdminInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.default-admin.login}")
    private String adminLogin;

    @Value("${app.default-admin.password}")
    private String adminPassword;

    @Override
    public void run(final String @NonNull ... args) {
        if (!userRepository.existsByLogin(adminLogin)) {
            final User admin = User.builder()
                    .login(adminLogin)
                    .passwordHash(passwordEncoder.encode(adminPassword))
                    .userRole(UserRole.ADMIN)
                    .isActive(true)
                    .build();
            userRepository.save(admin);
        }
    }
}