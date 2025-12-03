package com.ordep.syncotable.dto.audit.request;

import java.time.Instant;

public record AuditQueryRequest(
    String entityType,
    Long entityId,
    Long userId,
    Instant from,
    Instant to) {
}