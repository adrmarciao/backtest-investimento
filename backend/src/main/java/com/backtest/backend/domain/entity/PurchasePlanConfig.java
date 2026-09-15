package com.backtest.backend.domain.entity;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

public class PurchasePlanConfig {
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

    public PurchasePlanConfig() {
        this.id = "default";
        this.saldoTotal = BigDecimal.ZERO;
        this.parcelasMeses = 12;
        this.periodicidade = Periodicity.SEMANAL;
        this.benchmark = "IBOV";
    }

    public PurchasePlanConfig(String id, BigDecimal saldoTotal, int parcelasMeses, Periodicity periodicidade,
                              BigDecimal aporteRodadaManual, String benchmark, BigDecimal fiboUp,
                              BigDecimal fiboDown, BigDecimal altaAno, String tokenBrapi) {
        this.id = id != null ? id : "default";
        this.saldoTotal = saldoTotal != null ? saldoTotal : BigDecimal.ZERO;
        this.parcelasMeses = parcelasMeses > 0 ? parcelasMeses : 12;
        this.periodicidade = periodicidade != null ? periodicidade : Periodicity.SEMANAL;
        this.aporteRodadaManual = aporteRodadaManual;
        this.benchmark = benchmark != null ? benchmark.trim().toUpperCase() : "IBOV";
        this.fiboUp = fiboUp;
        this.fiboDown = fiboDown;
        this.altaAno = altaAno;
        this.tokenBrapi = tokenBrapi;
    }

    public BigDecimal calculateAporteRodada() {
        if (aporteRodadaManual != null && aporteRodadaManual.compareTo(BigDecimal.ZERO) > 0) {
            return aporteRodadaManual.setScale(2, RoundingMode.HALF_UP);
        }
        if (saldoTotal == null || saldoTotal.compareTo(BigDecimal.ZERO) <= 0 || parcelasMeses <= 0) {
            return BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
        }
        if (periodicidade == Periodicity.MENSAL) {
            return saldoTotal.divide(BigDecimal.valueOf(parcelasMeses), 2, RoundingMode.HALF_UP);
        }
        // SEMANAL = Saldo / (parcelas * 4)
        return saldoTotal.divide(BigDecimal.valueOf((long) parcelasMeses * 4), 2, RoundingMode.HALF_UP);
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
        this.benchmark = benchmark != null ? benchmark.trim().toUpperCase() : "IBOV";
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PurchasePlanConfig that = (PurchasePlanConfig) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
