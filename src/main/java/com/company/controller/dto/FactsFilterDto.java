package com.company.controller.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.annotation.Nullable;

import java.time.LocalDate;
import java.util.List;

public record FactsFilterDto(
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        LocalDate date,

        @Nullable
        List<String> chainNames,

        @Nullable
        List<String> products
) {
}
