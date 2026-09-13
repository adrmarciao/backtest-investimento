package com.backtest.backend.infrastructure.adapter.out.persistence;

import com.backtest.backend.domain.entity.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Document(collection = "backtest_results")
public class BacktestResultDocument {

    @Id
    private String id;
    private LocalDateTime dataExecucao;
    private List<Purchase> compras = new ArrayList<>();
    private List<TimeSeriesPoint> serieTemporal = new ArrayList<>();
    private List<AssetSummary> resumoAtivos = new ArrayList<>();
    private List<String> anosIgnorados = new ArrayList<>();
    private BigDecimal retornoTotal;
    private BigDecimal cagr;
    private BigDecimal maxDrawdown;
    private BigDecimal sharpeRatio;
    private BigDecimal alfaIbov;
    private BigDecimal totalAportado;
    private BigDecimal valorFinal;
    private BigDecimal totalDividendosRecebidos;
    private BigDecimal totalDividendosReinvestidos;
    private BigDecimal saldoCaixaDividendos;

    public BacktestResultDocument() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LocalDateTime getDataExecucao() {
        return dataExecucao;
    }

    public void setDataExecucao(LocalDateTime dataExecucao) {
        this.dataExecucao = dataExecucao;
    }

    public List<Purchase> getCompras() {
        return compras;
    }

    public void setCompras(List<Purchase> compras) {
        this.compras = compras;
    }

    public List<TimeSeriesPoint> getSerieTemporal() {
        return serieTemporal;
    }

    public void setSerieTemporal(List<TimeSeriesPoint> serieTemporal) {
        this.serieTemporal = serieTemporal;
    }

    public List<AssetSummary> getResumoAtivos() {
        return resumoAtivos;
    }

    public void setResumoAtivos(List<AssetSummary> resumoAtivos) {
        this.resumoAtivos = resumoAtivos;
    }

    public List<String> getAnosIgnorados() {
        return anosIgnorados;
    }

    public void setAnosIgnorados(List<String> anosIgnorados) {
        this.anosIgnorados = anosIgnorados;
    }

    public BigDecimal getRetornoTotal() {
        return retornoTotal;
    }

    public void setRetornoTotal(BigDecimal retornoTotal) {
        this.retornoTotal = retornoTotal;
    }

    public BigDecimal getCagr() {
        return cagr;
    }

    public void setCagr(BigDecimal cagr) {
        this.cagr = cagr;
    }

    public BigDecimal getMaxDrawdown() {
        return maxDrawdown;
    }

    public void setMaxDrawdown(BigDecimal maxDrawdown) {
        this.maxDrawdown = maxDrawdown;
    }

    public BigDecimal getSharpeRatio() {
        return sharpeRatio;
    }

    public void setSharpeRatio(BigDecimal sharpeRatio) {
        this.sharpeRatio = sharpeRatio;
    }

    public BigDecimal getAlfaIbov() {
        return alfaIbov;
    }

    public void setAlfaIbov(BigDecimal alfaIbov) {
        this.alfaIbov = alfaIbov;
    }

    public BigDecimal getTotalAportado() {
        return totalAportado;
    }

    public void setTotalAportado(BigDecimal totalAportado) {
        this.totalAportado = totalAportado;
    }

    public BigDecimal getValorFinal() {
        return valorFinal;
    }

    public void setValorFinal(BigDecimal valorFinal) {
        this.valorFinal = valorFinal;
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

