package com.backtest.backend.infrastructure.adapter.out.persistence;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

@Document(collection = "fixed_criteria")
public class FixedCriteriaDocument {

    @Id
    private String id = "SINGLETON_CRITERIA";
    private BigDecimal plMax;
    private BigDecimal pvpMax;
    private BigDecimal dividaEbitdaMax;
    private BigDecimal roeMin;

    public FixedCriteriaDocument() {
    }

    public FixedCriteriaDocument(BigDecimal plMax, BigDecimal pvpMax, BigDecimal dividaEbitdaMax, BigDecimal roeMin) {
        this.id = "SINGLETON_CRITERIA";
        this.plMax = plMax;
        this.pvpMax = pvpMax;
        this.dividaEbitdaMax = dividaEbitdaMax;
        this.roeMin = roeMin;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public BigDecimal getPlMax() {
        return plMax;
    }

    public void setPlMax(BigDecimal plMax) {
        this.plMax = plMax;
    }

    public BigDecimal getPvpMax() {
        return pvpMax;
    }

    public void setPvpMax(BigDecimal pvpMax) {
        this.pvpMax = pvpMax;
    }

    public BigDecimal getDividaEbitdaMax() {
        return dividaEbitdaMax;
    }

    public void setDividaEbitdaMax(BigDecimal dividaEbitdaMax) {
        this.dividaEbitdaMax = dividaEbitdaMax;
    }

    public BigDecimal getRoeMin() {
        return roeMin;
    }

    public void setRoeMin(BigDecimal roeMin) {
        this.roeMin = roeMin;
    }
}
