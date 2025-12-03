package com.ordep.syncotable.dto.row.request;

import jakarta.validation.constraints.NotNull;
import java.util.Map;

public record CreateRowRequest(
    @NotNull(message = "cardId não pode ser nulo") Long cardId,
    @NotNull(message = "valores não podem ser nulos") Map<String, Object> values) {
}
