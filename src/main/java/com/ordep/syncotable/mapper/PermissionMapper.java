package com.ordep.syncotable.mapper;

import com.ordep.syncotable.dto.permission.request.PermissionUpdateRequest;
import com.ordep.syncotable.dto.permission.response.ColumnPermissionEntryResponse;
import com.ordep.syncotable.dto.permission.response.PermissionMatrixResponse;
import com.ordep.syncotable.model.Permission;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

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

    @Mapping(source = "permission.user.id", target = "userId")
    @Mapping(source = "permission.card.id", target = "cardId")
    @Mapping(source = "columnPermissionList", target = "columnOverrides")
    PermissionMatrixResponse toMatrixResponse(Permission permission, List<ColumnPermissionEntryResponse> columnPermissionList);

}
