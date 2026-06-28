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

    @NotEmpty(message = "login is required")
    @Size(max = 50, message = "login mustn't exceed 50 characters")
    private String login;

    @NotEmpty(message = "password is required")
    private String password;

    @NotEmpty(message = "userRole is required")
    @Pattern(regexp = "USER|ADMIN", message = "userRole must be USER or ADMIN")
    private String userRole;
}