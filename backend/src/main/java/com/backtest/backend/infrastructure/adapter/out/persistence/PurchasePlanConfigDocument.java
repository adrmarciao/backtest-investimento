package com.backtest.backend.infrastructure.adapter.out.persistence;

import com.backtest.backend.domain.entity.Periodicity;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

@Document(collection = "purchase_plan_config")
public class PurchasePlanConfigDocument {

    @Id
    private String id;
    private BigDecimal saldoTotal;
    private int parcelasMeses;
    private Periodicity periodicidade;
    private BigDecimal aporteRodadaManual;
    private String benchmark;
    private BigDecimal fiboUp;
    private BigDecimal fiboDown;
    private BigDecimal altaAno;
    private String tokenBrapi;

    public PurchasePlanConfigDocument() {
    }

    public PurchasePlanConfigDocument(String id, BigDecimal saldoTotal, int parcelasMeses, Periodicity periodicidade,
                                      BigDecimal aporteRodadaManual, String benchmark, BigDecimal fiboUp,
                                      BigDecimal fiboDown, BigDecimal altaAno, String tokenBrapi) {
        this.id = id;
        this.saldoTotal = saldoTotal;
        this.parcelasMeses = parcelasMeses;
        this.periodicidade = periodicidade;
        this.aporteRodadaManual = aporteRodadaManual;
        this.benchmark = benchmark;
        this.fiboUp = fiboUp;
        this.fiboDown = fiboDown;
        this.altaAno = altaAno;
        this.tokenBrapi = tokenBrapi;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public BigDecimal getSaldoTotal() {
        return saldoTotal;
    }

    public void setSaldoTotal(BigDecimal saldoTotal) {
        this.saldoTotal = saldoTotal;
    }

    public int getParcelasMeses() {
        return parcelasMeses;
    }

    public void setParcelasMeses(int parcelasMeses) {
        this.parcelasMeses = parcelasMeses;
    }

    public Periodicity getPeriodicidade() {
        return periodicidade;
    }

    public void setPeriodicidade(Periodicity periodicidade) {
        this.periodicidade = periodicidade;
    }

    public BigDecimal getAporteRodadaManual() {
        return aporteRodadaManual;
    }

    public void setAporteRodadaManual(BigDecimal aporteRodadaManual) {
        this.aporteRodadaManual = aporteRodadaManual;
    }

    public String getBenchmark() {
        return benchmark;
    }

    public void setBenchmark(String benchmark) {
        this.benchmark = benchmark;
    }

    public BigDecimal getFiboUp() {
        return fiboUp;
    }

    public void setFiboUp(BigDecimal fiboUp) {
        this.fiboUp = fiboUp;
    }

    public BigDecimal getFiboDown() {
        return fiboDown;
    }

    public void setFiboDown(BigDecimal fiboDown) {
        this.fiboDown = fiboDown;
    }

    public BigDecimal getAltaAno() {
        return altaAno;
    }

    public void setAltaAno(BigDecimal altaAno) {
        this.altaAno = altaAno;
    }

    public String getTokenBrapi() {
        return tokenBrapi;
    }

    public void setTokenBrapi(String tokenBrapi) {
        this.tokenBrapi = tokenBrapi;
    }
}
