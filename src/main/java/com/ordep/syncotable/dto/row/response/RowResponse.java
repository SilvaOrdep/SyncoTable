package com.ordep.syncotable.dto.row.response;

import java.time.Instant;
import java.util.Map;

public record RowResponse(
    Long id,
    Long cardId,
    Map<String, Object> values,
    Long version,
    String status,
    Instant createdAt,
    Instant updatedAt) {
}
