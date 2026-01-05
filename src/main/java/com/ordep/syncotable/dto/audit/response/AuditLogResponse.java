package com.ordep.syncotable.dto.audit.response;

import java.time.Instant;
import java.util.Map;

public record AuditLogResponse(
    Long id,
    String entityType,
    Long entityId,
    String action,
    Long userId,
    String userName,
    Instant timestamp,
    Map<String, Object> diff) {
}