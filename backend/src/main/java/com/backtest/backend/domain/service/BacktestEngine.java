package com.backtest.backend.domain.service;

import com.backtest.backend.domain.entity.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class BacktestEngine {

    /**
     * Avalia se um ano é elegível de acordo com os critérios fixos (Fase 1).
     */
    public boolean isYearEligible(AnnualIndicators indicators, FixedCriteria criteria) {
        if (criteria == null) {
            return true;
        }
        if (indicators == null) {
            return false;
        }

        if (criteria.getPlMax() != null && indicators.getPl() != null) {
            if (indicators.getPl().compareTo(criteria.getPlMax()) > 0) {
                return false;
            }
        }

        if (criteria.getPvpMax() != null && indicators.getPvp() != null) {
            if (indicators.getPvp().compareTo(criteria.getPvpMax()) > 0) {
                return false;
            }
        }

        if (criteria.getDividaEbitdaMax() != null && indicators.getDividaEbitda() != null) {
            if (indicators.getDividaEbitda().compareTo(criteria.getDividaEbitdaMax()) > 0) {
                return false;
            }
        }

        if (criteria.getRoeMin() != null && indicators.getRoe() != null) {
            if (indicators.getRoe().compareTo(criteria.getRoeMin()) < 0) {
                return false;
            }
        }

        return true;
    }

    /**
     * Executa o motor de compras para um ativo dado um conjunto de preços históricos e indicadores por ano.
     */
    public List<Purchase> processAssetPurchases(
            Asset asset,
            FixedCriteria criteria,
            List<AnnualIndicators> indicatorsList,
            List<HistoricalPrice> historicalPrices,
            Set<String> anosIgnoradosOutput
    ) {
        return processAssetPurchasesWithDividends(
                asset, criteria, indicatorsList, historicalPrices, Collections.emptyList(), anosIgnoradosOutput
        ).getPurchases();
    }

    /**
     * Executa o motor de compras para um ativo com reinvestimento condicional de dividendos acumulados.
     */
    public AssetBacktestResult processAssetPurchasesWithDividends(
            Asset asset,
            FixedCriteria criteria,
            List<AnnualIndicators> indicatorsList,
            List<HistoricalPrice> historicalPrices,
            List<DividendPayment> dividendPayments,
            Set<String> anosIgnoradosOutput
    ) {
        List<Purchase> purchases = new ArrayList<>();
        if (asset == null || criteria == null || indicatorsList == null || historicalPrices == null) {
            return new AssetBacktestResult(purchases, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO);
        }

        List<DividendPayment> sortedDividends = new ArrayList<>(dividendPayments != null ? dividendPayments : Collections.emptyList());
        sortedDividends.sort(Comparator.comparing(DividendPayment::getData));

        Map<Integer, AnnualIndicators> indicatorsByYear = indicatorsList.stream()
                .collect(Collectors.toMap(AnnualIndicators::getAno, ind -> ind, (a, b) -> b));

        BigDecimal sharesHeld = BigDecimal.ZERO;
        BigDecimal saldoCaixaDividendos = BigDecimal.ZERO;
        BigDecimal totalDividendosRecebidos = BigDecimal.ZERO;
        BigDecimal totalDividendosReinvestidos = BigDecimal.ZERO;
        int dividendIndex = 0;

        for (HistoricalPrice pricePoint : historicalPrices) {
            LocalDate currentDate = pricePoint.getData();
            int year = currentDate.getYear();

            // 1. Processar dividendos distribuídos até a data atual
            while (dividendIndex < sortedDividends.size() && !sortedDividends.get(dividendIndex).getData().isAfter(currentDate)) {
                DividendPayment div = sortedDividends.get(dividendIndex);
                if (sharesHeld.compareTo(BigDecimal.ZERO) > 0 && div.getValorPorAcao() != null) {
                    BigDecimal divAmount = sharesHeld.multiply(div.getValorPorAcao());
                    saldoCaixaDividendos = saldoCaixaDividendos.add(divAmount);
                    totalDividendosRecebidos = totalDividendosRecebidos.add(divAmount);
                }
                dividendIndex++;
            }

            AnnualIndicators annualInd = indicatorsByYear.get(year);
            if (annualInd == null) {
                if (anosIgnoradosOutput != null) {
                    anosIgnoradosOutput.add(asset.getTicker() + " - " + year + " (Sem dados fundamentalistas)");
                }
                continue;
            }

            // Fase 1: Avaliar critérios fixos do ano
            if (!isYearEligible(annualInd, criteria)) {
                if (anosIgnoradosOutput != null) {
                    anosIgnoradosOutput.add(asset.getTicker() + " - " + year + " (Critérios fixos não satisfeitos)");
                }
                continue;
            }

            // Fase 2: Calcular tetos e comparar com preço
            BigDecimal tetoBazin = PriceCeilingCalculator.calculateBazin(annualInd.getDpa());
            BigDecimal tetoGraham = PriceCeilingCalculator.calculateGraham(annualInd.getLpa(), annualInd.getVpa());

            BigDecimal currentPrice = pricePoint.getPrecoFechamento();
            if (currentPrice == null || currentPrice.compareTo(BigDecimal.ZERO) <= 0) {
                continue;
            }

            boolean bazinOk = (tetoBazin == null) || (currentPrice.compareTo(tetoBazin) <= 0);
            boolean grahamOk = (tetoGraham == null) || (currentPrice.compareTo(tetoGraham) <= 0);

            // A compra ocorre se preço respeitar os tetos presentes
            if (bazinOk && grahamOk) {
                // Compra regular via aporte
                BigDecimal aporte = asset.getValorAporte();
                if (aporte != null && aporte.compareTo(BigDecimal.ZERO) > 0) {
                    BigDecimal cotas = aporte.divide(currentPrice, 6, RoundingMode.HALF_UP);
                    sharesHeld = sharesHeld.add(cotas);
                    purchases.add(new Purchase(currentDate, asset.getTicker(), currentPrice, aporte, cotas, tetoBazin, tetoGraham, false));
                }

                // Reinvestimento de dividendos em caixa
                if (saldoCaixaDividendos.compareTo(BigDecimal.ZERO) > 0) {
                    BigDecimal cotasReinvestidas = saldoCaixaDividendos.divide(currentPrice, 6, RoundingMode.HALF_UP);
                    if (cotasReinvestidas.compareTo(BigDecimal.ZERO) > 0) {
                        BigDecimal valorReinvestido = saldoCaixaDividendos;
                        sharesHeld = sharesHeld.add(cotasReinvestidas);
                        purchases.add(new Purchase(currentDate, asset.getTicker(), currentPrice, valorReinvestido, cotasReinvestidas, tetoBazin, tetoGraham, true));
                        totalDividendosReinvestidos = totalDividendosReinvestidos.add(valorReinvestido);
                        saldoCaixaDividendos = BigDecimal.ZERO;
                    }
                }
            }
        }

        return new AssetBacktestResult(
                purchases,
                totalDividendosRecebidos.setScale(2, RoundingMode.HALF_UP),
                totalDividendosReinvestidos.setScale(2, RoundingMode.HALF_UP),
                saldoCaixaDividendos.setScale(2, RoundingMode.HALF_UP)
        );
    }
}

