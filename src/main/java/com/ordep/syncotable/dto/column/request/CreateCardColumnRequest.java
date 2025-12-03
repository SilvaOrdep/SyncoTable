package com.ordep.syncotable.dto.column.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record CreateCardColumnRequest(
    @NotNull(message = "cardId não pode ser nulo") Long cardId,
    @NotEmpty(message = "key não pode ser vazia") String key,
    @NotEmpty(message = "label não pode ser vazia") String label,
    @NotEmpty(message = "type não pode ser vazio") String type,
    Integer orderIndex,
    Boolean required,
    Boolean visible,
    Boolean editable) {
}
