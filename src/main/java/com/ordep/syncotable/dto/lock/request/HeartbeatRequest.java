package com.ordep.syncotable.dto.lock.request;

import jakarta.validation.constraints.NotNull;

public record HeartbeatRequest(
    @NotNull(message = "cardId não pode ser nulo") Long cardId) {
}