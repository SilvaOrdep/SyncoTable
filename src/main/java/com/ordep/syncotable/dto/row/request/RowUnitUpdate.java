package com.ordep.syncotable.dto.row.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RowUnitUpdate(@NotBlank(message = "Coluna não pode ser nula") String columnKey,
                            @NotBlank(message = "Campo de novo valor não pode ser nulo") String newValue,
                            @NotNull(message = "Id da linha não pode ser nulo") Long rowId,
                            @NotNull(message = "userId não pode ser nulo") Long userId,
                            @NotNull(message = "Versão da linha não pode ser nula") Long version) {
}