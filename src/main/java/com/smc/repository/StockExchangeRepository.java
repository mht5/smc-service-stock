package com.smc.repository;

import com.smc.domain.StockExchange;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StockExchangeRepository extends JpaRepository<StockExchange, Integer> {

    List<StockExchange> findByName(String name);

    StockExchange findById(int id);

}