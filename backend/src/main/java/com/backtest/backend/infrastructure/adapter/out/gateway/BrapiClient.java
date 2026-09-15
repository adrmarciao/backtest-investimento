package com.backtest.backend.infrastructure.adapter.out.gateway;

import com.backtest.backend.domain.port.out.FundamentalDataGatewayPort.FundamentalData;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class BrapiClient {

    private final WebClient brapiWebClient;
    private final WebClient fundamentusWebClient;

    private static final java.util.regex.Pattern LPA_PATTERN = java.util.regex.Pattern.compile("\\?</span><span class=\"txt\">LPA</span></td>\\s*<td class=\"data w2\"><span class=\"txt\">([0-9.,-]+)</span>");
    private static final java.util.regex.Pattern VPA_PATTERN = java.util.regex.Pattern.compile("\\?</span><span class=\"txt\">VPA</span></td>\\s*<td class=\"data w2\"><span class=\"txt\">([0-9.,-]+)</span>");

    public BrapiClient(WebClient.Builder webClientBuilder) {
        this.brapiWebClient = webClientBuilder
                .baseUrl("https://brapi.dev")
                .defaultHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64)")
                .build();

        this.fundamentusWebClient = webClientBuilder
                .baseUrl("https://www.fundamentus.com.br")
                .defaultHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/124.0.0.0 Safari/537.36")
                .build();
    }

    public Map<String, FundamentalData> fetchFundamentals(List<String> tickers, String token) {
        Map<String, FundamentalData> result = new HashMap<>();
        if (tickers == null || tickers.isEmpty()) return result;

        for (String ticker : tickers) {
            if (ticker == null || ticker.isBlank()) continue;
            String cleanTicker = ticker.trim().toUpperCase();

            BigDecimal lpa = null;
            BigDecimal vpa = null;

            // 1. Consultar Brapi individualmente (1 ativo por requisição para compatibilidade com qualquer plano)
            if (token != null && !token.isBlank()) {
                try {
                    JsonNode response = brapiWebClient.get()
                            .uri(uriBuilder -> uriBuilder
                                    .path("/api/quote/{ticker}")
                                    .queryParam("token", token.trim())
                                    .queryParam("fundamental", "true")
                                    .build(cleanTicker))
                            .retrieve()
                            .bodyToMono(JsonNode.class)
                            .block();

                    if (response != null && response.has("results")) {
                        JsonNode results = response.path("results");
                        if (results.isArray() && !results.isEmpty()) {
                            JsonNode item = results.get(0);
                            lpa = extractLpa(item);
                            vpa = extractVpa(item);
                        }
                    }
                } catch (Exception e) {
                    System.err.println("BrapiClient: Aviso ao consultar Brapi para " + cleanTicker + ": " + e.getMessage());
                }
            }

            // 2. Se VPA ou LPA ainda forem nulos, consultar Fundamentus como complemento
            if (vpa == null || lpa == null) {
                try {
                    FundamentalData fundData = fetchFromFundamentus(cleanTicker);
                    if (fundData != null) {
                        if (lpa == null && fundData.lpa() != null) {
                            lpa = fundData.lpa();
                        }
                        if (vpa == null && fundData.vpa() != null) {
                            vpa = fundData.vpa();
                        }
                    }
                } catch (Exception e) {
                    System.err.println("BrapiClient: Falha ao consultar Fundamentus para " + cleanTicker + ": " + e.getMessage());
                }
            }

            if (lpa != null || vpa != null) {
                result.put(cleanTicker, new FundamentalData(lpa, vpa));
            }

            // Pequena pausa defensiva de 50ms para evitar rate limiting
            try {
                Thread.sleep(50);
            } catch (InterruptedException ignored) {
                Thread.currentThread().interrupt();
            }
        }

        return result;
    }

    private FundamentalData fetchFromFundamentus(String ticker) {
        String queryTicker = ticker;
        if ("TRPL4".equals(ticker)) {
            FundamentalData isae = fetchFundamentusDirect("ISAE4");
            if (isae != null && (isae.lpa() != null || isae.vpa() != null)) {
                return isae;
            }
        }
        return fetchFundamentusDirect(queryTicker);
    }

    private FundamentalData fetchFundamentusDirect(String papel) {
        try {
            String html = fundamentusWebClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/detalhes.php")
                            .queryParam("papel", papel)
                            .build())
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            if (html == null || html.isBlank()) return null;

            BigDecimal lpa = null;
            BigDecimal vpa = null;

            java.util.regex.Matcher mLpa = LPA_PATTERN.matcher(html);
            if (mLpa.find()) {
                lpa = parseBrNumber(mLpa.group(1));
            }

            java.util.regex.Matcher mVpa = VPA_PATTERN.matcher(html);
            if (mVpa.find()) {
                vpa = parseBrNumber(mVpa.group(1));
            }

            return new FundamentalData(lpa, vpa);
        } catch (Exception e) {
            return null;
        }
    }

    private BigDecimal parseBrNumber(String text) {
        if (text == null || text.isBlank() || "-".equals(text.trim())) return null;
        try {
            String clean = text.replace(".", "").replace(",", ".").trim();
            return new BigDecimal(clean).setScale(4, RoundingMode.HALF_UP);
        } catch (Exception e) {
            return null;
        }
    }

    private BigDecimal extractLpa(JsonNode item) {
        if (item.hasNonNull("earningsPerShare")) {
            return BigDecimal.valueOf(item.get("earningsPerShare").asDouble()).setScale(4, RoundingMode.HALF_UP);
        }
        if (item.path("defaultKeyStatistics").hasNonNull("earningsPerShare")) {
            return BigDecimal.valueOf(item.path("defaultKeyStatistics").get("earningsPerShare").asDouble()).setScale(4, RoundingMode.HALF_UP);
        }
        if (item.path("financialData").hasNonNull("earningsPerShare")) {
            return BigDecimal.valueOf(item.path("financialData").get("earningsPerShare").asDouble()).setScale(4, RoundingMode.HALF_UP);
        }
        return null;
    }

    private BigDecimal extractVpa(JsonNode item) {
        if (item.hasNonNull("bookValuePerShare")) {
            return BigDecimal.valueOf(item.get("bookValuePerShare").asDouble()).setScale(4, RoundingMode.HALF_UP);
        }
        if (item.path("defaultKeyStatistics").hasNonNull("bookValuePerShare")) {
            return BigDecimal.valueOf(item.path("defaultKeyStatistics").get("bookValuePerShare").asDouble()).setScale(4, RoundingMode.HALF_UP);
        }
        if (item.path("defaultKeyStatistics").hasNonNull("bookValue")) {
            return BigDecimal.valueOf(item.path("defaultKeyStatistics").get("bookValue").asDouble()).setScale(4, RoundingMode.HALF_UP);
        }
        return null;
    }
}
