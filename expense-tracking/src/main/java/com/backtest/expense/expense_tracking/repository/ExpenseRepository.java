package com.backtest.expense.expense_tracking.repository;

import com.backtest.expense.expense_tracking.model.Expense;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExpenseRepository extends MongoRepository<Expense, String> {
}
