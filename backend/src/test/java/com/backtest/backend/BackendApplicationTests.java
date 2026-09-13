package com.backtest.backend;

import com.backtest.backend.domain.port.out.*;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.data.mongo.MongoRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
@EnableAutoConfiguration(exclude = {MongoAutoConfiguration.class, MongoDataAutoConfiguration.class, MongoRepositoriesAutoConfiguration.class})
class BackendApplicationTests {

    @MockBean
    private AssetRepositoryPort assetRepositoryPort;
    @MockBean
    private FixedCriteriaRepositoryPort fixedCriteriaRepositoryPort;
    @MockBean
    private AnnualIndicatorsRepositoryPort annualIndicatorsRepositoryPort;
    @MockBean
    private BacktestResultRepositoryPort backtestResultRepositoryPort;
    @MockBean
    private PriceFetcherGatewayPort priceFetcherGatewayPort;

	@Test
	void contextLoads() {
	}

}
