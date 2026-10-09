import React, { useState, useEffect } from 'react';
import { Box, Typography, Card, Grid } from '@mui/material';
import { QRCodeCanvas } from 'qrcode.react';
import { getExpenses } from '../services/expenseApi';
import ExpenseList from './ExpenseList';

export default function ExpensesTab() {
  const [expenses, setExpenses] = useState([]);
  const [loading, setLoading] = useState(true);

  const fetchExpenses = async () => {
    setLoading(true);
    try {
      const data = await getExpenses();
      setExpenses(data);
    } catch (err) {
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchExpenses();
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
          <ExpenseList expenses={expenses} loading={loading} onRefresh={fetchExpenses} />
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
