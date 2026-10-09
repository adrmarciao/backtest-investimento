package com.backtest.expense.expense_tracking.service;

import com.backtest.expense.expense_tracking.model.Expense;
import com.backtest.expense.expense_tracking.port.ReceiptExtractionPort;
import com.backtest.expense.expense_tracking.repository.ExpenseRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExpenseService {

    private final ReceiptExtractionPort extractionPort;
    private final ExpenseRepository expenseRepository;

    public ExpenseService(ReceiptExtractionPort extractionPort, ExpenseRepository expenseRepository) {
        this.extractionPort = extractionPort;
        this.expenseRepository = expenseRepository;
    }

    public Expense processReceipt(byte[] imageBytes, String contentType) {
        Expense extractedExpense = extractionPort.extractExpenseFromReceipt(imageBytes, contentType);
        return expenseRepository.save(extractedExpense);
    }

    public List<Expense> listAllExpenses() {
        return expenseRepository.findAll();
    }

    public Expense updateExpense(String id, Expense updatedExpense) {
        return expenseRepository.findById(id).map(existingExpense -> {
            existingExpense.setStoreName(updatedExpense.getStoreName());
            existingExpense.setDate(updatedExpense.getDate());
            existingExpense.setTotalAmount(updatedExpense.getTotalAmount());
            existingExpense.setItems(updatedExpense.getItems());
            return expenseRepository.save(existingExpense);
        }).orElseThrow(() -> new RuntimeException("Expense not found with id " + id));
    }

    public void deleteExpense(String id) {
        expenseRepository.deleteById(id);
    }
}
