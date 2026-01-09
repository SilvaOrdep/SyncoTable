package com.ordep.syncotable.dto.row.request;

import jakarta.validation.constraints.NotNull;
import java.util.Map;

public record UpdateRowRequest(
    @NotNull(message = "rowId não pode ser nulo") Long rowId,
    @NotNull(message = "versão não pode ser nula") Long version,
    @NotNull(message = "userId não pode ser nulo") Long userId,
    @NotNull(message = "valores não podem ser nulos") Map<String, Object> values) {
}
