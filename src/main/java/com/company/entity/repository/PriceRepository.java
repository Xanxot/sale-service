package com.company.entity.repository;


import com.company.entity.Price;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;


public interface PriceRepository extends JpaRepository<Price, Long>, JpaSpecificationExecutor<Price> {

}
