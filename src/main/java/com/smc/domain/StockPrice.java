package com.smc.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Time;
import java.sql.Date;

@Entity
@Table(name = "STOCK_PRICE")
public class StockPrice implements Serializable {

    private static final long serialVersionUID = -4081466274129982833L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @Column(name = "STOCK_EXCHANGE_ID", nullable = false)
    private int stockExchangeId;

    @Column(name = "STOCK_CODE", nullable = false)
    private String stockCode;

    @Column(name = "PRICE", nullable = false)
    private BigDecimal price;

    @Column(name = "DATE")
    private Date date;

    @Column(name = "TIME")
    private Time time;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getStockExchangeId() {
        return stockExchangeId;
    }

    public void setStockExchangeId(int stockExchangeId) {
        this.stockExchangeId = stockExchangeId;
    }

    public String getStockCode() {
        return stockCode;
    }

    public void setStockCode(String stockCode) {
        this.stockCode = stockCode;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Time getTime() {
        return time;
    }

    public void setTime(Time time) {
        this.time = time;
    }
}
