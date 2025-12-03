package com.ordep.syncotable.dto.column.response;

public record CardColumnResponse(
    Long id,
    Long cardId,
    String key,
    String label,
    String type,
    Integer orderIndex,
    boolean required,
    boolean visible,
    boolean editable) {
}
