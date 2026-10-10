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

      <Box sx={{ display: 'flex', flexDirection: 'column', gap: 5 }}>
        <Box>
          <ExpenseList expenses={expenses} loading={loading} onRefresh={fetchExpenses} />
        </Box>
        
        <Card sx={{ 
          bgcolor: '#1e2330', 
          textAlign: 'center', 
          p: 4, 
          border: '1px solid #43474e', 
          borderRadius: 3,
          boxShadow: '0 8px 32px rgba(0,0,0,0.4)',
          maxWidth: 600,
          mx: 'auto',
          width: '100%'
        }}>
          <Typography variant="h5" sx={{ mb: 2, color: '#e2e2e9', fontWeight: 'bold' }}>
            Scanner de Recibo Mobile
          </Typography>
          <Typography variant="body1" sx={{ mb: 4, color: '#a0a3ab' }}>
            Escaneie este QR Code com seu celular para capturar e enviar recibos de forma rápida e fácil.
          </Typography>
          <Box sx={{ 
            display: 'inline-flex', 
            bgcolor: '#fff', 
            p: 3, 
            borderRadius: 3,
            boxShadow: '0 4px 12px rgba(0,0,0,0.1)'
          }}>
            <QRCodeCanvas value={uploadUrl} size={180} level="H" />
          </Box>
          <Typography variant="caption" display="block" sx={{ mt: 3, wordBreak: 'break-all', color: '#7a7e86', fontFamily: 'monospace' }}>
            {uploadUrl}
          </Typography>
        </Card>
      </Box>
    </Box>
  );
}
