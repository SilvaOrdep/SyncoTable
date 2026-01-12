package com.ordep.syncotable.dto.lock.response;

import java.time.Instant;

public record LockStatusResponse(
        boolean isLocked,
        String lockedByUsername,
        Instant lockedAt,
        boolean canEdit
) {}