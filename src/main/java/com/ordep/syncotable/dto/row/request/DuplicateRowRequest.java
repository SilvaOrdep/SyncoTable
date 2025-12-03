package com.ordep.syncotable.dto.row.request;

import jakarta.validation.constraints.NotNull;

public record DuplicateRowRequest(
    @NotNull(message = "rowId não pode ser nulo") Long rowId,
    Long targetCardId) {
}
