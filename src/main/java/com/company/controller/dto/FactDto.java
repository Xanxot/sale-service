package com.company.controller.dto;

import com.company.entity.PromoFlag;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.time.LocalDate;

public record FactDto(
        String ch3ShipToCode,
        String materialNo,
        BigDecimal volumeUnits,
        BigDecimal actualSalesValue,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        LocalDate date,
        PromoFlag promoFlag
) {
}

