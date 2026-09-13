package com.backtest.backend.domain.entity;

import java.math.BigDecimal;
import java.util.Objects;

public class Asset {
    private String ticker;
    private BigDecimal valorAporte;
    private Periodicity periodicidade;

    public Asset() {
    }

    public Asset(String ticker, BigDecimal valorAporte, Periodicity periodicidade) {
        this.ticker = ticker != null ? ticker.trim().toUpperCase() : null;
        this.valorAporte = valorAporte;
        this.periodicidade = periodicidade;
    }

    public String getTicker() {
        return ticker;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker != null ? ticker.trim().toUpperCase() : null;
    }

    public BigDecimal getValorAporte() {
        return valorAporte;
    }

    public void setValorAporte(BigDecimal valorAporte) {
        this.valorAporte = valorAporte;
    }

    public Periodicity getPeriodicidade() {
        return periodicidade;
    }

    public void setPeriodicidade(Periodicity periodicidade) {
        this.periodicidade = periodicidade;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Asset asset = (Asset) o;
        return Objects.equals(ticker, asset.ticker);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ticker);
    }
}
