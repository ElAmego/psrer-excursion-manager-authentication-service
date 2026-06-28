package org.zapovednik.authservice.dto.response;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuditLogResponseDto {
    private Long id;
    private Long userId;
    private String login;
    private String action;
    private String details;
    private LocalDateTime createdAt;
}