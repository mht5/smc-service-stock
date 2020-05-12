package com.smc.service;

import com.smc.domain.StockExchange;
import com.smc.repository.StockExchangeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StockExchangeServiceImpl implements StockExchangeService {

    @Autowired
    private StockExchangeRepository stockExchangeRepo;

    private void validateStockExchange(StockExchange stockExchange) throws Exception {
        if (stockExchange == null) {
            throw new Exception("New stock exchange can not be null.");
        }
        if (stockExchange.getName() == null || stockExchange.getName().length() < 1) {
            throw new Exception("Stock exchange name can not be null.");
        }
        if (stockExchange.getContactAddress() == null || stockExchange.getContactAddress().length() < 1) {
            throw new Exception("Stock exchange address can not be null.");
        }
        if (stockExchange.getBrief() == null || stockExchange.getBrief().length() < 1) {
            throw new Exception("Stock exchange brief can not be null.");
        }
    }

    @Override
    public boolean add(StockExchange stockExchange) throws Exception {
        try {
            validateStockExchange(stockExchange);
        } catch (Exception e) {
            throw e;
        }
        List<StockExchange> stockExchangeList = stockExchangeRepo.findByName(stockExchange.getName());
        if (!stockExchangeList.isEmpty()) {
            throw new Exception("Stock exchange with the same name already exists.");
        }
        stockExchangeRepo.save(stockExchange);
        return true;
    }

    @Override
    public List<StockExchange> findAll() {
        return stockExchangeRepo.findAll();
    }

    @Override
    public StockExchange findStockExchangeById(int id) {
        return stockExchangeRepo.findById(id);
    }

}
