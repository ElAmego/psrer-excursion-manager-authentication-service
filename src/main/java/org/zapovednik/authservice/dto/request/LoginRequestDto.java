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

    @NotEmpty(message = "login is required")
    @Size(max = 50, message = "login mustn't exceed 50 characters")
    private String login;

    @NotEmpty(message = "password is required")
    private String password;
}