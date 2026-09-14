import React, { useState, useEffect } from 'react';
import Box from '@mui/material/Box';
import Card from '@mui/material/Card';
import CardContent from '@mui/material/CardContent';
import Typography from '@mui/material/Typography';
import TextField from '@mui/material/TextField';
import Button from '@mui/material/Button';
import FormControl from '@mui/material/FormControl';
import InputLabel from '@mui/material/InputLabel';
import Select from '@mui/material/Select';
import MenuItem from '@mui/material/MenuItem';
import Table from '@mui/material/Table';
import TableBody from '@mui/material/TableBody';
import TableCell from '@mui/material/TableCell';
import TableContainer from '@mui/material/TableContainer';
import TableHead from '@mui/material/TableHead';
import TableRow from '@mui/material/TableRow';
import Paper from '@mui/material/Paper';
import Chip from '@mui/material/Chip';
import Alert from '@mui/material/Alert';
import CircularProgress from '@mui/material/CircularProgress';
import AddCircleIcon from '@mui/icons-material/AddCircle';
import DeleteOutlineIcon from '@mui/icons-material/DeleteOutlined';
import { getAssets, saveAsset, deleteAsset } from '../services/api';

export default function AssetsTab({ onAssetUpdated }) {
  const [assets, setAssets] = useState([]);
  const [ticker, setTicker] = useState('');
  const [valorAporte, setValorAporte] = useState('');
  const [periodicidade, setPeriodicidade] = useState('MENSAL');
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');

  const loadAssets = async () => {
    try {
      const res = await getAssets();
      setAssets(res.data);
    } catch (err) {
      console.error(err);
    }
  };

  useEffect(() => {
    loadAssets();
  }, []);

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!ticker || !valorAporte) {
      setError('Preencha o ticker e o valor do aporte');
      return;
    }
    setError('');
    setLoading(true);
    try {
      await saveAsset({
        ticker: ticker.toUpperCase(),
        valorAporte: parseFloat(valorAporte),
        periodicidade,
      });
      setTicker('');
      setValorAporte('');
      await loadAssets();
      if (onAssetUpdated) onAssetUpdated();
    } catch (err) {
      setError(err.response?.data || 'Erro ao cadastrar ativo');
    } finally {
      setLoading(false);
    }
  };

  const handleDelete = async (tickerToDelete) => {
    if (!window.confirm(`Deseja excluir o ativo ${tickerToDelete}?`)) return;
    try {
      await deleteAsset(tickerToDelete);
      await loadAssets();
      if (onAssetUpdated) onAssetUpdated();
    } catch (err) {
      alert('Erro ao excluir ativo');
    }
  };

  return (
    <Card sx={{ p: 1 }}>
      <CardContent>
        <Typography variant="h5" component="h2" sx={{ fontWeight: 700, mb: 1 }}>
          Cadastro de Ativos (B3)
        </Typography>
        <Typography variant="body2" sx={{ color: 'text.secondary', mb: 3 }}>
          Configure o universo de ações acompanhadas na carteira e seus respectivos aportes planejados.
        </Typography>

        {error && (
          <Alert severity="error" sx={{ mb: 3 }}>
            {error}
          </Alert>
        )}

        <Box
          component="form"
          onSubmit={handleSubmit}
          sx={{
            display: 'grid',
            gridTemplateColumns: { xs: '1fr', sm: 'repeat(3, 1fr)', md: 'repeat(4, 1fr)' },
            gap: 2,
            mb: 4,
            alignItems: 'center',
          }}
        >
          <TextField
            label="Ticker (ex: WEGE3)"
            variant="outlined"
            size="small"
            value={ticker}
            onChange={(e) => setTicker(e.target.value)}
            placeholder="PETR4, VALE3, WEGE3"
            required
          />

          <TextField
            label="Valor do Aporte (R$)"
            type="number"
            inputProps={{ step: '0.01' }}
            variant="outlined"
            size="small"
            value={valorAporte}
            onChange={(e) => setValorAporte(e.target.value)}
            placeholder="500.00"
            required
          />

          <FormControl size="small" variant="outlined">
            <InputLabel id="periodicidade-label">Periodicidade</InputLabel>
            <Select
              labelId="periodicidade-label"
              label="Periodicidade"
              value={periodicidade}
              onChange={(e) => setPeriodicidade(e.target.value)}
            >
              <MenuItem value="MENSAL">Mensal</MenuItem>
              <MenuItem value="SEMANAL">Semanal</MenuItem>
            </Select>
          </FormControl>

          <Button
            type="submit"
            variant="contained"
            disabled={loading}
            startIcon={loading ? <CircularProgress size={18} color="inherit" /> : <AddCircleIcon />}
            sx={{ height: 40 }}
          >
            {loading ? 'Cadastrando...' : 'Cadastrar Ativo'}
          </Button>
        </Box>

        <Typography variant="h6" sx={{ fontWeight: 700, mb: 2 }}>
          Ativos Cadastrados
        </Typography>

        <TableContainer component={Paper} sx={{ borderRadius: 2, border: '1px solid #43474e' }}>
          <Table size="medium">
            <TableHead>
              <TableRow>
                <TableCell>Ticker</TableCell>
                <TableCell>Aporte por Período</TableCell>
                <TableCell>Periodicidade</TableCell>
                <TableCell align="right">Ações</TableCell>
              </TableRow>
            </TableHead>
            <TableBody>
              {assets.length === 0 ? (
                <TableRow>
                  <TableCell colSpan={4} align="center" sx={{ py: 4, color: 'text.secondary' }}>
                    Nenhum ativo cadastrado. Cadastre o primeiro acima!
                  </TableCell>
                </TableRow>
              ) : (
                assets.map((asset) => (
                  <TableRow key={asset.ticker} hover>
                    <TableCell sx={{ fontWeight: 700, color: 'primary.main' }}>
                      {asset.ticker}
                    </TableCell>
                    <TableCell>
                      R$ {asset.valorAporte?.toFixed(2)}
                    </TableCell>
                    <TableCell>
                      <Chip
                        label={asset.periodicidade}
                        color="success"
                        size="small"
                        sx={{ fontWeight: 600 }}
                      />
                    </TableCell>
                    <TableCell align="right">
                      <Button
                        variant="outlined"
                        color="error"
                        size="small"
                        startIcon={<DeleteOutlineIcon fontSize="small" />}
                        onClick={() => handleDelete(asset.ticker)}
                      >
                        Excluir
                      </Button>
                    </TableCell>
                  </TableRow>
                ))
              )}
            </TableBody>
          </Table>
        </TableContainer>
      </CardContent>
    </Card>
  );
}

