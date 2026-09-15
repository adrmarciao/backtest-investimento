package com.backtest.backend.infrastructure.adapter.out.persistence;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

@Document(collection = "purchase_plan_assets")
public class PurchasePlanAssetDocument {

    @Id
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

    public PurchasePlanAssetDocument() {
    }

    public PurchasePlanAssetDocument(String id, String ticker, String setor, BigDecimal peso, BigDecimal habilitado,
                                     BigDecimal precoAtual, BigDecimal dpaForecast, BigDecimal tetoBazin,
                                     BigDecimal lpa, BigDecimal vpa, BigDecimal precoGraham, BigDecimal margemBazin,
                                     int ajusteManualQtd, int ordem) {
        this.id = id;
        this.ticker = ticker;
        this.setor = setor;
        this.peso = peso;
        this.habilitado = habilitado;
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
        this.ticker = ticker;
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
}
