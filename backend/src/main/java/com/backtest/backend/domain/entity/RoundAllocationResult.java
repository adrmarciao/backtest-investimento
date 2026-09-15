package com.backtest.backend.domain.entity;

import java.math.BigDecimal;
import java.util.List;

public record RoundAllocationResult(
    BigDecimal saldoTotal,
    BigDecimal aporteRodada,
    boolean aporteManual,
    BigDecimal totalGasto,
    BigDecimal sobraCaixa,
    int totalAcoesCompradas,
    List<AssetAllocationItem> itens,
    String boletaTexto
) {}
