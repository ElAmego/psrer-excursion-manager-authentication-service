package org.zapovednik.authservice.model.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.zapovednik.authservice.dto.response.AuditLogResponseDto;
import org.zapovednik.authservice.model.entity.AuditLog;

@Mapper(componentModel = "spring")
public interface AuditLogMapper {

    @Mapping(target = "login", ignore = true)
    AuditLogResponseDto toDto(final AuditLog auditLog);
}