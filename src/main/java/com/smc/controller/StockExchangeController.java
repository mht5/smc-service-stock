package com.smc.controller;

import com.smc.domain.StockExchange;
import com.smc.service.StockExchangeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class StockExchangeController {

    @Autowired
    private StockExchangeService stockExchangeService;

    @PostMapping("/add")
    public boolean add(@RequestBody StockExchange stockExchange) throws Exception {
        return stockExchangeService.add(stockExchange);
    }

    @GetMapping("/list-all")
    public List<StockExchange> findAll() {
        return stockExchangeService.findAll();
    }

    @GetMapping("/find-by-id")
    public StockExchange findStockExchangeById(@RequestParam int id) {
        return stockExchangeService.findStockExchangeById(id);
    }
}
