package org.zapovednik.authservice.model.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.zapovednik.authservice.model.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByLogin (final String login);
    Boolean existsByLogin (final String login);
}