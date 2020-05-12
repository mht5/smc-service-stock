package com.smc.service;

import com.smc.domain.StockExchange;

import java.util.List;

public interface StockExchangeService {

    boolean add(StockExchange stockExchange) throws Exception;

    List<StockExchange> findAll();

    StockExchange findStockExchangeById(int id);
}
