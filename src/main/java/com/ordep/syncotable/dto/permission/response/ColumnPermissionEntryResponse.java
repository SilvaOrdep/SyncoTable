package com.ordep.syncotable.dto.permission.response;

public record ColumnPermissionEntryResponse(
        Long columnId,
        boolean canView,
        boolean canEdit
) {}