import React, { useState, useEffect } from 'react';
import { Dialog, DialogTitle, DialogContent, DialogActions, Button, TextField, Box } from '@mui/material';
import { updateExpense } from '../services/expenseApi';

export default function ExpenseEditModal({ expense, open, onClose, onSave }) {
  const [storeName, setStoreName] = useState('');
  const [date, setDate] = useState('');
  const [totalAmount, setTotalAmount] = useState('');
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    if (expense) {
      setStoreName(expense.storeName || '');
      // Ensure date is in YYYY-MM-DD format for the date input
      setDate(expense.date ? expense.date.substring(0, 10) : '');
      setTotalAmount(expense.totalAmount || '');
    }
  }, [expense]);

  const handleSave = async () => {
    setLoading(true);
    try {
      await updateExpense(expense.id, {
        ...expense,
        storeName,
        date,
        totalAmount: parseFloat(totalAmount),
      });
      onSave();
    } catch (error) {
      console.error("Failed to update expense", error);
    } finally {
      setLoading(false);
    }
  };

  return (
    <Dialog open={open} onClose={onClose} PaperProps={{ sx: { bgcolor: '#171c26', color: '#fff', minWidth: 300 } }}>
      <DialogTitle>Editar Despesa</DialogTitle>
      <DialogContent>
        <Box sx={{ display: 'flex', flexDirection: 'column', gap: 2, mt: 1 }}>
          <TextField
            label="Loja"
            value={storeName}
            onChange={(e) => setStoreName(e.target.value)}
            fullWidth
            InputProps={{ sx: { color: '#fff' } }}
            InputLabelProps={{ sx: { color: '#a0a3ab' } }}
            sx={{
              '& .MuiOutlinedInput-root': {
                '& fieldset': { borderColor: '#43474e' },
                '&:hover fieldset': { borderColor: '#a0a3ab' },
              }
            }}
          />
          <TextField
            label="Data"
            type="date"
            value={date}
            onChange={(e) => setDate(e.target.value)}
            fullWidth
            InputProps={{ sx: { color: '#fff' } }}
            InputLabelProps={{ shrink: true, sx: { color: '#a0a3ab' } }}
            sx={{
              '& .MuiOutlinedInput-root': {
                '& fieldset': { borderColor: '#43474e' },
                '&:hover fieldset': { borderColor: '#a0a3ab' },
              }
            }}
          />
          <TextField
            label="Total (R$)"
            type="number"
            value={totalAmount}
            onChange={(e) => setTotalAmount(e.target.value)}
            fullWidth
            InputProps={{ sx: { color: '#fff' } }}
            InputLabelProps={{ sx: { color: '#a0a3ab' } }}
            sx={{
              '& .MuiOutlinedInput-root': {
                '& fieldset': { borderColor: '#43474e' },
                '&:hover fieldset': { borderColor: '#a0a3ab' },
              }
            }}
          />
        </Box>
      </DialogContent>
      <DialogActions>
        <Button onClick={onClose} sx={{ color: '#a0a3ab' }}>Cancelar</Button>
        <Button onClick={handleSave} disabled={loading} sx={{ color: '#d4e3ff' }}>
          {loading ? 'Salvando...' : 'Salvar'}
        </Button>
      </DialogActions>
    </Dialog>
  );
}
