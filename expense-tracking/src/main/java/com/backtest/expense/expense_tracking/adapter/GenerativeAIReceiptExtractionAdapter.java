package com.backtest.expense.expense_tracking.adapter;

import com.backtest.expense.expense_tracking.model.Expense;
import com.backtest.expense.expense_tracking.port.ReceiptExtractionPort;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class GenerativeAIReceiptExtractionAdapter implements ReceiptExtractionPort {

    private final String apiKey;
    private final String model;
    private final int thinkingBudget;
    private final WebClient webClient;
    private final ObjectMapper objectMapper;

    public GenerativeAIReceiptExtractionAdapter(
            @Value("${ai.api.key}") String apiKey,
            @Value("${ai.model:gemini-2.5-flash}") String model,
            @Value("${ai.thinking-budget:0}") int thinkingBudget,
            WebClient.Builder webClientBuilder,
            ObjectMapper objectMapper) {
        this.apiKey = apiKey;
        this.model = model;
        this.thinkingBudget = thinkingBudget;
        this.webClient = webClientBuilder.baseUrl("https://generativelanguage.googleapis.com").build();
        this.objectMapper = objectMapper;
    }

    @Override
    public Expense extractExpenseFromReceipt(byte[] imageBytes, String contentType) {
        String base64Image = Base64.getEncoder().encodeToString(imageBytes);

        String mimeType = contentType;
        if (mimeType == null || !mimeType.startsWith("image/")) {
            mimeType = "image/jpeg";
        }

        String prompt = "Extract information from this receipt. Return ONLY a JSON object with this exact structure: {\"storeName\": \"Name of store\", \"date\": \"YYYY-MM-DD\", \"totalAmount\": 12.34, \"items\": [{\"description\": \"Item name\", \"price\": 1.23}]}";

        Map<String, Object> requestBody = Map.of(
            "contents", List.of(
                Map.of(
                    "parts", List.of(
                        Map.of("text", prompt),
                        Map.of(
                            "inline_data", Map.of(
                                "mime_type", mimeType,
                                "data", base64Image
                            )
                        )
                    )
                )
            ),
            "generationConfig", buildGenerationConfig()
        );

        try {
            String responseStr = webClient.post()
                    .uri("/v1beta/models/{model}:generateContent", model)
                    .header("x-goog-api-key", apiKey)
                    .bodyValue(requestBody)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            JsonNode rootNode = objectMapper.readTree(responseStr);
            String contentText = rootNode.path("candidates").path(0)
                    .path("content").path("parts").path(0)
                    .path("text").asText();
            
            return objectMapper.readValue(contentText, Expense.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to extract expense from receipt using Gemini API", e);
        }
    }

    private Map<String, Object> buildGenerationConfig() {
        Map<String, Object> config = new HashMap<>();
        config.put("responseMimeType", "application/json");
        // Thinking aumenta muito a latência e não agrega para extração simples de recibo
        if (thinkingBudget >= 0) {
            config.put("thinkingConfig", Map.of("thinkingBudget", thinkingBudget));
        }
        return config;
    }
}
