package com.backtest.expense.expense_tracking.port;

import com.backtest.expense.expense_tracking.model.Expense;

public interface ReceiptExtractionPort {
    Expense extractExpenseFromReceipt(byte[] imageBytes, String contentType);
}
