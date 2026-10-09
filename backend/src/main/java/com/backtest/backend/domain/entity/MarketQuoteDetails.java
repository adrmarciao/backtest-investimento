package com.backtest.backend.domain.entity;

import java.math.BigDecimal;

public record MarketQuoteDetails(
    BigDecimal price,
    BigDecimal high52Week,
    BigDecimal low52Week
) {}
