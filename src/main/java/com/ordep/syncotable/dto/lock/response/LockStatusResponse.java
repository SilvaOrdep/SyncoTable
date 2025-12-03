package com.ordep.syncotable.dto.lock.response;

import java.time.Instant;

public record LockStatusResponse(
    boolean locked,
    String lockedBy,
    Instant lockedAt) {
}