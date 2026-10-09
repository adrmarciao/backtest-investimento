package com.backtest.expense.expense_tracking.repository;

import com.backtest.expense.expense_tracking.model.Expense;
import com.backtest.expense.expense_tracking.model.ExpenseItem;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ExpenseRepositoryTest {

    @Autowired
    private ExpenseRepository expenseRepository;

    @AfterEach
    void tearDown() {
        expenseRepository.deleteAll();
    }

    @Test
    void shouldSaveAndLoadExpense() {
        // Arrange
        ExpenseItem item = new ExpenseItem("Coffee", new BigDecimal("4.50"));
        Expense expense = new Expense("Cafe", LocalDate.now(), new BigDecimal("4.50"), List.of(item));

        // Act
        Expense savedExpense = expenseRepository.save(expense);
        
        // Assert
        assertNotNull(savedExpense.getId());
        
        Optional<Expense> loadedExpenseOpt = expenseRepository.findById(savedExpense.getId());
        assertTrue(loadedExpenseOpt.isPresent());
        
        Expense loadedExpense = loadedExpenseOpt.get();
        assertEquals("Cafe", loadedExpense.getStoreName());
        assertEquals(new BigDecimal("4.50"), loadedExpense.getTotalAmount());
        assertEquals(1, loadedExpense.getItems().size());
        assertEquals("Coffee", loadedExpense.getItems().get(0).getDescription());
    }
}
