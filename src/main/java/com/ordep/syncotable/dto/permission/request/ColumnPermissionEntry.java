package com.ordep.syncotable.dto.permission.request;

import jakarta.validation.constraints.NotNull;

public record ColumnPermissionEntry(
    @NotNull(message = "columnId não pode ser nulo") Long columnId,
    @NotNull(message = "permissionId não pode ser nulo") Long permissionId,
    Boolean canView,
    Boolean canEdit) {
}
