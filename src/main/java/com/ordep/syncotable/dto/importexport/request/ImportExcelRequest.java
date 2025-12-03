package com.ordep.syncotable.dto.importexport.request;

import org.springframework.web.multipart.MultipartFile;
import jakarta.validation.constraints.NotNull;

public record ImportExcelRequest(
    @NotNull(message = "arquivo não pode ser nulo") MultipartFile file,
    String suggestedTitle) {
}