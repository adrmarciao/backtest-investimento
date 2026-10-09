package com.backtest.expense.expense_tracking.model;

import java.math.BigDecimal;

public class ExpenseItem {
    private String description;
    private BigDecimal price;

    public ExpenseItem() {}

    public ExpenseItem(String description, BigDecimal price) {
        this.description = description;
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
