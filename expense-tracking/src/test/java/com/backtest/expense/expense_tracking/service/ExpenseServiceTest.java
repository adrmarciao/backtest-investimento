package com.backtest.expense.expense_tracking.service;

import com.backtest.expense.expense_tracking.model.Expense;
import com.backtest.expense.expense_tracking.port.ReceiptExtractionPort;
import com.backtest.expense.expense_tracking.repository.ExpenseRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ExpenseServiceTest {

    @Mock
    private ReceiptExtractionPort extractionPort;

    @Mock
    private ExpenseRepository expenseRepository;

    @InjectMocks
    private ExpenseService expenseService;

    @Test
    void shouldProcessReceiptAndSaveExpense() {
        // Arrange
        byte[] fakeImage = new byte[]{1, 2, 3};
        String contentType = "image/jpeg";
        
        Expense mockExtractedExpense = new Expense("Test Store", LocalDate.now(), new BigDecimal("100.00"), List.of());
        when(extractionPort.extractExpenseFromReceipt(fakeImage, contentType)).thenReturn(mockExtractedExpense);
        
        Expense savedExpense = new Expense("Test Store", LocalDate.now(), new BigDecimal("100.00"), List.of());
        savedExpense.setId("test-id-123");
        when(expenseRepository.save(any(Expense.class))).thenReturn(savedExpense);

        // Act
        Expense result = expenseService.processReceipt(fakeImage, contentType);

        // Assert
        assertEquals("test-id-123", result.getId());
        assertEquals("Test Store", result.getStoreName());
        
        verify(extractionPort).extractExpenseFromReceipt(fakeImage, contentType);
        verify(expenseRepository).save(mockExtractedExpense);
    }

    @Test
    void shouldUpdateExpense() {
        // Arrange
        String expenseId = "test-id";
        Expense existingExpense = new Expense("Old Store", LocalDate.now(), new BigDecimal("50.00"), List.of());
        existingExpense.setId(expenseId);
        
        Expense updatedExpense = new Expense("New Store", LocalDate.now(), new BigDecimal("100.00"), List.of());
        
        when(expenseRepository.findById(expenseId)).thenReturn(java.util.Optional.of(existingExpense));
        when(expenseRepository.save(any(Expense.class))).thenAnswer(i -> i.getArgument(0));

        // Act
        Expense result = expenseService.updateExpense(expenseId, updatedExpense);

        // Assert
        assertEquals("New Store", result.getStoreName());
        assertEquals(new BigDecimal("100.00"), result.getTotalAmount());
        verify(expenseRepository).findById(expenseId);
        verify(expenseRepository).save(existingExpense);
    }

    @Test
    void shouldDeleteExpense() {
        // Arrange
        String expenseId = "test-id";

        // Act
        expenseService.deleteExpense(expenseId);

        // Assert
        verify(expenseRepository).deleteById(expenseId);
    }
}
