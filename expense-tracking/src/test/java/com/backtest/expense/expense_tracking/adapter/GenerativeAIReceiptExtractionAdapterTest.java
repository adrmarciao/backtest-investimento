package com.backtest.expense.expense_tracking.adapter;

import com.backtest.expense.expense_tracking.model.Expense;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GenerativeAIReceiptExtractionAdapterTest {

    @Test
    @Disabled("Test requires actual Gemini API call or deep WebClient mocking. Disabled for now.")
    void shouldExtractExpense() {
        WebClient.Builder webClientBuilder = mock(WebClient.Builder.class);
        WebClient webClient = mock(WebClient.class);
        when(webClientBuilder.baseUrl(org.mockito.ArgumentMatchers.anyString())).thenReturn(webClientBuilder);
        when(webClientBuilder.build()).thenReturn(webClient);
        
        GenerativeAIReceiptExtractionAdapter adapter = new GenerativeAIReceiptExtractionAdapter("fake-key", "gemini-2.5-flash", 0, webClientBuilder, new ObjectMapper());
        Expense expense = adapter.extractExpenseFromReceipt(new byte[]{1, 2, 3}, "image/jpeg");

        assertNotNull(expense);
    }
}
