import React, { useState } from 'react';
import { Box, Typography, Card, CardContent, CircularProgress, IconButton, Button, Dialog, DialogTitle, DialogContent, DialogContentText, DialogActions } from '@mui/material';
import EditIcon from '@mui/icons-material/Edit';
import DeleteIcon from '@mui/icons-material/Delete';
import { deleteExpense } from '../services/expenseApi';
import ExpenseEditModal from './ExpenseEditModal';

export default function ExpenseList({ expenses, loading, onRefresh }) {
  const [editingExpense, setEditingExpense] = useState(null);
  const [deletingExpense, setDeletingExpense] = useState(null);

  const handleDelete = async () => {
    if (deletingExpense) {
      try {
        await deleteExpense(deletingExpense.id);
        setDeletingExpense(null);
        onRefresh();
      } catch (err) {
        console.error("Failed to delete expense", err);
      }
    }
  };

  if (loading) {
    return <CircularProgress />;
  }

  if (expenses.length === 0) {
    return <Typography color="text.secondary">Nenhuma despesa registrada ainda.</Typography>;
  }

  return (
    <Box>
      {expenses.map((expense) => (
        <Card key={expense.id} sx={{ mb: 2, bgcolor: '#171c26', border: '1px solid #43474e' }}>
          <CardContent sx={{ position: 'relative' }}>
            <Box sx={{ position: 'absolute', top: 8, right: 8 }}>
              <IconButton size="small" onClick={() => setEditingExpense(expense)} sx={{ color: '#a0a3ab' }}>
                <EditIcon fontSize="small" />
              </IconButton>
              <IconButton size="small" onClick={() => setDeletingExpense(expense)} sx={{ color: '#ff897d' }}>
                <DeleteIcon fontSize="small" />
              </IconButton>
            </Box>

            <Typography variant="h6" sx={{ color: '#d4e3ff' }}>{expense.storeName}</Typography>
            <Typography variant="body2" sx={{ color: '#a0a3ab', mb: 1 }}>
              Data: {expense.date ? new Date(expense.date).toLocaleDateString() : 'N/A'}
            </Typography>
            
            <Box sx={{ mt: 2, mb: 1 }}>
              {expense.items && expense.items.map((item, idx) => (
                <Typography key={idx} variant="body2" sx={{ color: '#c4c7d0' }}>
                  • {item.description}: R$ {item.price}
                </Typography>
              ))}
            </Box>
            
            <Typography variant="subtitle1" sx={{ mt: 2, fontWeight: 'bold', color: '#fff' }}>
              Total: R$ {expense.totalAmount}
            </Typography>
          </CardContent>
        </Card>
      ))}

      {/* Delete Confirmation Dialog */}
      <Dialog
        open={Boolean(deletingExpense)}
        onClose={() => setDeletingExpense(null)}
        PaperProps={{ sx: { bgcolor: '#171c26', color: '#fff' } }}
      >
        <DialogTitle>Confirmar Exclusão</DialogTitle>
        <DialogContent>
          <DialogContentText sx={{ color: '#a0a3ab' }}>
            Tem certeza que deseja excluir a despesa "{deletingExpense?.storeName}"? Esta ação não pode ser desfeita.
          </DialogContentText>
        </DialogContent>
        <DialogActions>
          <Button onClick={() => setDeletingExpense(null)} sx={{ color: '#a0a3ab' }}>Cancelar</Button>
          <Button onClick={handleDelete} sx={{ color: '#ff897d' }} autoFocus>
            Excluir
          </Button>
        </DialogActions>
      </Dialog>

      {/* Edit Modal */}
      {editingExpense && (
        <ExpenseEditModal
          expense={editingExpense}
          open={Boolean(editingExpense)}
          onClose={() => setEditingExpense(null)}
          onSave={() => {
            setEditingExpense(null);
            onRefresh();
          }}
        />
      )}
    </Box>
  );
}
