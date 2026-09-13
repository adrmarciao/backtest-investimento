package com.backtest.backend.domain.port.in;

import com.backtest.backend.domain.entity.AnnualIndicators;
import java.util.List;
import java.util.Optional;

public interface SaveAnnualIndicatorsPort {
    AnnualIndicators saveIndicators(AnnualIndicators indicators);
    List<AnnualIndicators> getIndicatorsByTicker(String ticker);
    Optional<AnnualIndicators> getIndicatorsByTickerAndYear(String ticker, int year);
    void deleteIndicatorsByTickerAndYear(String ticker, int year);
}
