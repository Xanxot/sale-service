package com.company.entity.repository;

import com.company.entity.Actual;
import com.company.entity.repository.projection.FactReportProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface ActualRepository extends JpaRepository<Actual, Long>, JpaSpecificationExecutor<Actual> {

    @Query("""
            select a from Actual a
            join a.customer c
            where (:chains is null or c.chainName in :chains)
              and (:materials is null or a.materialNo in :materials)
              and a.date = :date
            """)
    List<Actual> findByChainAndMaterialAndDate(
            @Param("chains") List<String> chains,
            @Param("materials") List<String> materials,
            @Param("date") LocalDate date
    );


    @Query(value = """
            select c.chainName as chainName,
                p.productCategoryName as productCategoryName,
                month(a.date) as month,
                sum(case when a.promoFlag = 'REGULAR' then a.volumeUnits else 0 end) as regularVolume,
                sum(case when a.promoFlag = 'PROMO' then a.volumeUnits else 0 end ) as promoVolume,
            case
                when sum(a.volumeUnits) = 0 then null else (
                            sum(case when a.promoFlag = 'PROMO' then a.volumeUnits else 0 end) * 100.0) / sum(a.volumeUnits) end as promoPercent
            from Actual a
            join a.customer c left join Product p ON a.materialNo = p.materialNo
            where a.date between :from and :to
            group by c.chainName, p.productCategoryName, month(a.date) order by month(a.date)
            """)
    List<FactReportProjection> findFactReportByDate(
            @Param("from") LocalDate from,
            @Param("to") LocalDate to
    );
}
