package com.backtest.backend.infrastructure.adapter.out.gateway;

import com.backtest.backend.domain.port.out.FundamentalDataGatewayPort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class FundamentalDataGatewayAdapter implements FundamentalDataGatewayPort {

    private final BrapiClient brapiClient;

    public FundamentalDataGatewayAdapter(BrapiClient brapiClient) {
        this.brapiClient = brapiClient;
    }

    @Override
    public Map<String, FundamentalData> fetchFundamentals(List<String> tickers, String token) {
        return brapiClient.fetchFundamentals(tickers, token);
    }
}
