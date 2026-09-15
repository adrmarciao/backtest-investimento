package com.backtest.backend.domain.entity;

import java.math.BigDecimal;

public record AssetAllocationItem(
    String ticker,
    String setor,
    BigDecimal precoAtual,
    BigDecimal pesoEfetivo,
    BigDecimal percentualAlocado,
    BigDecimal valorAlocado,
    int qtdSugerida,
    int ajusteManualQtd,
    int qtdFinal,
    BigDecimal totalGasto,
    BigDecimal margemBazin,
    BigDecimal tetoBazin,
    BigDecimal precoGraham
) {}
