package com.company.controller.dto;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record PriceUpdateDto(
        @NotNull(message = "id is required")
        Long id,
        String chainName,
        BigDecimal regularPricePerUnit
) {

}
