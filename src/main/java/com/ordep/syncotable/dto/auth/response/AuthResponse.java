package com.ordep.syncotable.dto.auth.response;

public record AuthResponse(
    Long userId,
    String username,
    String role,
    String token) {
}
