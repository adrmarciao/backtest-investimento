package com.backtest.backend.domain.entity;

import java.math.BigDecimal;

public record BenchmarkStatus(
    String benchmark,
    BigDecimal precoAtual,
    BigDecimal altaAno,
    BigDecimal drawdownPercent,
    BigDecimal fiboUp,
    BigDecimal fiboDown,
    BigDecimal fiboRetractionPercent,
    String statusDescricao
) {}
