package com.ordep.syncotable.mapper;

import com.ordep.syncotable.dto.permission.request.PermissionUpdateRequest;
import com.ordep.syncotable.model.Permission;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface PermissionMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "card", ignore = true)
    Permission toEntity(PermissionUpdateRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "card", ignore = true)
    void updateEntity(PermissionUpdateRequest request, @MappingTarget Permission permission);

}
