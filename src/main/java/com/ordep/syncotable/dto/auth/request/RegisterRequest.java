package com.ordep.syncotable.dto.auth.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

public record RegisterRequest(
    @NotEmpty(message = "nome de usuário não pode ser vazio") String username,
    @NotEmpty(message = "Email não pode ser vazio") @Email(message = "utilize um email válido") String email,
    @NotEmpty(message = "campo de senha não pode ser vazio") String password,
    @NotEmpty(message = "perfil não pode ser vazio") String role) {
}
