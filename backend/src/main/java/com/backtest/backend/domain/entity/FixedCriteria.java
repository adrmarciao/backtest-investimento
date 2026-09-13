package com.backtest.backend.domain.entity;

import java.math.BigDecimal;

public class FixedCriteria {
    private BigDecimal plMax;
    private BigDecimal pvpMax;
    private BigDecimal dividaEbitdaMax;
    private BigDecimal roeMin;

    public FixedCriteria() {
    }

    public FixedCriteria(BigDecimal plMax, BigDecimal pvpMax, BigDecimal dividaEbitdaMax, BigDecimal roeMin) {
        this.plMax = plMax;
        this.pvpMax = pvpMax;
        this.dividaEbitdaMax = dividaEbitdaMax;
        this.roeMin = roeMin;
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
