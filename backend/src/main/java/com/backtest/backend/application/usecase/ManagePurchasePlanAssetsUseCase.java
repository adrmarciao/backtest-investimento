package com.backtest.backend.application.usecase;

import com.backtest.backend.domain.entity.BenchmarkStatus;
import com.backtest.backend.domain.entity.MarketQuoteDetails;
import com.backtest.backend.domain.entity.PurchasePlanAsset;
import com.backtest.backend.domain.entity.PurchasePlanConfig;
import com.backtest.backend.domain.entity.RoundAllocationResult;
import com.backtest.backend.domain.port.in.ManagePurchasePlanAssetsPort;
import com.backtest.backend.domain.port.out.FundamentalDataGatewayPort;
import com.backtest.backend.domain.port.out.MarketQuoteGatewayPort;
import com.backtest.backend.domain.port.out.PurchasePlanAssetRepositoryPort;
import com.backtest.backend.domain.port.out.PurchasePlanConfigRepositoryPort;
import com.backtest.backend.domain.service.PurchasePlanCalculatorService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class ManagePurchasePlanAssetsUseCase implements ManagePurchasePlanAssetsPort {

    private final PurchasePlanAssetRepositoryPort assetRepositoryPort;
    private final PurchasePlanConfigRepositoryPort configRepositoryPort;
    private final MarketQuoteGatewayPort marketQuoteGatewayPort;
    private final FundamentalDataGatewayPort fundamentalDataGatewayPort;
    private final PurchasePlanCalculatorService calculatorService;

    public ManagePurchasePlanAssetsUseCase(
            PurchasePlanAssetRepositoryPort assetRepositoryPort,
            PurchasePlanConfigRepositoryPort configRepositoryPort,
            MarketQuoteGatewayPort marketQuoteGatewayPort,
            FundamentalDataGatewayPort fundamentalDataGatewayPort,
            PurchasePlanCalculatorService calculatorService
    ) {
        this.assetRepositoryPort = assetRepositoryPort;
        this.configRepositoryPort = configRepositoryPort;
        this.marketQuoteGatewayPort = marketQuoteGatewayPort;
        this.fundamentalDataGatewayPort = fundamentalDataGatewayPort;
        this.calculatorService = calculatorService;
    }

    @Override
    public List<PurchasePlanAsset> getAllAssets() {
        return assetRepositoryPort.findAll();
    }

    @Override
    public List<PurchasePlanAsset> saveAllAssets(List<PurchasePlanAsset> assets) {
        if (assets == null) return List.of();
        for (int i = 0; i < assets.size(); i++) {
            PurchasePlanAsset asset = assets.get(i);
            if (asset.getOrdem() == 0) {
                asset.setOrdem(i);
            }
            asset.recalculateValuations();
        }
        return assetRepositoryPort.saveAll(assets);
    }

    @Override
    public PurchasePlanAsset saveAsset(PurchasePlanAsset asset) {
        if (asset == null || asset.getTicker() == null || asset.getTicker().isBlank()) {
            throw new IllegalArgumentException("Ticker é obrigatório para cadastrar o ativo.");
        }
        asset.recalculateValuations();
        return assetRepositoryPort.save(asset);
    }

    @Override
    public void deleteAsset(String ticker) {
        if (ticker != null && !ticker.isBlank()) {
            assetRepositoryPort.deleteByTicker(ticker.trim().toUpperCase());
        }
    }

    @Override
    public List<PurchasePlanAsset> syncQuotes() {
        List<PurchasePlanAsset> currentAssets = assetRepositoryPort.findAll();
        if (currentAssets.isEmpty()) {
            return currentAssets;
        }

        List<String> tickers = currentAssets.stream().map(PurchasePlanAsset::getTicker).toList();
        Map<String, BigDecimal> quotes = marketQuoteGatewayPort.fetchCurrentQuotes(tickers);

        for (PurchasePlanAsset asset : currentAssets) {
            BigDecimal quote = quotes.get(asset.getTicker());
            if (quote != null) {
                asset.setPrecoAtual(quote);
            }
            asset.recalculateValuations();
        }

        return assetRepositoryPort.saveAll(currentAssets);
    }

    @Override
    public List<PurchasePlanAsset> syncFundamentals(String token) {
        PurchasePlanConfig config = configRepositoryPort.findDefault().orElseGet(PurchasePlanConfig::new);
        String activeToken = (token != null && !token.isBlank()) ? token.trim() : config.getTokenBrapi();

        if (activeToken == null || activeToken.isBlank()) {
            throw new IllegalArgumentException("Token da Brapi não informado nem configurado.");
        }

        List<PurchasePlanAsset> currentAssets = assetRepositoryPort.findAll();
        if (currentAssets.isEmpty()) {
            return currentAssets;
        }

        List<String> tickers = currentAssets.stream().map(PurchasePlanAsset::getTicker).toList();
        Map<String, FundamentalDataGatewayPort.FundamentalData> fundamentals =
                fundamentalDataGatewayPort.fetchFundamentals(tickers, activeToken);

        for (PurchasePlanAsset asset : currentAssets) {
            FundamentalDataGatewayPort.FundamentalData data = fundamentals.get(asset.getTicker());
            if (data != null) {
                if (data.lpa() != null) asset.setLpa(data.lpa());
                if (data.vpa() != null) asset.setVpa(data.vpa());
            }
            asset.recalculateValuations();
        }

        return assetRepositoryPort.saveAll(currentAssets);
    }

    @Override
    public RoundAllocationResult calculateAllocation() {
        PurchasePlanConfig config = configRepositoryPort.findDefault().orElseGet(PurchasePlanConfig::new);
        List<PurchasePlanAsset> assets = assetRepositoryPort.findAll();
        return calculatorService.calculateAllocation(config, assets);
    }

    @Override
    public BenchmarkStatus getBenchmarkStatus(String benchmark) {
        PurchasePlanConfig config = configRepositoryPort.findDefault().orElseGet(PurchasePlanConfig::new);
        String targetBenchmark = (benchmark != null && !benchmark.isBlank())
                ? benchmark.trim().toUpperCase()
                : config.getBenchmark();

        MarketQuoteDetails quoteDetails = marketQuoteGatewayPort.fetchQuoteDetails(targetBenchmark);
        BigDecimal currentPrice = quoteDetails != null ? quoteDetails.price() : null;
        BigDecimal high52 = quoteDetails != null ? quoteDetails.high52Week() : null;
        BigDecimal low52 = quoteDetails != null ? quoteDetails.low52Week() : null;

        BigDecimal effectiveAlta = (config != null && config.getAltaAno() != null) ? config.getAltaAno() : high52;
        BigDecimal effectiveFiboUp = (config != null && config.getFiboUp() != null) ? config.getFiboUp() : high52;
        BigDecimal effectiveFiboDown = (config != null && config.getFiboDown() != null) ? config.getFiboDown() : low52;

        return calculatorService.calculateBenchmarkStatus(
                targetBenchmark,
                currentPrice,
                effectiveAlta,
                effectiveFiboUp,
                effectiveFiboDown
        );
    }
}

