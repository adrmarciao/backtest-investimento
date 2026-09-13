package com.backtest.backend.domain.port.out;

import com.backtest.backend.domain.entity.AnnualIndicators;
import java.util.List;
import java.util.Optional;

public interface AnnualIndicatorsRepositoryPort {
    AnnualIndicators save(AnnualIndicators indicators);
    Optional<AnnualIndicators> findByTickerAndAno(String ticker, int ano);
    List<AnnualIndicators> findByTicker(String ticker);
    void deleteByTickerAndAno(String ticker, int ano);
    void deleteByTicker(String ticker);
}
