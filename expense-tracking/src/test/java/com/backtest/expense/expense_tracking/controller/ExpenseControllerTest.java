package com.backtest.expense.expense_tracking.controller;

import com.backtest.expense.expense_tracking.model.Expense;
import com.backtest.expense.expense_tracking.service.ExpenseService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.http.MediaType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ExpenseController.class)
class ExpenseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ExpenseService expenseService;

    @Test
    void shouldUploadReceipt() throws Exception {
        Expense mockExpense = new Expense("Test", LocalDate.now(), new BigDecimal("10.00"), List.of());
        mockExpense.setId("123");
        when(expenseService.processReceipt(any(), any())).thenReturn(mockExpense);

        MockMultipartFile file = new MockMultipartFile("file", "receipt.jpg", "image/jpeg", "fake-image".getBytes());

        mockMvc.perform(multipart("/api/expenses/upload")
                        .file(file)
                        .param("token", "fake-token"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("123"))
                .andExpect(jsonPath("$.storeName").value("Test"));
    }

    @Test
    void shouldListExpenses() throws Exception {
        Expense mockExpense = new Expense("Test", LocalDate.now(), new BigDecimal("10.00"), List.of());
        mockExpense.setId("123");
        when(expenseService.listAllExpenses()).thenReturn(List.of(mockExpense));

        mockMvc.perform(get("/api/expenses"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("123"));
    }

    @Test
    void shouldUpdateExpense() throws Exception {
        Expense mockExpense = new Expense("Updated Store", LocalDate.now(), new BigDecimal("20.00"), List.of());
        mockExpense.setId("123");
        when(expenseService.updateExpense(eq("123"), any())).thenReturn(mockExpense);

        String json = "{\"storeName\":\"Updated Store\",\"date\":\"2026-10-09\",\"totalAmount\":20.00,\"items\":[]}";

        mockMvc.perform(put("/api/expenses/123")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("123"))
                .andExpect(jsonPath("$.storeName").value("Updated Store"));
    }

    @Test
    void shouldDeleteExpense() throws Exception {
        mockMvc.perform(delete("/api/expenses/123"))
                .andExpect(status().isNoContent());
    }
}
