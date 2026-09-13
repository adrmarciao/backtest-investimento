package com.backtest.backend.domain.service;

import com.backtest.backend.domain.entity.Purchase;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class AssetBacktestResult {
    private List<Purchase> purchases = new ArrayList<>();
    private BigDecimal totalDividendosRecebidos = BigDecimal.ZERO;
    private BigDecimal totalDividendosReinvestidos = BigDecimal.ZERO;
    private BigDecimal saldoCaixaDividendos = BigDecimal.ZERO;

    public AssetBacktestResult() {
    }

    public AssetBacktestResult(List<Purchase> purchases, BigDecimal totalDividendosRecebidos, BigDecimal totalDividendosReinvestidos, BigDecimal saldoCaixaDividendos) {
        this.purchases = purchases != null ? purchases : new ArrayList<>();
        this.totalDividendosRecebidos = totalDividendosRecebidos != null ? totalDividendosRecebidos : BigDecimal.ZERO;
        this.totalDividendosReinvestidos = totalDividendosReinvestidos != null ? totalDividendosReinvestidos : BigDecimal.ZERO;
        this.saldoCaixaDividendos = saldoCaixaDividendos != null ? saldoCaixaDividendos : BigDecimal.ZERO;
    }

    public List<Purchase> getPurchases() {
        return purchases;
    }

    public void setPurchases(List<Purchase> purchases) {
        this.purchases = purchases;
    }

    public BigDecimal getTotalDividendosRecebidos() {
        return totalDividendosRecebidos;
    }

    public void setTotalDividendosRecebidos(BigDecimal totalDividendosRecebidos) {
        this.totalDividendosRecebidos = totalDividendosRecebidos;
    }

    public BigDecimal getTotalDividendosReinvestidos() {
        return totalDividendosReinvestidos;
    }

    public void setTotalDividendosReinvestidos(BigDecimal totalDividendosReinvestidos) {
        this.totalDividendosReinvestidos = totalDividendosReinvestidos;
    }

    public BigDecimal getSaldoCaixaDividendos() {
        return saldoCaixaDividendos;
    }

    public void setSaldoCaixaDividendos(BigDecimal saldoCaixaDividendos) {
        this.saldoCaixaDividendos = saldoCaixaDividendos;
    }
}
