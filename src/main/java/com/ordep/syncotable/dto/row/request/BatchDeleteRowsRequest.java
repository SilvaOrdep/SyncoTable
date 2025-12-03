package com.ordep.syncotable.dto.row.request;

import jakarta.validation.constraints.NotEmpty;
import java.util.List;

public record BatchDeleteRowsRequest(
    @NotEmpty(message = "lista de IDs não pode ser vazia") List<Long> rowIds) {
}
