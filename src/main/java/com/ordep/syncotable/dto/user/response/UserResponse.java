package com.ordep.syncotable.dto.user.response;

public record UserResponse(
    Long id,
    String username,
    String email,
    String role,
    boolean active) {
}
