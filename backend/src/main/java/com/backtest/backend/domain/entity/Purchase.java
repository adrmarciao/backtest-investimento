package com.backtest.backend.domain.entity;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Purchase {
    private LocalDate data;
    private String ticker;
    private BigDecimal preco;
    private BigDecimal valorAportado;
    private BigDecimal cotas;
    private BigDecimal tetoBazin;
    private BigDecimal tetoGraham;
    private Boolean isReinvestimento = false;

    public Purchase() {
    }

    public Purchase(LocalDate data, String ticker, BigDecimal preco, BigDecimal valorAportado, BigDecimal cotas, BigDecimal tetoBazin, BigDecimal tetoGraham) {
        this.data = data;
        this.ticker = ticker != null ? ticker.trim().toUpperCase() : null;
        this.preco = preco;
        this.valorAportado = valorAportado;
        this.cotas = cotas;
        this.tetoBazin = tetoBazin;
        this.tetoGraham = tetoGraham;
        this.isReinvestimento = false;
    }

    public Purchase(LocalDate data, String ticker, BigDecimal preco, BigDecimal valorAportado, BigDecimal cotas, BigDecimal tetoBazin, BigDecimal tetoGraham, Boolean isReinvestimento) {
        this(data, ticker, preco, valorAportado, cotas, tetoBazin, tetoGraham);
        this.isReinvestimento = isReinvestimento != null && isReinvestimento;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public String getTicker() {
        return ticker;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker != null ? ticker.trim().toUpperCase() : null;
    }

    public BigDecimal getPreco() {
        return preco;
    }

    public void setPreco(BigDecimal preco) {
        this.preco = preco;
    }

    public BigDecimal getValorAportado() {
        return valorAportado;
    }

    public void setValorAportado(BigDecimal valorAportado) {
        this.valorAportado = valorAportado;
    }

    public BigDecimal getCotas() {
        return cotas;
    }

    public void setCotas(BigDecimal cotas) {
        this.cotas = cotas;
    }

    public BigDecimal getTetoBazin() {
        return tetoBazin;
    }

    public void setTetoBazin(BigDecimal tetoBazin) {
        this.tetoBazin = tetoBazin;
    }

    public BigDecimal getTetoGraham() {
        return tetoGraham;
    }

    public void setTetoGraham(BigDecimal tetoGraham) {
        this.tetoGraham = tetoGraham;
    }

    public Boolean getIsReinvestimento() {
        return isReinvestimento;
    }

    public void setIsReinvestimento(Boolean isReinvestimento) {
        this.isReinvestimento = isReinvestimento != null && isReinvestimento;
    }
}

