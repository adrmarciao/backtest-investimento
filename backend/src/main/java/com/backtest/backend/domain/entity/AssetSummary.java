package com.backtest.backend.domain.entity;

import java.math.BigDecimal;

public class AssetSummary {
    private String ticker;
    private BigDecimal totalAportado;
    private BigDecimal totalCotas;
    private BigDecimal precoMedio;
    private BigDecimal precoAtual;
    private BigDecimal valorAtual;
    private BigDecimal retornoPercentual;

    public AssetSummary() {
    }

    public AssetSummary(String ticker, BigDecimal totalAportado, BigDecimal totalCotas, BigDecimal precoMedio, BigDecimal precoAtual, BigDecimal valorAtual, BigDecimal retornoPercentual) {
        this.ticker = ticker != null ? ticker.trim().toUpperCase() : null;
        this.totalAportado = totalAportado;
        this.totalCotas = totalCotas;
        this.precoMedio = precoMedio;
        this.precoAtual = precoAtual;
        this.valorAtual = valorAtual;
        this.retornoPercentual = retornoPercentual;
    }

    public String getTicker() {
        return ticker;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker != null ? ticker.trim().toUpperCase() : null;
    }

    public BigDecimal getTotalAportado() {
        return totalAportado;
    }

    public void setTotalAportado(BigDecimal totalAportado) {
        this.totalAportado = totalAportado;
    }

    public BigDecimal getTotalCotas() {
        return totalCotas;
    }

    public void setTotalCotas(BigDecimal totalCotas) {
        this.totalCotas = totalCotas;
    }

    public BigDecimal getPrecoMedio() {
        return precoMedio;
    }

    public void setPrecoMedio(BigDecimal precoMedio) {
        this.precoMedio = precoMedio;
    }

    public BigDecimal getPrecoAtual() {
        return precoAtual;
    }

    public void setPrecoAtual(BigDecimal precoAtual) {
        this.precoAtual = precoAtual;
    }

    public BigDecimal getValorAtual() {
        return valorAtual;
    }

    public void setValorAtual(BigDecimal valorAtual) {
        this.valorAtual = valorAtual;
    }

    public BigDecimal getRetornoPercentual() {
        return retornoPercentual;
    }

    public void setRetornoPercentual(BigDecimal retornoPercentual) {
        this.retornoPercentual = retornoPercentual;
    }
}
