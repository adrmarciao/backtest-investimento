package com.backtest.expense.expense_tracking.adapter;

import com.backtest.expense.expense_tracking.model.Expense;
import com.backtest.expense.expense_tracking.port.ReceiptExtractionPort;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Component
public class GenerativeAIReceiptExtractionAdapter implements ReceiptExtractionPort {

    private final String apiKey;

    public GenerativeAIReceiptExtractionAdapter(@Value("${ai.api.key}") String apiKey) {
        this.apiKey = apiKey;
    }

    @Override
    public Expense extractExpenseFromReceipt(byte[] imageBytes, String contentType) {
        // Mock implementation for now
        // In a real scenario, this would call Gemini/OpenAI API with the image bytes
        return new Expense("Generative AI Store", LocalDate.now(), new BigDecimal("99.99"), List.of());
    }
}
