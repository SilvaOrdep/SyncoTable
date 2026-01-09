package com.ordep.syncotable.dto.card.request;

import jakarta.validation.constraints.NotEmpty;

public record UpdateCardRequest(
    @NotEmpty(message = "título não pode ser vazio") String title,
    String description,
    Long userId,
    Boolean locked) {
}
