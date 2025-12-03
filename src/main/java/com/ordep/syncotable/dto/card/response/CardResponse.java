package com.ordep.syncotable.dto.card.response;

import java.time.Instant;

public record CardResponse(
    Long id,
    String title,
    String description,
    String lockedBy,
    Instant lockedAt,
    Instant createdAt) {
}
