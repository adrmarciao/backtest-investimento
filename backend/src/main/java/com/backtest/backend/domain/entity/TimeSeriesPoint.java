package com.backtest.backend.domain.entity;

import java.math.BigDecimal;
import java.time.LocalDate;

public class TimeSeriesPoint {
    private LocalDate data;
    private BigDecimal valorInvestidoAcumulado;
    private BigDecimal valorPatrimonio;
    private BigDecimal valorIbovespa;
    private BigDecimal patrimonioNormalizado; // base 100
    private BigDecimal ibovespaNormalizado;   // base 100

    public TimeSeriesPoint() {
    }

    public TimeSeriesPoint(LocalDate data, BigDecimal valorInvestidoAcumulado, BigDecimal valorPatrimonio, BigDecimal valorIbovespa, BigDecimal patrimonioNormalizado, BigDecimal ibovespaNormalizado) {
        this.data = data;
        this.valorInvestidoAcumulado = valorInvestidoAcumulado;
        this.valorPatrimonio = valorPatrimonio;
        this.valorIbovespa = valorIbovespa;
        this.patrimonioNormalizado = patrimonioNormalizado;
        this.ibovespaNormalizado = ibovespaNormalizado;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public BigDecimal getValorInvestidoAcumulado() {
        return valorInvestidoAcumulado;
    }

    public void setValorInvestidoAcumulado(BigDecimal valorInvestidoAcumulado) {
        this.valorInvestidoAcumulado = valorInvestidoAcumulado;
    }

    public BigDecimal getValorPatrimonio() {
        return valorPatrimonio;
    }

    public void setValorPatrimonio(BigDecimal valorPatrimonio) {
        this.valorPatrimonio = valorPatrimonio;
    }

    public BigDecimal getValorIbovespa() {
        return valorIbovespa;
    }

    public void setValorIbovespa(BigDecimal valorIbovespa) {
        this.valorIbovespa = valorIbovespa;
    }

    public BigDecimal getPatrimonioNormalizado() {
        return patrimonioNormalizado;
    }

    public void setPatrimonioNormalizado(BigDecimal patrimonioNormalizado) {
        this.patrimonioNormalizado = patrimonioNormalizado;
    }

    public BigDecimal getIbovespaNormalizado() {
        return ibovespaNormalizado;
    }

    public void setIbovespaNormalizado(BigDecimal ibovespaNormalizado) {
        this.ibovespaNormalizado = ibovespaNormalizado;
    }
}
