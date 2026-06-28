package org.zapovednik.authservice.model.mapper;

import org.mapstruct.Mapper;
import org.zapovednik.authservice.dto.response.UserResponseDto;
import org.zapovednik.authservice.model.entity.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserResponseDto toDto(final User user);
}