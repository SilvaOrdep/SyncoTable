package com.ordep.syncotable.dto.auth.request;

import jakarta.validation.constraints.NotEmpty;

public record LoginRequest(
    @NotEmpty(message = "nome de usuário não pode ser vazio") String username,
    @NotEmpty(message = "campo de senha não pode ser vazio") String password) {
}
