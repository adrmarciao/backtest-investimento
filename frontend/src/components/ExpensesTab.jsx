import React, { useState, useEffect } from 'react';
import { Box, Typography, Card, CardContent, Grid, CircularProgress } from '@mui/material';
import { QRCodeCanvas } from 'qrcode.react';

export default function ExpensesTab() {
  const [expenses, setExpenses] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    fetch('/api/expenses')
      .then(res => res.json())
      .then(data => {
        setExpenses(data);
        setLoading(false);
      })
      .catch(err => {
        console.error(err);
        setLoading(false);
      });
  }, []);

  // Use the current origin + /upload path
  const uploadUrl = `${window.location.origin}/upload?token=secure-test-token`;

  return (
    <Box>
      <Typography variant="h5" sx={{ mb: 3, fontWeight: 'bold' }}>
        Controle de Gastos
      </Typography>

      <Grid container spacing={3}>
        <Grid item xs={12} md={8}>
          {loading ? (
            <CircularProgress />
          ) : (
            <Box>
              {expenses.length === 0 ? (
                <Typography color="text.secondary">Nenhuma despesa registrada ainda.</Typography>
              ) : (
                expenses.map((expense) => (
                  <Card key={expense.id} sx={{ mb: 2, bgcolor: '#171c26', border: '1px solid #43474e' }}>
                    <CardContent>
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
                ))
              )}
            </Box>
          )}
        </Grid>
        
        <Grid item xs={12} md={4}>
          <Card sx={{ bgcolor: '#171c26', textAlign: 'center', p: 3, border: '1px solid #43474e' }}>
            <Typography variant="h6" sx={{ mb: 2, color: '#fff' }}>
              Scanner de Recibo Mobile
            </Typography>
            <Typography variant="body2" sx={{ mb: 3, color: '#a0a3ab' }}>
              Escaneie este QR Code com seu celular para capturar e enviar recibos.
            </Typography>
            <Box sx={{ display: 'inline-flex', bgcolor: '#fff', p: 2, borderRadius: 2 }}>
              <QRCodeCanvas value={uploadUrl} size={150} level="H" />
            </Box>
            <Typography variant="caption" display="block" sx={{ mt: 2, wordBreak: 'break-all', color: '#7a7e86' }}>
              {uploadUrl}
            </Typography>
          </Card>
        </Grid>
      </Grid>
    </Box>
  );
}
