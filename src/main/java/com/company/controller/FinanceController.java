package com.company.controller;

import com.company.controller.dto.PriceDto;
import com.company.controller.dto.PriceUpdateDto;
import com.company.service.PriceService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/finance")
@RequiredArgsConstructor
public class FinanceController {

    private final PriceService priceService;

    @PostMapping("/")
    @Operation(summary = "Создать Price")
    public ResponseEntity<PriceDto> createFinance(@RequestBody @Valid PriceDto priceDto) {
        return ResponseEntity.ok(priceService.saveFinance(priceDto));
    }

    @PutMapping("/")
    @Operation(summary = "Обновить Price")
    public ResponseEntity<PriceDto> updateFinance(@RequestBody @Valid PriceUpdateDto priceDto) {
        return ResponseEntity.ok(priceService.updateFinance(priceDto));
    }

    @GetMapping("/all")
    @Operation(summary = "Получить Price")
    public ResponseEntity<List<PriceDto>> getAllFinance() {
        return ResponseEntity.ok(priceService.getAllFinance());
    }
    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить Price")
    public ResponseEntity<Void> deleteFinanceById(@PathVariable Long id) {
        priceService.deleteFinance(id);
        return ResponseEntity.noContent().build();
    }
}
