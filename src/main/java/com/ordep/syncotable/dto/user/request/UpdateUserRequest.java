package com.ordep.syncotable.dto.user.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

public record UpdateUserRequest(
    @NotEmpty(message = "nome de usuário não pode ser vazio") String username,
    @NotEmpty(message = "Email não pode ser vazio") @Email(message = "utilize um email válido") String email,
    String role,
    Boolean active) {
}
