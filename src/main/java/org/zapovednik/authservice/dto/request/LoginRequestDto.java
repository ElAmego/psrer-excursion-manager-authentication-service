package org.zapovednik.authservice.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequestDto {

    @NotEmpty(message = "Login is required")
    @Size(max = 50, message = "Login must not exceed 50 characters")
    private String login;

    @NotEmpty(message = "Password is required")
    private String password;
}