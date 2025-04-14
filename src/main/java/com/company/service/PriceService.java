package com.company.service;


import com.company.controller.dto.PriceDto;
import com.company.controller.dto.PriceUpdateDto;
import com.company.entity.Price;
import com.company.entity.repository.PriceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.Optional.ofNullable;

@Service
@RequiredArgsConstructor
public class PriceService {
    private final PriceRepository priceRepository;

    public List<PriceDto> getAllFinance() {
        return priceRepository.findAll().stream().map(Price::toPriceDto).toList();
    }

    @Transactional
    public void deleteFinance(Long id) {
        priceRepository.deleteById(id);
    }

    @Transactional
    public PriceDto saveFinance(PriceDto priceDto) {
        var price = new Price().setChainName(priceDto.chainName())
                .setMaterialNo(priceDto.materialNo())
                .setRegularPricePerUnit(priceDto.regularPricePerUnit());

        priceRepository.save(price);
        return price.toPriceDto();
    }

    @Transactional
    public PriceDto updateFinance(PriceUpdateDto priceUpdateDto) {
        var price = priceRepository.findById(priceUpdateDto.id()).orElseThrow(() -> new RuntimeException("Price not found with id = " + priceUpdateDto.id()));

        ofNullable(priceUpdateDto.chainName()).ifPresent(price::setChainName);
        ofNullable(priceUpdateDto.regularPricePerUnit()).ifPresent(price::setRegularPricePerUnit);

        priceRepository.save(price);
        return price.toPriceDto();
    }
}
