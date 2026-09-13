package com.backtest.backend.application.usecase;

import com.backtest.backend.domain.entity.*;
import com.backtest.backend.domain.port.in.ExecuteBacktestPort;
import com.backtest.backend.domain.port.out.*;
import com.backtest.backend.domain.service.AssetBacktestResult;
import com.backtest.backend.domain.service.BacktestEngine;
import com.backtest.backend.domain.service.MetricsCalculator;


import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class ExecuteBacktestUseCase implements ExecuteBacktestPort {

    private static final String IBOV_TICKER = "^BVSP";

    private final AssetRepositoryPort assetRepositoryPort;
    private final FixedCriteriaRepositoryPort fixedCriteriaRepositoryPort;
    private final AnnualIndicatorsRepositoryPort annualIndicatorsRepositoryPort;
    private final BacktestResultRepositoryPort backtestResultRepositoryPort;
    private final PriceFetcherGatewayPort priceFetcherGatewayPort;
    private final BacktestEngine backtestEngine;
    private final MetricsCalculator metricsCalculator;

    public ExecuteBacktestUseCase(
            AssetRepositoryPort assetRepositoryPort,
            FixedCriteriaRepositoryPort fixedCriteriaRepositoryPort,
            AnnualIndicatorsRepositoryPort annualIndicatorsRepositoryPort,
            BacktestResultRepositoryPort backtestResultRepositoryPort,
            PriceFetcherGatewayPort priceFetcherGatewayPort
    ) {
        this.assetRepositoryPort = assetRepositoryPort;
        this.fixedCriteriaRepositoryPort = fixedCriteriaRepositoryPort;
        this.annualIndicatorsRepositoryPort = annualIndicatorsRepositoryPort;
        this.backtestResultRepositoryPort = backtestResultRepositoryPort;
        this.priceFetcherGatewayPort = priceFetcherGatewayPort;
        this.backtestEngine = new BacktestEngine();
        this.metricsCalculator = new MetricsCalculator();
    }

    @Override
    public BacktestResult executeBacktest(LocalDate start, LocalDate end) {
        if (start == null || end == null || start.isAfter(end)) {
            throw new IllegalArgumentException("Período do backtest inválido");
        }

        List<Asset> assets = assetRepositoryPort.findAll();
        if (assets.isEmpty()) {
            throw new IllegalStateException("Nenhum ativo cadastrado para realizar o backtest");
        }

        FixedCriteria criteria = fixedCriteriaRepositoryPort.find()
                .orElseThrow(() -> new IllegalStateException("Critérios fixos de compra não configurados"));

        List<Purchase> allPurchases = new ArrayList<>();
        Set<String> anosIgnorados = new LinkedHashSet<>();
        Map<String, List<HistoricalPrice>> pricesByTicker = new HashMap<>();
        Map<String, BigDecimal> currentPrices = new HashMap<>();

        BigDecimal grandTotalDivRecebidos = BigDecimal.ZERO;
        BigDecimal grandTotalDivReinvestidos = BigDecimal.ZERO;
        BigDecimal grandSaldoCaixaDiv = BigDecimal.ZERO;

        // Processar compras para cada ativo
        for (Asset asset : assets) {
            List<AnnualIndicators> indicators = annualIndicatorsRepositoryPort.findByTicker(asset.getTicker());
            List<HistoricalPrice> assetPrices = priceFetcherGatewayPort.fetchHistoricalPrices(
                    asset.getTicker(), start, end, asset.getPeriodicidade()
            );
            List<DividendPayment> dividendPayments = priceFetcherGatewayPort.fetchHistoricalDividends(
                    asset.getTicker(), start, end
            );
            pricesByTicker.put(asset.getTicker(), assetPrices);

            if (!assetPrices.isEmpty()) {
                currentPrices.put(asset.getTicker(), assetPrices.get(assetPrices.size() - 1).getPrecoFechamento());
            }

            AssetBacktestResult assetResult = backtestEngine.processAssetPurchasesWithDividends(
                    asset, criteria, indicators, assetPrices, dividendPayments, anosIgnorados
            );
            allPurchases.addAll(assetResult.getPurchases());

            grandTotalDivRecebidos = grandTotalDivRecebidos.add(assetResult.getTotalDividendosRecebidos());
            grandTotalDivReinvestidos = grandTotalDivReinvestidos.add(assetResult.getTotalDividendosReinvestidos());
            grandSaldoCaixaDiv = grandSaldoCaixaDiv.add(assetResult.getSaldoCaixaDividendos());
        }

        // Ordenar compras por data
        allPurchases.sort(Comparator.comparing(Purchase::getData));

        // Buscar dados do IBOVESPA
        List<HistoricalPrice> ibovPrices = priceFetcherGatewayPort.fetchHistoricalPrices(
                IBOV_TICKER, start, end, Periodicity.SEMANAL
        );
        Map<LocalDate, BigDecimal> ibovPriceMap = ibovPrices.stream()
                .collect(Collectors.toMap(HistoricalPrice::getData, HistoricalPrice::getPrecoFechamento, (a, b) -> b));

        // Construir Série Temporal do Portfólio
        List<TimeSeriesPoint> timeSeries = buildTimeSeries(allPurchases, pricesByTicker, ibovPrices, start, end);

        // Calcular Métricas
        BigDecimal totalAportado = metricsCalculator.calculateTotalInvested(allPurchases);
        List<AssetSummary> assetSummaries = metricsCalculator.calculateAssetSummaries(allPurchases, currentPrices);

        BigDecimal valorFinal = assetSummaries.stream()
                .map(AssetSummary::getValorAtual)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal retornoTotal = metricsCalculator.calculateTotalReturn(totalAportado, valorFinal);
        BigDecimal cagr = metricsCalculator.calculateCAGR(totalAportado, valorFinal, start, end);
        BigDecimal maxDrawdown = metricsCalculator.calculateMaxDrawdown(timeSeries);
        BigDecimal sharpeRatio = metricsCalculator.calculateSharpeRatio(timeSeries, cagr);

        // Retorno do IBOVESPA
        BigDecimal ibovReturn = BigDecimal.ZERO;
        if (!ibovPrices.isEmpty()) {
            BigDecimal firstIbov = ibovPrices.get(0).getPrecoFechamento();
            BigDecimal lastIbov = ibovPrices.get(ibovPrices.size() - 1).getPrecoFechamento();
            if (firstIbov != null && firstIbov.compareTo(BigDecimal.ZERO) > 0 && lastIbov != null) {
                ibovReturn = lastIbov.subtract(firstIbov)
                        .divide(firstIbov, 6, RoundingMode.HALF_UP)
                        .multiply(new BigDecimal("100"))
                        .setScale(2, RoundingMode.HALF_UP);
            }
        }
        BigDecimal alfaIbov = metricsCalculator.calculateAlphaIbov(retornoTotal, ibovReturn);

        // Montar Resultado
        BacktestResult result = new BacktestResult();
        result.setId(UUID.randomUUID().toString());
        result.setDataExecucao(LocalDateTime.now());
        result.setCompras(allPurchases);
        result.setSerieTemporal(timeSeries);
        result.setResumoAtivos(assetSummaries);
        result.setAnosIgnorados(new ArrayList<>(anosIgnorados));
        result.setTotalAportado(totalAportado);
        result.setValorFinal(valorFinal);
        result.setRetornoTotal(retornoTotal);
        result.setCagr(cagr);
        result.setMaxDrawdown(maxDrawdown);
        result.setSharpeRatio(sharpeRatio);
        result.setAlfaIbov(alfaIbov);
        result.setTotalDividendosRecebidos(grandTotalDivRecebidos);
        result.setTotalDividendosReinvestidos(grandTotalDivReinvestidos);
        result.setSaldoCaixaDividendos(grandSaldoCaixaDiv);

        return backtestResultRepositoryPort.save(result);
    }

    private List<TimeSeriesPoint> buildTimeSeries(
            List<Purchase> purchases,
            Map<String, List<HistoricalPrice>> pricesByTicker,
            List<HistoricalPrice> ibovPrices,
            LocalDate start,
            LocalDate end
    ) {
        List<TimeSeriesPoint> timeSeries = new ArrayList<>();
        if (ibovPrices.isEmpty()) {
            return timeSeries;
        }

        BigDecimal initialIbov = ibovPrices.get(0).getPrecoFechamento();
        if (initialIbov == null || initialIbov.compareTo(BigDecimal.ZERO) <= 0) {
            initialIbov = BigDecimal.ONE;
        }

        BigDecimal accumulatedSharesMap = BigDecimal.ZERO;
        Map<String, BigDecimal> sharesHeld = new HashMap<>();
        BigDecimal accumulatedInvested = BigDecimal.ZERO;

        int purchaseIndex = 0;

        for (HistoricalPrice ibovPoint : ibovPrices) {
            LocalDate currentDate = ibovPoint.getData();

            // Incorporar compras até a data atual
            while (purchaseIndex < purchases.size() && !purchases.get(purchaseIndex).getData().isAfter(currentDate)) {
                Purchase p = purchases.get(purchaseIndex);
                sharesHeld.put(p.getTicker(), sharesHeld.getOrDefault(p.getTicker(), BigDecimal.ZERO).add(p.getCotas()));
                if (!Boolean.TRUE.equals(p.getIsReinvestimento())) {
                    accumulatedInvested = accumulatedInvested.add(p.getValorAportado());
                }
                purchaseIndex++;
            }

            // Calcular valor atual do patrimônio
            BigDecimal currentPortfolioValue = BigDecimal.ZERO;
            for (Map.Entry<String, BigDecimal> entry : sharesHeld.entrySet()) {
                String ticker = entry.getKey();
                BigDecimal shares = entry.getValue();
                List<HistoricalPrice> assetPrices = pricesByTicker.getOrDefault(ticker, List.of());
                BigDecimal priceAtDate = findClosestPrice(assetPrices, currentDate);
                if (priceAtDate != null) {
                    currentPortfolioValue = currentPortfolioValue.add(shares.multiply(priceAtDate));
                }
            }

            BigDecimal currentIbov = ibovPoint.getPrecoFechamento() != null ? ibovPoint.getPrecoFechamento() : BigDecimal.ZERO;

            // Normalização base 100
            BigDecimal portfolioNorm = BigDecimal.valueOf(100);
            if (accumulatedInvested.compareTo(BigDecimal.ZERO) > 0) {
                portfolioNorm = currentPortfolioValue.divide(accumulatedInvested, 6, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100));
            }

            BigDecimal ibovNorm = currentIbov.divide(initialIbov, 6, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100));

            timeSeries.add(new TimeSeriesPoint(
                    currentDate,
                    accumulatedInvested.setScale(2, RoundingMode.HALF_UP),
                    currentPortfolioValue.setScale(2, RoundingMode.HALF_UP),
                    currentIbov.setScale(2, RoundingMode.HALF_UP),
                    portfolioNorm.setScale(2, RoundingMode.HALF_UP),
                    ibovNorm.setScale(2, RoundingMode.HALF_UP)
            ));
        }

        return timeSeries;
    }

    private BigDecimal findClosestPrice(List<HistoricalPrice> prices, LocalDate targetDate) {
        if (prices.isEmpty()) return null;
        BigDecimal lastPrice = prices.get(0).getPrecoFechamento();
        for (HistoricalPrice hp : prices) {
            if (hp.getData().isAfter(targetDate)) {
                break;
            }
            if (hp.getPrecoFechamento() != null) {
                lastPrice = hp.getPrecoFechamento();
            }
        }
        return lastPrice;
    }
}
