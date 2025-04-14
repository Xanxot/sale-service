package com.company.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "material_no", nullable = false)
    private String materialNo;

    @Column(name = "material_desc_rus")
    private String materialDescRus;

    @Column(name = "product_category_code")
    private String productCategoryCode;

    @Column(name = "product_category_name")
    private String productCategoryName;
}
