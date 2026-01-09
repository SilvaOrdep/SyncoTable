package com.ordep.syncotable.dto.row.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record BatchDeleteRowsRequest(
    @NotEmpty(message = "lista de IDs não pode ser vazia") List<Long> rowIds,
    @NotNull(message = "userId não pode ser nulo") Long userId
    ) {
}
