package com.backtest.backend.infrastructure.adapter.out.gateway;

import com.backtest.backend.domain.entity.MarketQuoteDetails;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class YahooFinanceClientTest {

    private YahooFinanceClient yahooFinanceClient;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        yahooFinanceClient = new YahooFinanceClient(WebClient.builder());
        objectMapper = new ObjectMapper();
    }

    @Test
    void parseQuoteDetailsResponseFromMeta() throws Exception {
        String json = """
                {
                  "chart": {
                    "result": [
                      {
                        "meta": {
                          "currency": "BRL",
                          "symbol": "^BVSP",
                          "regularMarketPrice": 130123.45,
                          "fiftyTwoWeekHigh": 137469.15,
                          "fiftyTwoWeekLow": 118200.50
                        }
                      }
                    ]
                  }
                }
                """;

        MarketQuoteDetails details = yahooFinanceClient.parseQuoteDetailsResponse(objectMapper.readTree(json));

        assertNotNull(details);
        assertEquals(new BigDecimal("130123.45"), details.price());
        assertEquals(new BigDecimal("137469.15"), details.high52Week());
        assertEquals(new BigDecimal("118200.50"), details.low52Week());
    }
}
