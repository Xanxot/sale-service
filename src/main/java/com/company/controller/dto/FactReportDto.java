package com.company.controller.dto;

import java.math.BigDecimal;

public record FactReportDto(
        String chainName,
        String productCategoryName,
        Integer month,
        BigDecimal regularVolume,
        BigDecimal promoVolume,
        Double promoShare

) {

}
