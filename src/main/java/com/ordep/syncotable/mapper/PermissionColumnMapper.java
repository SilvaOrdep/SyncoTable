package com.ordep.syncotable.mapper;

import com.ordep.syncotable.dto.permission.request.ColumnPermissionEntry;
import com.ordep.syncotable.dto.permission.request.PermissionUpdateRequest;
import com.ordep.syncotable.dto.permission.response.ColumnPermissionEntryResponse;
import com.ordep.syncotable.model.Permission;
import com.ordep.syncotable.model.PermissionColumn;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface PermissionColumnMapper {

    @Mapping(target = "columnId", source = "cardColumn.id")
    @Mapping(target = "permissionId", source = "permission.id")
    ColumnPermissionEntryResponse toResponse(PermissionColumn permissionColumn);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "permission", ignore = true)
    @Mapping(target = "cardColumn", ignore = true)
    PermissionColumn toEntity(ColumnPermissionEntry entry);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "permission", ignore = true)
    @Mapping(target = "cardColumn", ignore = true)
    void updateEntity(ColumnPermissionEntry request, @MappingTarget PermissionColumn permissionColumn);

}
