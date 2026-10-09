package com.backtest.expense.expense_tracking.controller;

import com.backtest.expense.expense_tracking.model.Expense;
import com.backtest.expense.expense_tracking.service.ExpenseService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/expenses")
@CrossOrigin(origins = "*")
public class ExpenseController {

    private final ExpenseService expenseService;

    public ExpenseController(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    @PostMapping("/upload")
    public ResponseEntity<Expense> uploadReceipt(@RequestParam("file") MultipartFile file,
                                                 @RequestParam(value = "token", required = false) String token) throws IOException {
        Expense expense = expenseService.processReceipt(file.getBytes(), file.getContentType());
        return ResponseEntity.ok(expense);
    }

    @GetMapping
    public ResponseEntity<List<Expense>> listExpenses() {
        return ResponseEntity.ok(expenseService.listAllExpenses());
    }
}
