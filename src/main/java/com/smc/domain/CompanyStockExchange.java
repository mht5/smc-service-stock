package com.smc.domain;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "COMPANY_STOCK_EXCHANGE")
@IdClass(SCEKey.class)
public class CompanyStockExchange implements Serializable {

    private static final long serialVersionUID = 7569647090917328266L;

    public CompanyStockExchange() {}

    public CompanyStockExchange(Integer companyId, Integer stockExchangeId, String stockCode) {
        this.companyId = companyId;
        this.stockExchangeId = stockExchangeId;
        this.stockCode = stockCode;
    }

    @Id
    @Column(name = "COMPANY_ID", nullable = false)
    private Integer companyId;

    @Id
    @Column(name = "STOCK_EXCHANGE_ID", nullable = false)
    private Integer stockExchangeId;

    @Column(name = "STOCK_CODE", length = 10, nullable = false)
    private String stockCode;

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public Integer getStockExchangeId() {
        return stockExchangeId;
    }

    public void setStockExchangeId(Integer stockExchangeId) {
        this.stockExchangeId = stockExchangeId;
    }

    public String getStockCode() {
        return stockCode;
    }

    public void setStockCode(String stockCode) {
        this.stockCode = stockCode;
    }
}
