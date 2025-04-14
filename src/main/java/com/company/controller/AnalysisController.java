package com.company.controller;

import com.company.controller.dto.FactDto;
import com.company.controller.dto.FactReportDto;
import com.company.controller.dto.FactsFilterDto;
import com.company.service.FinanceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/analysis")
@RequiredArgsConstructor
public class AnalysisController {

    private final FinanceService financeService;

    @PostMapping("/all")
    @Operation(summary = "Выгрузка фактов по дням, согласно фильтрации по списку наименований сетей и списку продуктов")
    public ResponseEntity<List<FactDto>> getAllFinance(@RequestBody @Valid FactsFilterDto filterParam) {
        return ResponseEntity.ok(financeService.factByDay(filterParam));
    }

    @GetMapping("/")
    @Operation(summary = "Выгрузка фактов продаж с учётом признака промо")
    public ResponseEntity<List<FactReportDto>> getAllFinance(
            @RequestParam("from")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            @Schema(example = "2021-01-01") LocalDate from,
            @RequestParam("to")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            @Schema(example = "2021-12-31")
            LocalDate to) {
        return ResponseEntity.ok(financeService.analytic(from, to));
    }
}
