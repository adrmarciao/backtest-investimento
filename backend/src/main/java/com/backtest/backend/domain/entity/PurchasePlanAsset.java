package com.backtest.backend.domain.entity;

import com.backtest.backend.domain.service.PriceCeilingCalculator;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

public class PurchasePlanAsset {
    private String id;
    private String ticker;
    private String setor;
    private BigDecimal peso;
    private BigDecimal habilitado;
    private BigDecimal precoAtual;
    private BigDecimal dpaForecast;
    private BigDecimal tetoBazin;
    private BigDecimal lpa;
    private BigDecimal vpa;
    private BigDecimal precoGraham;
    private BigDecimal margemBazin;
    private int ajusteManualQtd;
    private int ordem;

    public PurchasePlanAsset() {
        this.peso = BigDecimal.ONE;
        this.habilitado = BigDecimal.ONE;
        this.ajusteManualQtd = 0;
        this.ordem = 0;
    }

    public PurchasePlanAsset(String id, String ticker, String setor, BigDecimal peso, BigDecimal habilitado,
                             BigDecimal precoAtual, BigDecimal dpaForecast, BigDecimal tetoBazin,
                             BigDecimal lpa, BigDecimal vpa, BigDecimal precoGraham, BigDecimal margemBazin,
                             int ajusteManualQtd, int ordem) {
        this.id = id;
        this.ticker = ticker != null ? ticker.trim().toUpperCase() : null;
        this.setor = setor;
        this.peso = peso != null ? peso : BigDecimal.ONE;
        this.habilitado = habilitado != null ? habilitado : BigDecimal.ONE;
        this.precoAtual = precoAtual;
        this.dpaForecast = dpaForecast;
        this.tetoBazin = tetoBazin;
        this.lpa = lpa;
        this.vpa = vpa;
        this.precoGraham = precoGraham;
        this.margemBazin = margemBazin;
        this.ajusteManualQtd = ajusteManualQtd;
        this.ordem = ordem;
    }

    public void recalculateValuations() {
        this.tetoBazin = PriceCeilingCalculator.calculateBazin(this.dpaForecast);
        this.precoGraham = PriceCeilingCalculator.calculateGraham(this.lpa, this.vpa);
        this.margemBazin = calculateMargemBazin(this.precoAtual, this.tetoBazin);
    }

    public static BigDecimal calculateMargemBazin(BigDecimal preco, BigDecimal teto) {
        if (preco == null || preco.compareTo(BigDecimal.ZERO) <= 0 || teto == null) {
            return null;
        }
        return teto.subtract(preco)
                .divide(preco, 4, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100))
                .setScale(2, RoundingMode.HALF_UP);
    }

    public boolean isHabilitado() {
        return habilitado != null && habilitado.compareTo(BigDecimal.ZERO) > 0;
    }

    public BigDecimal getEffectiveWeight() {
        if (!isHabilitado() || peso == null || peso.compareTo(BigDecimal.ZERO) <= 0) {
            return BigDecimal.ZERO;
        }
        return peso.multiply(habilitado);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTicker() {
        return ticker;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker != null ? ticker.trim().toUpperCase() : null;
    }

    public String getSetor() {
        return setor;
    }

    public void setSetor(String setor) {
        this.setor = setor;
    }

    public BigDecimal getPeso() {
        return peso;
    }

    public void setPeso(BigDecimal peso) {
        this.peso = peso;
    }

    public BigDecimal getHabilitado() {
        return habilitado;
    }

    public void setHabilitado(BigDecimal habilitado) {
        this.habilitado = habilitado;
    }

    public BigDecimal getPrecoAtual() {
        return precoAtual;
    }

    public void setPrecoAtual(BigDecimal precoAtual) {
        this.precoAtual = precoAtual;
    }

    public BigDecimal getDpaForecast() {
        return dpaForecast;
    }

    public void setDpaForecast(BigDecimal dpaForecast) {
        this.dpaForecast = dpaForecast;
    }

    public BigDecimal getTetoBazin() {
        return tetoBazin;
    }

    public void setTetoBazin(BigDecimal tetoBazin) {
        this.tetoBazin = tetoBazin;
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

    public BigDecimal getPrecoGraham() {
        return precoGraham;
    }

    public void setPrecoGraham(BigDecimal precoGraham) {
        this.precoGraham = precoGraham;
    }

    public BigDecimal getMargemBazin() {
        return margemBazin;
    }

    public void setMargemBazin(BigDecimal margemBazin) {
        this.margemBazin = margemBazin;
    }

    public int getAjusteManualQtd() {
        return ajusteManualQtd;
    }

    public void setAjusteManualQtd(int ajusteManualQtd) {
        this.ajusteManualQtd = ajusteManualQtd;
    }

    public int getOrdem() {
        return ordem;
    }

    public void setOrdem(int ordem) {
        this.ordem = ordem;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PurchasePlanAsset that = (PurchasePlanAsset) o;
        return Objects.equals(ticker, that.ticker);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ticker);
    }
}
