package com.backtest.backend.domain.entity;

import java.math.BigDecimal;
import java.time.LocalDate;

public class DividendPayment {
    private LocalDate data;
    private BigDecimal valorPorAcao;

    public DividendPayment() {
    }

    public DividendPayment(LocalDate data, BigDecimal valorPorAcao) {
        this.data = data;
        this.valorPorAcao = valorPorAcao;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public BigDecimal getValorPorAcao() {
        return valorPorAcao;
    }

    public void setValorPorAcao(BigDecimal valorPorAcao) {
        this.valorPorAcao = valorPorAcao;
    }
}
