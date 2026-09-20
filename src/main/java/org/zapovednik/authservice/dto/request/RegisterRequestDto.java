package org.zapovednik.authservice.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequestDto {

    @NotEmpty(message = "Login is required")
    @Size(max = 50, message = "login must not exceed 50 characters")
    private String login;

    @NotEmpty(message = "Password is required")
    private String password;

    @NotEmpty(message = "User role is required")
    @Pattern(regexp = "USER|ADMIN", message = "User role must be USER or ADMIN")
    private String userRole;
}