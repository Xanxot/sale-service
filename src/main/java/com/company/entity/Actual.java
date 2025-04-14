package com.company.entity;

import com.company.controller.dto.FactDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "actual")
public class Actual {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "ch3_ship_to_code")
    private String ch3ShipToCode;

    @Column(name = "material_no", nullable = false)
    private String materialNo;

    @Column(name = "volume_units", precision = 10, scale = 2)
    private BigDecimal volumeUnits;

    @Column(name = "actual_sales_value", precision = 10, scale = 2)
    private BigDecimal actualSalesValue;

    @Column(name = "date")
    private LocalDate date;

    @Enumerated(EnumType.STRING)
    @Column(name = "promo_flag")
    private PromoFlag promoFlag;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "ch3_ship_to_code",
            referencedColumnName = "ch3_ship_to_code",
            insertable = false,
            updatable = false)
    private Customer customer;

    @ManyToOne(optional = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "material_no",
            referencedColumnName = "material_no",
            insertable = false,
            updatable = false)
    private Product product;

    public FactDto toFactDto(){
       return new FactDto(ch3ShipToCode, materialNo, volumeUnits, actualSalesValue, date, promoFlag);
    }
}



