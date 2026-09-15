package com.backtest.backend.infrastructure.adapter.out.gateway;

import com.backtest.backend.domain.entity.HistoricalPrice;
import com.backtest.backend.domain.entity.Periodicity;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class GoogleFinanceClient {

    private final WebClient webClient;
    private final YahooFinanceClient yahooFinanceFallback;

    private static final Pattern PDSBRC_PATTERN = Pattern.compile("jsname=\"Pdsbrc\"[^>]*>([^<]+)<");
    private static final Pattern YMLKEC_PATTERN = Pattern.compile("class=\"[^\"]*YMlKec[^\"]*fxKbKc[^\"]*\"[^>]*>([^<]+)<");
    private static final Pattern DATA_PRICE_PATTERN = Pattern.compile("data-last-price=\"([0-9.]+)\"");

    public GoogleFinanceClient(WebClient.Builder webClientBuilder, YahooFinanceClient yahooFinanceFallback) {
        this.webClient = webClientBuilder
                .baseUrl("https://www.google.com/finance")
                .defaultHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/124.0.0.0 Safari/537.36")
                .defaultHeader("Accept-Language", "pt-BR,pt;q=0.9,en-US;q=0.8,en;q=0.7")
                .build();
        this.yahooFinanceFallback = yahooFinanceFallback;
    }

    public BigDecimal fetchQuote(String ticker) {
        if (ticker == null || ticker.isBlank()) return null;
        String cleanTicker = ticker.trim().toUpperCase();

        try {
            String path = buildPath(cleanTicker);
            String html = webClient.get()
                    .uri(path)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            if (html != null && !html.isBlank()) {
                BigDecimal parsed = parsePriceFromHtml(html);
                if (parsed != null && parsed.compareTo(BigDecimal.ZERO) > 0) {
                    return parsed;
                }
            }
        } catch (Exception e) {
            System.err.println("GoogleFinanceClient: Falha ao buscar cotação para " + cleanTicker + ": " + e.getMessage());
        }

        return fetchFallbackFromYahoo(cleanTicker);
    }

    private String buildPath(String cleanTicker) {
        if ("IBOV".equals(cleanTicker) || "^BVSP".equals(cleanTicker)) {
            return "/quote/IBOV:INDEXBVMF";
        }
        if ("IDIV".equals(cleanTicker)) {
            return "/quote/IDIV:INDEXBVMF";
        }
        return "/quote/" + cleanTicker + ":BVMF";
    }

    private BigDecimal parsePriceFromHtml(String html) {
        Matcher mPrice = DATA_PRICE_PATTERN.matcher(html);
        if (mPrice.find()) {
            try {
                return new BigDecimal(mPrice.group(1)).setScale(2, RoundingMode.HALF_UP);
            } catch (Exception ignored) {}
        }

        Matcher mPdsbrc = PDSBRC_PATTERN.matcher(html);
        if (mPdsbrc.find()) {
            BigDecimal val = parseCleanNumber(mPdsbrc.group(1));
            if (val != null) return val;
        }

        Matcher mYm = YMLKEC_PATTERN.matcher(html);
        if (mYm.find()) {
            return parseCleanNumber(mYm.group(1));
        }

        return null;
    }

    private BigDecimal parseCleanNumber(String text) {
        if (text == null) return null;
        String cleaned = text.replace("R$", "")
                .replace("\u00A0", "")
                .replace(" ", "")
                .trim();

        try {
            if (cleaned.contains(",") && cleaned.contains(".")) {
                if (cleaned.lastIndexOf(',') > cleaned.lastIndexOf('.')) {
                    cleaned = cleaned.replace(".", "").replace(",", ".");
                } else {
                    cleaned = cleaned.replace(",", "");
                }
            } else if (cleaned.contains(",")) {
                cleaned = cleaned.replace(",", ".");
            }
            return new BigDecimal(cleaned).setScale(2, RoundingMode.HALF_UP);
        } catch (Exception e) {
            return null;
        }
    }

    private BigDecimal fetchFallbackFromYahoo(String cleanTicker) {
        try {
            String yahooSymbol = cleanTicker;
            if ("IBOV".equals(cleanTicker)) yahooSymbol = "^BVSP";
            LocalDate now = LocalDate.now();
            List<HistoricalPrice> prices = yahooFinanceFallback.fetchPrices(
                    yahooSymbol,
                    now.minusDays(7),
                    now,
                    Periodicity.SEMANAL
            );
            if (prices != null && !prices.isEmpty()) {
                return prices.get(prices.size() - 1).getPrecoFechamento();
            }
        } catch (Exception e) {
            System.err.println("YahooFinance fallback falhou para " + cleanTicker + ": " + e.getMessage());
        }
        return null;
    }
}
