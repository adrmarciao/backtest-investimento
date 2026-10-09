import React, { useState, useEffect } from 'react';
import { Box, Typography, Button, CircularProgress, Alert } from '@mui/material';
import CloudUploadIcon from '@mui/icons-material/CloudUpload';

export default function UploadPage() {
  const [file, setFile] = useState(null);
  const [uploading, setUploading] = useState(false);
  const [success, setSuccess] = useState(false);
  const [error, setError] = useState('');
  const [token, setToken] = useState('');

  useEffect(() => {
    const params = new URLSearchParams(window.location.search);
    setToken(params.get('token') || '');
  }, []);

  const handleFileChange = (e) => {
    if (e.target.files && e.target.files.length > 0) {
      setFile(e.target.files[0]);
    }
  };

  const handleUpload = async () => {
    if (!file) return;
    setUploading(true);
    setError('');
    
    const formData = new FormData();
    formData.append('file', file);
    formData.append('token', token);

    try {
      const res = await fetch('/api/expenses/upload', {
        method: 'POST',
        body: formData,
      });

      if (!res.ok) {
        throw new Error('Falha no upload');
      }

      setSuccess(true);
      setFile(null);
    } catch (err) {
      setError(err.message || 'Ocorreu um erro');
    } finally {
      setUploading(false);
    }
  };

  return (
    <Box sx={{ p: 3, display: 'flex', flexDirection: 'column', alignItems: 'center', minHeight: '100vh', bgcolor: '#0f141d', color: '#fff' }}>
      <Typography variant="h5" sx={{ mb: 4, fontWeight: 'bold' }}>
        Scanner de Recibo
      </Typography>

      {success ? (
        <Alert severity="success" sx={{ mb: 3 }}>Recibo enviado com sucesso!</Alert>
      ) : null}

      {error ? (
        <Alert severity="error" sx={{ mb: 3 }}>{error}</Alert>
      ) : null}

      <Button
        variant="contained"
        component="label"
        startIcon={<CloudUploadIcon />}
        sx={{ mb: 3, py: 2, px: 4, borderRadius: '16px' }}
      >
        Tirar Foto / Escolher
        <input
          type="file"
          accept="image/*"
          capture="environment"
          hidden
          onChange={handleFileChange}
        />
      </Button>

      {file && (
        <Typography variant="body2" sx={{ mb: 3 }}>
          Selecionado: {file.name}
        </Typography>
      )}

      <Button
        variant="contained"
        color="secondary"
        disabled={!file || uploading}
        onClick={handleUpload}
        sx={{ py: 1.5, px: 6, borderRadius: '16px' }}
      >
        {uploading ? <CircularProgress size={24} color="inherit" /> : 'Enviar'}
      </Button>
    </Box>
  );
}
