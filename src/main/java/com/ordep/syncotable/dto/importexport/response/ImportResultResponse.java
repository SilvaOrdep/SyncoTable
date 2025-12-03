package com.ordep.syncotable.dto.importexport.response;

public record ImportResultResponse(
    Long cardId,
    int columnsCreated,
    int rowsInserted) {
}