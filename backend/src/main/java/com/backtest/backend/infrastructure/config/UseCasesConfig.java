package com.backtest.backend.infrastructure.config;

import com.backtest.backend.application.usecase.*;
import com.backtest.backend.domain.port.in.*;
import com.backtest.backend.domain.port.out.*;
import com.backtest.backend.domain.service.PurchasePlanCalculatorService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCasesConfig {

    @Bean
    public RegisterAssetPort registerAssetPort(AssetRepositoryPort assetRepositoryPort) {
        return new RegisterAssetUseCase(assetRepositoryPort);
    }

    @Bean
    public SaveFixedCriteriaPort saveFixedCriteriaPort(FixedCriteriaRepositoryPort repositoryPort) {
        return new SaveFixedCriteriaUseCase(repositoryPort);
    }

    @Bean
    public SaveAnnualIndicatorsPort saveAnnualIndicatorsPort(AnnualIndicatorsRepositoryPort repositoryPort) {
        return new SaveAnnualIndicatorsUseCase(repositoryPort);
    }

    @Bean
    public ExecuteBacktestPort executeBacktestPort(
            AssetRepositoryPort assetRepositoryPort,
            FixedCriteriaRepositoryPort fixedCriteriaRepositoryPort,
            AnnualIndicatorsRepositoryPort annualIndicatorsRepositoryPort,
            BacktestResultRepositoryPort backtestResultRepositoryPort,
            PriceFetcherGatewayPort priceFetcherGatewayPort
    ) {
        return new ExecuteBacktestUseCase(
                assetRepositoryPort,
                fixedCriteriaRepositoryPort,
                annualIndicatorsRepositoryPort,
                backtestResultRepositoryPort,
                priceFetcherGatewayPort
        );
    }

    @Bean
    public GetBacktestResultPort getBacktestResultPort(BacktestResultRepositoryPort repositoryPort) {
        return new GetBacktestResultUseCase(repositoryPort);
    }

    @Bean
    public PurchasePlanCalculatorService purchasePlanCalculatorService() {
        return new PurchasePlanCalculatorService();
    }

    @Bean
    public ManagePurchasePlanConfigPort managePurchasePlanConfigPort(PurchasePlanConfigRepositoryPort repositoryPort) {
        return new ManagePurchasePlanConfigUseCase(repositoryPort);
    }

    @Bean
    public ManagePurchasePlanAssetsPort managePurchasePlanAssetsPort(
            PurchasePlanAssetRepositoryPort assetRepositoryPort,
            PurchasePlanConfigRepositoryPort configRepositoryPort,
            MarketQuoteGatewayPort marketQuoteGatewayPort,
            FundamentalDataGatewayPort fundamentalDataGatewayPort,
            PurchasePlanCalculatorService calculatorService
    ) {
        return new ManagePurchasePlanAssetsUseCase(
                assetRepositoryPort,
                configRepositoryPort,
                marketQuoteGatewayPort,
                fundamentalDataGatewayPort,
                calculatorService
        );
    }
}
