package com.smc.repository;

import com.smc.domain.CompanyStockExchange;
import com.smc.domain.SCEKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CompanyStockExchangeRepository extends JpaRepository<CompanyStockExchange, SCEKey> {

    List<CompanyStockExchange> findByCompanyId(int companyId);

    List<CompanyStockExchange> findByStockExchangeId(int stockExchangeId);

    List<CompanyStockExchange> findByStockCode(String stockCode);

    @Query("FROM CompanyStockExchange cse WHERE cse.stockExchangeId = :stockExchangeId " +
            "AND cse.companyId = :companyId1 OR cse.companyId = :companyId2")
    List<CompanyStockExchange> findByStockExchangeIdAndCompanyIds(@Param("stockExchangeId") int stockExchangeId,
                                                                  @Param("companyId1") int companyId1,
                                                                  @Param("companyId2") int companyId2);

}