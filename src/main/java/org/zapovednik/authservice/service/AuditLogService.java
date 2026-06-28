package org.zapovednik.authservice.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.zapovednik.authservice.dto.response.AuditLogResponseDto;

public interface AuditLogService {
    void log (final Long userId, final String action, final String details);
    Page<AuditLogResponseDto> findAll(final Pageable pageable);
}