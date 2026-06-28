package org.zapovednik.authservice.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.zapovednik.authservice.dto.response.AuditLogResponseDto;
import org.zapovednik.authservice.model.entity.AuditLog;
import org.zapovednik.authservice.model.mapper.AuditLogMapper;
import org.zapovednik.authservice.model.repository.AuditLogRepository;
import org.zapovednik.authservice.service.AuditLogService;

@Service
@RequiredArgsConstructor
public class AuditLogServiceImpl implements AuditLogService {
    private final AuditLogRepository auditLogRepository;
    private final AuditLogMapper auditLogMapper;

    @Override
    public void log(final Long userId, final String action, final String details) {
        final AuditLog auditLog = AuditLog.builder()
                .userId(userId)
                .action(action)
                .details(details)
                .build();

        auditLogRepository.save(auditLog);
    }

    @Override
    public Page<AuditLogResponseDto> findAll(final Pageable pageable) {
        return auditLogRepository.findAll(pageable)
                .map(auditLogMapper::toDto);
    }
}