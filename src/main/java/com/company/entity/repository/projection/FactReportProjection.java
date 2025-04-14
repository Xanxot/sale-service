package com.company.entity.repository.projection;

import java.math.BigDecimal;

public interface FactReportProjection {
    String getChainName();
    String getProductCategoryName();
    Integer getMonth();
    BigDecimal getRegularVolume();
    BigDecimal getPromoVolume();
    Double getPromoPercent();
}
