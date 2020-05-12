package com.smc.service;

import com.smc.domain.StockExchange;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.number.OrderingComparison.greaterThan;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class StockExchangeServiceTest {

    @Autowired
    private StockExchangeService stockExchangeService;

    @Test
    @Rollback
    public void findAllTest() {
        List<StockExchange> exchangeList = stockExchangeService.findAll();
        Assert.assertThat(exchangeList.size(), greaterThan(0));
    }

    @Test
    @Rollback
    public void findStockExchangeByIdTest() {
        int stockExchangeId = 1;
        StockExchange exchange = stockExchangeService.findStockExchangeById(stockExchangeId);
        Assert.assertThat(exchange.getId(), is(stockExchangeId));
    }

}
