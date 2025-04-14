package com.company.service;

import com.company.controller.dto.FactDto;
import com.company.controller.dto.FactReportDto;
import com.company.controller.dto.FactsFilterDto;
import com.company.entity.Actual;
import com.company.entity.repository.ActualRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FinanceService {

    private final ActualRepository actualRepository;

    public List<FactDto> factByDay(FactsFilterDto factsFilterParams) {

        List<String> chains = factsFilterParams.chainNames();
        List<String> products = factsFilterParams.products();

        if (chains != null && chains.isEmpty()) {
            chains = null;
        }
        if (products != null && products.isEmpty()) {
            products = null;
        }

        return actualRepository.findByChainAndMaterialAndDate(
                chains,
                products,
                factsFilterParams.date()
        ).stream().map(Actual::toFactDto).collect(Collectors.toList());
    }

    public List<FactReportDto> analytic(LocalDate from, LocalDate to) {

        return actualRepository.findFactReportByDate(from, to)
                .stream()
                .map(row -> new FactReportDto(
                        row.getChainName(),
                        row.getProductCategoryName(),
                        row.getMonth(),
                        row.getRegularVolume(),
                        row.getPromoVolume(),
                        row.getPromoPercent()
                )).toList();

    }


}
