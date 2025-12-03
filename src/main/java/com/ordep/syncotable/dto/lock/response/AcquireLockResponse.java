package com.ordep.syncotable.dto.lock.response;

import java.time.Instant;

public record AcquireLockResponse(
    boolean acquired,
    String lockedBy,
    Instant lockedAt) {
}