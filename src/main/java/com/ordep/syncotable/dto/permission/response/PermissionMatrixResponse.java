package com.ordep.syncotable.dto.permission.response;

import java.util.List;

public record PermissionMatrixResponse(
    Long userId,
    Long cardId,
    boolean canView,
    boolean canCreate,
    boolean canEdit,
    boolean canDelete,
    List<ColumnPermissionEntryResponse> columnOverrides) {
}
