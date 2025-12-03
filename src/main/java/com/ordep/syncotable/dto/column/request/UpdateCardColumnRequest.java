package com.ordep.syncotable.dto.column.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record UpdateCardColumnRequest(
    @NotNull(message = "columnId não pode ser nulo") Long columnId,
    @NotEmpty(message = "label não pode ser vazia") String label,
    String type,
    Integer orderIndex,
    Boolean required,
    Boolean visible,
    Boolean editable) {
}
