package com.company.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "customers")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "ch3_ship_to_code", nullable = false)
    private String ch3ShipToCode;

    @Column(name = "chain_name")
    private String chainName;

    @Column(name = "ship_to_name")
    private String shipToName;

    @OneToMany(mappedBy = "customer", fetch = FetchType.LAZY)
    private List<Actual> actuals;
}