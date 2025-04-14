package com.company.entity;

import com.company.controller.dto.PriceDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "price")
public class Price {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "chain_name", nullable = false)
    private String chainName;

    @Column(name = "material_no", nullable = false)
    private String materialNo;

    @Column(name = "regular_price_per_unit", precision = 10, scale = 2)
    private BigDecimal regularPricePerUnit;

    public PriceDto toPriceDto(){
        return new PriceDto(id, chainName, materialNo, regularPricePerUnit);
    }
}
