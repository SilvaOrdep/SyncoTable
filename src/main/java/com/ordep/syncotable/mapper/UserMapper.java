package com.ordep.syncotable.mapper;

import com.ordep.syncotable.dto.user.UserDto;
import com.ordep.syncotable.dto.user.request.CreateUserRequest;
import com.ordep.syncotable.dto.user.request.UpdateUserRequest;
import com.ordep.syncotable.dto.user.response.UserResponse;
import com.ordep.syncotable.dto.user.response.UserSummaryResponse;
import com.ordep.syncotable.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserMapper {

    UserDto mapToDto(User user);
    
    @Mapping(target = "id", ignore = true)
    User mapToEntity(UserDto dto);

    @Mapping(target = "role", ignore = true)
    @Mapping(target = "active", source = "activated")
    UserResponse toResponse(User user);

    UserSummaryResponse toSummaryResponse(User user);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "activated", constant = "true")
    @Mapping(target = "lockedCards", ignore = true)
    User toEntity(CreateUserRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "activated", source = "active")
    @Mapping(target = "lockedCards", ignore = true)
    void updateEntity(UpdateUserRequest request, @MappingTarget User user);

}
