package com.ordep.syncotable.dto.user.response;

public record UserSummaryResponse(
    Long id,
    String username,
    String role) {
}
