package com.ordep.syncotable.mapper;

import com.ordep.syncotable.dto.user.UserDto;
import com.ordep.syncotable.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserMapper {

    UserDto mapToDto(User user);
    @Mapping(target = "id", ignore = true)
    User mapToEntity(UserDto dto);

}
