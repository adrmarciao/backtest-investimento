package com.backtest.backend.infrastructure.adapter.out.gateway;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class GoogleFinanceClientTest {

    private GoogleFinanceClient googleFinanceClient;
    private YahooFinanceClient yahooFinanceFallback;

    @BeforeEach
    void setUp() {
        yahooFinanceFallback = Mockito.mock(YahooFinanceClient.class);
        WebClient.Builder builder = WebClient.builder();
        googleFinanceClient = new GoogleFinanceClient(builder, yahooFinanceFallback);
    }

    @Test
    void parsePriceAnd52WeekHighLowFromPortugueseHtml() {
        String html = """
                <div class="Pdsbrc" data-last-price="130123.45">130.123,45</div>
                <div class="gyr2W">
                    <div class="SwQK7">Alto — 52 sem</div>
                    <div class="dO6ijd">137.469,15</div>
                </div>
                <div class="gyr2W">
                    <div class="SwQK7">Baixo — 52 sem</div>
                    <div class="dO6ijd">118.200,50</div>
                </div>
                """;

        BigDecimal price = googleFinanceClient.parsePriceFromHtml(html);
        BigDecimal high = googleFinanceClient.parseHigh52FromHtml(html);
        BigDecimal low = googleFinanceClient.parseLow52FromHtml(html);

        assertEquals(new BigDecimal("130123.45"), price);
        assertEquals(new BigDecimal("137469.15"), high);
        assertEquals(new BigDecimal("118200.50"), low);
    }

    @Test
    void parsePriceAnd52WeekHighLowFromEnglishHtml() {
        String html = """
                <div class="Pdsbrc" data-last-price="130123.45">130,123.45</div>
                <div class="gyr2W">
                    <div class="SwQK7">52-week high</div>
                    <div class="dO6ijd">137,469.15</div>
                </div>
                <div class="gyr2W">
                    <div class="SwQK7">52-week low</div>
                    <div class="dO6ijd">118,200.50</div>
                </div>
                """;

        BigDecimal high = googleFinanceClient.parseHigh52FromHtml(html);
        BigDecimal low = googleFinanceClient.parseLow52FromHtml(html);

        assertEquals(new BigDecimal("137469.15"), high);
        assertEquals(new BigDecimal("118200.50"), low);
    }
}
