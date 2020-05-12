package com.smc.domain;

import java.io.Serializable;

public class SCEKey implements Serializable {

    private static final long serialVersionUID = -6775118036814123486L;

    private Integer companyId;

    private Integer stockExchangeId;

    public SCEKey() {}

    public SCEKey(Integer companyId, Integer stockExchangeId) {
        this.companyId = companyId;
        this.stockExchangeId = stockExchangeId;
    }

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
}
