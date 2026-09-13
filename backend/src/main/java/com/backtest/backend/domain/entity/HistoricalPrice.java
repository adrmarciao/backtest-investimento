package com.backtest.backend.domain.entity;

import java.math.BigDecimal;
import java.time.LocalDate;

public class HistoricalPrice {
    private LocalDate data;
    private BigDecimal precoFechamento;

    public HistoricalPrice() {
    }

    public HistoricalPrice(LocalDate data, BigDecimal precoFechamento) {
        this.data = data;
        this.precoFechamento = precoFechamento;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public BigDecimal getPrecoFechamento() {
        return precoFechamento;
    }

    public void setPrecoFechamento(BigDecimal precoFechamento) {
        this.precoFechamento = precoFechamento;
    }
}
