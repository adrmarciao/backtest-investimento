package com.backtest.backend.infrastructure.adapter.out.gateway;

import com.backtest.backend.domain.entity.DividendPayment;
import com.backtest.backend.domain.entity.HistoricalPrice;
import com.backtest.backend.domain.entity.Periodicity;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Component
public class YahooFinanceClient {

    private final WebClient webClient;

    public YahooFinanceClient(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder
                .baseUrl("https://query1.finance.yahoo.com")
                .defaultHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64)")
                .build();
    }

    public List<HistoricalPrice> fetchPrices(String ticker, LocalDate start, LocalDate end, Periodicity periodicity) {
        String formattedSymbol = formatSymbol(ticker);
        long period1 = start.atStartOfDay(ZoneOffset.UTC).toEpochSecond();
        long period2 = end.plusDays(1).atStartOfDay(ZoneOffset.UTC).toEpochSecond();
        String interval = (periodicity == Periodicity.SEMANAL) ? "1wk" : "1mo";

        try {
            JsonNode response = webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/v8/finance/chart/{symbol}")
                            .queryParam("period1", period1)
                            .queryParam("period2", period2)
                            .queryParam("interval", interval)
                            .build(formattedSymbol))
                    .retrieve()
                    .bodyToMono(JsonNode.class)
                    .block();

            return parseChartResponse(response);
        } catch (Exception e) {
            System.err.println("Erro ao buscar cotações do Yahoo Finance para " + formattedSymbol + ": " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public List<DividendPayment> fetchDividends(String ticker, LocalDate start, LocalDate end) {
        String formattedSymbol = formatSymbol(ticker);
        long period1 = start.atStartOfDay(ZoneOffset.UTC).toEpochSecond();
        long period2 = end.plusDays(1).atStartOfDay(ZoneOffset.UTC).toEpochSecond();

        try {
            JsonNode response = webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/v8/finance/chart/{symbol}")
                            .queryParam("period1", period1)
                            .queryParam("period2", period2)
                            .queryParam("interval", "1d")
                            .queryParam("events", "div")
                            .build(formattedSymbol))
                    .retrieve()
                    .bodyToMono(JsonNode.class)
                    .block();

            return parseDividendsResponse(response);
        } catch (Exception e) {
            System.err.println("Erro ao buscar dividendos do Yahoo Finance para " + formattedSymbol + ": " + e.getMessage());
            return new ArrayList<>();
        }
    }

    private String formatSymbol(String ticker) {
        if (ticker == null) return "";
        String trimmed = ticker.trim().toUpperCase();
        if (trimmed.equals("^BVSP") || trimmed.endsWith(".SA")) {
            return trimmed;
        }
        return trimmed + ".SA";
    }

    private List<HistoricalPrice> parseChartResponse(JsonNode root) {
        List<HistoricalPrice> prices = new ArrayList<>();
        if (root == null || !root.has("chart")) {
            return prices;
        }

        JsonNode resultNode = root.path("chart").path("result");
        if (!resultNode.isArray() || resultNode.isEmpty()) {
            return prices;
        }

        JsonNode item = resultNode.get(0);
        JsonNode timestamps = item.path("timestamp");
        JsonNode quote = item.path("indicators").path("quote");

        if (!timestamps.isArray() || quote.isEmpty()) {
            return prices;
        }

        JsonNode closes = quote.get(0).path("close");
        if (!closes.isArray()) {
            return prices;
        }

        int count = Math.min(timestamps.size(), closes.size());
        for (int i = 0; i < count; i++) {
            JsonNode tsNode = timestamps.get(i);
            JsonNode closeNode = closes.get(i);

            if (tsNode != null && !tsNode.isNull() && closeNode != null && !closeNode.isNull()) {
                long epochSecond = tsNode.asLong();
                double closeVal = closeNode.asDouble();
                if (closeVal > 0) {
                    LocalDate date = Instant.ofEpochSecond(epochSecond).atZone(ZoneId.of("America/Sao_Paulo")).toLocalDate();
                    BigDecimal price = BigDecimal.valueOf(closeVal).setScale(2, RoundingMode.HALF_UP);
                    prices.add(new HistoricalPrice(date, price));
                }
            }
        }

        return prices;
    }

    private List<DividendPayment> parseDividendsResponse(JsonNode root) {
        List<DividendPayment> dividends = new ArrayList<>();
        if (root == null || !root.has("chart")) {
            return dividends;
        }

        JsonNode resultNode = root.path("chart").path("result");
        if (!resultNode.isArray() || resultNode.isEmpty()) {
            return dividends;
        }

        JsonNode item = resultNode.get(0);
        JsonNode dividendsNode = item.path("events").path("dividends");
        if (!dividendsNode.isObject()) {
            return dividends;
        }

        dividendsNode.fields().forEachRemaining(entry -> {
            JsonNode divObj = entry.getValue();
            if (divObj.has("amount") && divObj.has("date")) {
                long epochSec = divObj.get("date").asLong();
                double amount = divObj.get("amount").asDouble();
                if (amount > 0) {
                    LocalDate date = Instant.ofEpochSecond(epochSec).atZone(ZoneId.of("America/Sao_Paulo")).toLocalDate();
                    BigDecimal val = BigDecimal.valueOf(amount).setScale(4, RoundingMode.HALF_UP);
                    dividends.add(new DividendPayment(date, val));
                }
            }
        });

        dividends.sort(Comparator.comparing(DividendPayment::getData));
        return dividends;
    }
}

