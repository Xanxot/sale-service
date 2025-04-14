package com.company.controller.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record PriceDto(
        Long id,
        @NotBlank(message = "chainName is required")
        String chainName,
        @NotNull(message = "materialNo is required")
        String materialNo,
        @NotNull(message = "regularPricePerUnit is required")
        BigDecimal regularPricePerUnit
) {
}
