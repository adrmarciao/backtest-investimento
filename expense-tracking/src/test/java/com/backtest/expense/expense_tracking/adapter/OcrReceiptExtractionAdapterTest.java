package com.backtest.expense.expense_tracking.adapter;

import com.backtest.expense.expense_tracking.model.Expense;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class OcrReceiptExtractionAdapterTest {

    @Test
    public void testParseTextToExpense_ValidData() {
        OcrReceiptExtractionAdapter adapter = new OcrReceiptExtractionAdapter();
        String ocrText = "Supermercado XYZ\n" +
                "Rua Falsa 123\n" +
                "CNPJ: 00.000.000/0001-00\n" +
                "Data: 10/10/2026\n" +
                "--------------------\n" +
                "1 Leite 5.00\n" +
                "2 Pao 10.00\n" +
                "--------------------\n" +
                "TOTAL R$ 15,00\n" +
                "Obrigado volte sempre";

        Expense expense = adapter.parseTextToExpense(ocrText);

        assertNotNull(expense);
        assertEquals("Supermercado XYZ", expense.getStoreName());
        assertEquals(LocalDate.of(2026, 10, 10), expense.getDate());
        assertEquals(new BigDecimal("15.00"), expense.getTotalAmount());
    }

    @Test
    public void testParseTextToExpense_MissingData() {
        OcrReceiptExtractionAdapter adapter = new OcrReceiptExtractionAdapter();
        String ocrText = " \n\nLoja Desconhecida";

        Expense expense = adapter.parseTextToExpense(ocrText);

        assertNotNull(expense);
        assertEquals("Loja Desconhecida", expense.getStoreName());
        assertNotNull(expense.getDate()); // Fallback to current date
    }
}
