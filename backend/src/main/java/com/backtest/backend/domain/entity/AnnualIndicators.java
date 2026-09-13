package com.backtest.backend.domain.entity;

import java.math.BigDecimal;

public class AnnualIndicators {
    private String ticker;
    private int ano;
    private BigDecimal pl;
    private BigDecimal pvp;
    private BigDecimal dividaEbitda;
    private BigDecimal roe;
    private BigDecimal dpa;
    private BigDecimal lpa;
    private BigDecimal vpa;

    public AnnualIndicators() {
    }

    public AnnualIndicators(String ticker, int ano, BigDecimal pl, BigDecimal pvp, BigDecimal dividaEbitda, BigDecimal roe, BigDecimal dpa, BigDecimal lpa, BigDecimal vpa) {
        this.ticker = ticker != null ? ticker.trim().toUpperCase() : null;
        this.ano = ano;
        this.pl = pl;
        this.pvp = pvp;
        this.dividaEbitda = dividaEbitda;
        this.roe = roe;
        this.dpa = dpa;
        this.lpa = lpa;
        this.vpa = vpa;
    }

    public String getTicker() {
        return ticker;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker != null ? ticker.trim().toUpperCase() : null;
    }

    public int getAno() {
        return ano;
    }

    public void setAno(int ano) {
        this.ano = ano;
    }

    public BigDecimal getPl() {
        return pl;
    }

    public void setPl(BigDecimal pl) {
        this.pl = pl;
    }

    public BigDecimal getPvp() {
        return pvp;
    }

    public void setPvp(BigDecimal pvp) {
        this.pvp = pvp;
    }

    public BigDecimal getDividaEbitda() {
        return dividaEbitda;
    }

    public void setDividaEbitda(BigDecimal dividaEbitda) {
        this.dividaEbitda = dividaEbitda;
    }

    public BigDecimal getRoe() {
        return roe;
    }

    public void setRoe(BigDecimal roe) {
        this.roe = roe;
    }

    public BigDecimal getDpa() {
        return dpa;
    }

    public void setDpa(BigDecimal dpa) {
        this.dpa = dpa;
    }

    public BigDecimal getLpa() {
        return lpa;
    }

    public void setLpa(BigDecimal lpa) {
        this.lpa = lpa;
    }

    public BigDecimal getVpa() {
        return vpa;
    }

    public void setVpa(BigDecimal vpa) {
        this.vpa = vpa;
    }
}
