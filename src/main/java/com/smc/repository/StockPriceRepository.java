package com.smc.repository;

import com.smc.domain.StockPrice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface StockPriceRepository extends JpaRepository<StockPrice, Integer> {

    @Query("FROM StockPrice price WHERE price.stockExchangeId = :stockExchangeId AND price.stockCode = :stockCode " +
            "AND price.date BETWEEN :fromDate AND :toDate ORDER BY price.date ASC")
    List<StockPrice> getMissingData(@Param("stockExchangeId") int stockExchangeId, @Param("stockCode") String stockCode,
                                    @Param("fromDate") Date fromDate, @Param("toDate") Date toDate);

    @Query("FROM StockPrice price WHERE price.stockCode = :stockCode AND price.date BETWEEN :fromDate AND :toDate ORDER BY price.date ASC")
    List<StockPrice> findPriceByStockCodeAndDate(@Param("stockCode") String stockCode,
                                                 @Param("fromDate") Date fromDate,
                                                 @Param("toDate") Date toDate);
}