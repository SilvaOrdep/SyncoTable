package com.ordep.syncotable.dto.permission.request;

import jakarta.validation.constraints.NotNull;

public record ColumnPermissionEntry(
    @NotNull(message = "columnId não pode ser nulo") Long columnId,
    Boolean canView,
    Boolean canEdit) {
}
