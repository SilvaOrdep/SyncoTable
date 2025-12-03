package com.ordep.syncotable.dto.permission.request;

import jakarta.validation.constraints.NotNull;
import java.util.List;

public record PermissionUpdateRequest(
    @NotNull(message = "userId não pode ser nulo") Long userId,
    @NotNull(message = "cardId não pode ser nulo") Long cardId,
    boolean canView,
    boolean canCreate,
    boolean canEdit,
    boolean canDelete,
    List<ColumnPermissionEntry> columnOverrides) {
}
