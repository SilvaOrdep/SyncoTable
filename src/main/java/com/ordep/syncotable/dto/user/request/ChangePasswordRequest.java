package com.ordep.syncotable.dto.user.request;

import jakarta.validation.constraints.NotEmpty;

public record ChangePasswordRequest(
    @NotEmpty(message = "senha atual não pode ser vazia") String currentPassword,
    @NotEmpty(message = "nova senha não pode ser vazia") String newPassword) {
}
