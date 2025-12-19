package com.ordep.syncotable.dto.permission.response;

public record ColumnPermissionEntryResponse(
        Long columnId,
        Long permissionId,
        boolean canView,
        boolean canEdit
) {}