package com.backtest.backend.application.usecase;

import com.backtest.backend.domain.entity.BenchmarkStatus;
import com.backtest.backend.domain.entity.MarketQuoteDetails;
import com.backtest.backend.domain.entity.PurchasePlanConfig;
import com.backtest.backend.domain.port.out.FundamentalDataGatewayPort;
import com.backtest.backend.domain.port.out.MarketQuoteGatewayPort;
import com.backtest.backend.domain.port.out.PurchasePlanAssetRepositoryPort;
import com.backtest.backend.domain.port.out.PurchasePlanConfigRepositoryPort;
import com.backtest.backend.domain.service.PurchasePlanCalculatorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class ManagePurchasePlanAssetsUseCaseTest {

    private PurchasePlanAssetRepositoryPort assetRepositoryPort;
    private PurchasePlanConfigRepositoryPort configRepositoryPort;
    private MarketQuoteGatewayPort marketQuoteGatewayPort;
    private FundamentalDataGatewayPort fundamentalDataGatewayPort;
    private PurchasePlanCalculatorService calculatorService;
    private ManagePurchasePlanAssetsUseCase useCase;

    @BeforeEach
    void setUp() {
        assetRepositoryPort = Mockito.mock(PurchasePlanAssetRepositoryPort.class);
        configRepositoryPort = Mockito.mock(PurchasePlanConfigRepositoryPort.class);
        marketQuoteGatewayPort = Mockito.mock(MarketQuoteGatewayPort.class);
        fundamentalDataGatewayPort = Mockito.mock(FundamentalDataGatewayPort.class);
        calculatorService = new PurchasePlanCalculatorService();

        useCase = new ManagePurchasePlanAssetsUseCase(
                assetRepositoryPort,
                configRepositoryPort,
                marketQuoteGatewayPort,
                fundamentalDataGatewayPort,
                calculatorService
        );
    }

    @Test
    @DisplayName("Usa dados automáticos de 52 semanas quando configurações manuais estão nulas")
    void shouldFallbackToAutomaticQuoteDetailsWhenConfigNull() {
        PurchasePlanConfig config = new PurchasePlanConfig();
        config.setBenchmark("IBOV");
        config.setAltaAno(null);
        config.setFiboUp(null);
        config.setFiboDown(null);

        when(configRepositoryPort.findDefault()).thenReturn(Optional.of(config));
        when(marketQuoteGatewayPort.fetchQuoteDetails("IBOV")).thenReturn(
                new MarketQuoteDetails(
                        new BigDecimal("125000.00"),
                        new BigDecimal("135000.00"),
                        new BigDecimal("115000.00")
                )
        );

        BenchmarkStatus status = useCase.getBenchmarkStatus("IBOV");

        assertNotNull(status);
        assertEquals(new BigDecimal("125000.00"), status.precoAtual());
        assertEquals(new BigDecimal("135000.00"), status.altaAno());
        assertEquals(new BigDecimal("135000.00"), status.fiboUp());
        assertEquals(new BigDecimal("115000.00"), status.fiboDown());
        assertEquals(new BigDecimal("-7.41"), status.drawdownPercent());
        assertEquals(new BigDecimal("50.00"), status.fiboRetractionPercent());
    }

    @Test
    @DisplayName("Prioriza valores manuais quando informados na configuração")
    void shouldPrioritizeManualConfigOverAutomaticDetails() {
        PurchasePlanConfig config = new PurchasePlanConfig();
        config.setBenchmark("IBOV");
        config.setAltaAno(new BigDecimal("140000.00"));
        config.setFiboUp(new BigDecimal("140000.00"));
        config.setFiboDown(new BigDecimal("110000.00"));

        when(configRepositoryPort.findDefault()).thenReturn(Optional.of(config));
        when(marketQuoteGatewayPort.fetchQuoteDetails("IBOV")).thenReturn(
                new MarketQuoteDetails(
                        new BigDecimal("125000.00"),
                        new BigDecimal("135000.00"),
                        new BigDecimal("115000.00")
                )
        );

        BenchmarkStatus status = useCase.getBenchmarkStatus("IBOV");

        assertNotNull(status);
        assertEquals(new BigDecimal("125000.00"), status.precoAtual());
        assertEquals(new BigDecimal("140000.00"), status.altaAno());
        assertEquals(new BigDecimal("140000.00"), status.fiboUp());
        assertEquals(new BigDecimal("110000.00"), status.fiboDown());
    }
}
