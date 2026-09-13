package com.backtest.backend.application.usecase;

import com.backtest.backend.domain.entity.AnnualIndicators;
import com.backtest.backend.domain.port.in.SaveAnnualIndicatorsPort;
import com.backtest.backend.domain.port.out.AnnualIndicatorsRepositoryPort;

import java.util.List;
import java.util.Optional;

public class SaveAnnualIndicatorsUseCase implements SaveAnnualIndicatorsPort {

    private final AnnualIndicatorsRepositoryPort repositoryPort;

    public SaveAnnualIndicatorsUseCase(AnnualIndicatorsRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public AnnualIndicators saveIndicators(AnnualIndicators indicators) {
        if (indicators == null || indicators.getTicker() == null || indicators.getTicker().isBlank()) {
            throw new IllegalArgumentException("Ticker é obrigatório");
        }
        if (indicators.getAno() < 1900 || indicators.getAno() > 2100) {
            throw new IllegalArgumentException("Ano inválido");
        }
        return repositoryPort.save(indicators);
    }

    @Override
    public List<AnnualIndicators> getIndicatorsByTicker(String ticker) {
        if (ticker == null || ticker.isBlank()) {
            return List.of();
        }
        return repositoryPort.findByTicker(ticker.trim().toUpperCase());
    }

    @Override
    public Optional<AnnualIndicators> getIndicatorsByTickerAndYear(String ticker, int year) {
        if (ticker == null || ticker.isBlank()) {
            return Optional.empty();
        }
        return repositoryPort.findByTickerAndAno(ticker.trim().toUpperCase(), year);
    }

    @Override
    public void deleteIndicatorsByTickerAndYear(String ticker, int year) {
        if (ticker != null && !ticker.isBlank()) {
            repositoryPort.deleteByTickerAndAno(ticker.trim().toUpperCase(), year);
        }
    }
}
