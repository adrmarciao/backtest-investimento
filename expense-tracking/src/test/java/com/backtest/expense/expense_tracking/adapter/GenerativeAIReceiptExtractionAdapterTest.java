package com.backtest.expense.expense_tracking.adapter;

import com.backtest.expense.expense_tracking.model.Expense;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class GenerativeAIReceiptExtractionAdapterTest {

    @Test
    void shouldExtractExpense() {
        GenerativeAIReceiptExtractionAdapter adapter = new GenerativeAIReceiptExtractionAdapter("fake-key");
        Expense expense = adapter.extractExpenseFromReceipt(new byte[]{1, 2, 3}, "image/jpeg");

        assertNotNull(expense);
        assertEquals("Generative AI Store", expense.getStoreName());
        assertEquals(new BigDecimal("99.99"), expense.getTotalAmount());
    }
}
