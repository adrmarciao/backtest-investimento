package com.backtest.expense.expense_tracking.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Document(collection = "expenses")
public class Expense {
    @Id
    private String id;
    private String storeName;
    private LocalDate date;
    private BigDecimal totalAmount;
    private List<ExpenseItem> items;

    public Expense() {}

    public Expense(String storeName, LocalDate date, BigDecimal totalAmount, List<ExpenseItem> items) {
        this.storeName = storeName;
        this.date = date;
        this.totalAmount = totalAmount;
        this.items = items;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getStoreName() { return storeName; }
    public void setStoreName(String storeName) { this.storeName = storeName; }
    
    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }
    
    public BigDecimal getTotalAmount() { return totalAmount; }
    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }
    
    public List<ExpenseItem> getItems() { return items; }
    public void setItems(List<ExpenseItem> items) { this.items = items; }
}
