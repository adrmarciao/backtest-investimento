export const getExpenses = async () => {
    const response = await fetch('/api/expenses');
    if (!response.ok) {
        throw new Error('Failed to fetch expenses');
    }
    return response.json();
};

export const updateExpense = async (id, expenseData) => {
    const response = await fetch(`/api/expenses/${id}`, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(expenseData),
    });
    if (!response.ok) {
        throw new Error('Failed to update expense');
    }
    return response.json();
};

export const deleteExpense = async (id) => {
    const response = await fetch(`/api/expenses/${id}`, {
        method: 'DELETE',
    });
    if (!response.ok) {
        throw new Error('Failed to delete expense');
    }
};
