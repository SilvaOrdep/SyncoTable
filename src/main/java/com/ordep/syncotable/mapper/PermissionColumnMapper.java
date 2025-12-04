package com.ordep.syncotable.mapper;

import com.ordep.syncotable.dto.permission.request.ColumnPermissionEntry;
import com.ordep.syncotable.dto.permission.response.ColumnPermissionEntryResponse;
import com.ordep.syncotable.model.PermissionColumn;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface PermissionColumnMapper {

    @Mapping(target = "columnId", source = "cardColumn.id")
    ColumnPermissionEntryResponse toResponse(PermissionColumn permissionColumn);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "permission", ignore = true)
    @Mapping(target = "cardColumn", ignore = true)
    PermissionColumn toEntity(ColumnPermissionEntry entry);

}
