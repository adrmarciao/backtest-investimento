package com.backtest.backend.domain.service;

import com.backtest.backend.domain.entity.*;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class MetricsCalculator {

    private static final BigDecimal SELIC_TAXA_ANUAL_PADRAO = new BigDecimal("0.10"); // 10% a.a.

    /**
     * Calcula o valor investido acumulado total.
     */
    public BigDecimal calculateTotalInvested(List<Purchase> purchases) {
        if (purchases == null || purchases.isEmpty()) {
            return BigDecimal.ZERO;
        }
        return purchases.stream()
                .map(Purchase::getValorAportado)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * Retorno Total = ((Valor Final - Total Aportado) / Total Aportado) * 100
     */
    public BigDecimal calculateTotalReturn(BigDecimal totalAportado, BigDecimal valorFinal) {
        if (totalAportado == null || totalAportado.compareTo(BigDecimal.ZERO) <= 0 || valorFinal == null) {
            return BigDecimal.ZERO;
        }
        BigDecimal diff = valorFinal.subtract(totalAportado);
        return diff.divide(totalAportado, 6, RoundingMode.HALF_UP)
                .multiply(new BigDecimal("100"))
                .setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * CAGR = (Valor Final / Total Aportado) ^ (1 / anos) - 1
     */
    public BigDecimal calculateCAGR(BigDecimal totalAportado, BigDecimal valorFinal, LocalDate startDate, LocalDate endDate) {
        if (totalAportado == null || totalAportado.compareTo(BigDecimal.ZERO) <= 0 || valorFinal == null || startDate == null || endDate == null) {
            return BigDecimal.ZERO;
        }

        long days = ChronoUnit.DAYS.between(startDate, endDate);
        if (days <= 0) {
            return BigDecimal.ZERO;
        }
        double years = days / 365.25;
        if (years < 0.08) { // Menos de 1 mês
            return BigDecimal.ZERO;
        }

        double ratio = valorFinal.divide(totalAportado, 6, RoundingMode.HALF_UP).doubleValue();
        if (ratio <= 0) {
            return BigDecimal.ZERO;
        }

        double cagrDouble = (Math.pow(ratio, 1.0 / years) - 1.0) * 100.0;
        return BigDecimal.valueOf(cagrDouble).setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * Max Drawdown = max((Pico - Vele) / Pico) * 100
     */
    public BigDecimal calculateMaxDrawdown(List<TimeSeriesPoint> timeSeries) {
        if (timeSeries == null || timeSeries.isEmpty()) {
            return BigDecimal.ZERO;
        }

        double maxDrawdown = 0.0;
        double peak = 0.0;

        for (TimeSeriesPoint point : timeSeries) {
            if (point.getValorPatrimonio() == null) continue;
            double val = point.getValorPatrimonio().doubleValue();
            if (val > peak) {
                peak = val;
            } else if (peak > 0) {
                double drawdown = (peak - val) / peak;
                if (drawdown > maxDrawdown) {
                    maxDrawdown = drawdown;
                }
            }
        }

        return BigDecimal.valueOf(maxDrawdown * 100.0).setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * Sharpe Ratio simplificado = (Retorno Anualizado Portfólio - Risk Free) / Desvio Padrão Anualizado
     */
    public BigDecimal calculateSharpeRatio(List<TimeSeriesPoint> timeSeries, BigDecimal cagrPercent) {
        if (timeSeries == null || timeSeries.size() < 2 || cagrPercent == null) {
            return BigDecimal.ZERO;
        }

        List<Double> returns = new ArrayList<>();
        for (int i = 1; i < timeSeries.size(); i++) {
            BigDecimal prev = timeSeries.get(i - 1).getValorPatrimonio();
            BigDecimal curr = timeSeries.get(i).getValorPatrimonio();
            if (prev != null && curr != null && prev.compareTo(BigDecimal.ZERO) > 0) {
                double r = (curr.subtract(prev)).divide(prev, 6, RoundingMode.HALF_UP).doubleValue();
                returns.add(r);
            }
        }

        if (returns.isEmpty()) {
            return BigDecimal.ZERO;
        }

        double mean = returns.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
        double variance = returns.stream().mapToDouble(r -> Math.pow(r - mean, 2)).average().orElse(0.0);
        double stdDev = Math.sqrt(variance);

        // Anualizar stdDev aproximando 52 semanas ou 12 meses
        double annualizedStdDev = stdDev * Math.sqrt(52);
        if (annualizedStdDev <= 0) {
            return BigDecimal.ZERO;
        }

        double rp = cagrPercent.doubleValue() / 100.0;
        double rf = SELIC_TAXA_ANUAL_PADRAO.doubleValue();

        double sharpe = (rp - rf) / annualizedStdDev;
        return BigDecimal.valueOf(sharpe).setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * Alfa vs IBOVESPA = Retorno Total Portfólio (%) - Retorno Total IBOVESPA (%)
     */
    public BigDecimal calculateAlphaIbov(BigDecimal portfolioReturn, BigDecimal ibovReturn) {
        if (portfolioReturn == null || ibovReturn == null) {
            return BigDecimal.ZERO;
        }
        return portfolioReturn.subtract(ibovReturn).setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * Gera resumos por ativo (total aportado, cotas, preço médio, etc.)
     */
    public List<AssetSummary> calculateAssetSummaries(List<Purchase> purchases, Map<String, BigDecimal> currentPrices) {
        if (purchases == null || purchases.isEmpty()) {
            return Collections.emptyList();
        }

        Map<String, BigDecimal> totalAportadoMap = new HashMap<>();
        Map<String, BigDecimal> totalCotasMap = new HashMap<>();

        for (Purchase p : purchases) {
            String ticker = p.getTicker();
            totalAportadoMap.put(ticker, totalAportadoMap.getOrDefault(ticker, BigDecimal.ZERO).add(p.getValorAportado()));
            totalCotasMap.put(ticker, totalCotasMap.getOrDefault(ticker, BigDecimal.ZERO).add(p.getCotas()));
        }

        List<AssetSummary> summaries = new ArrayList<>();
        for (String ticker : totalAportadoMap.keySet()) {
            BigDecimal totalAporte = totalAportadoMap.get(ticker);
            BigDecimal totalCotas = totalCotasMap.get(ticker);
            BigDecimal precoMedio = (totalCotas.compareTo(BigDecimal.ZERO) > 0)
                    ? totalAporte.divide(totalCotas, 4, RoundingMode.HALF_UP)
                    : BigDecimal.ZERO;

            BigDecimal precoAtual = currentPrices != null ? currentPrices.getOrDefault(ticker, BigDecimal.ZERO) : BigDecimal.ZERO;
            BigDecimal valorAtual = totalCotas.multiply(precoAtual).setScale(2, RoundingMode.HALF_UP);

            BigDecimal retornoPct = BigDecimal.ZERO;
            if (totalAporte.compareTo(BigDecimal.ZERO) > 0) {
                retornoPct = valorAtual.subtract(totalAporte)
                        .divide(totalAporte, 6, RoundingMode.HALF_UP)
                        .multiply(new BigDecimal("100"))
                        .setScale(2, RoundingMode.HALF_UP);
            }

            summaries.add(new AssetSummary(ticker, totalAporte, totalCotas, precoMedio, precoAtual, valorAtual, retornoPct));
        }

        return summaries;
    }
}
