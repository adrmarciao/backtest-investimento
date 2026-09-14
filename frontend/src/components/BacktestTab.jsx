import React, { useState } from 'react';
import Box from '@mui/material/Box';
import Card from '@mui/material/Card';
import CardContent from '@mui/material/CardContent';
import Typography from '@mui/material/Typography';
import TextField from '@mui/material/TextField';
import Button from '@mui/material/Button';
import Alert from '@mui/material/Alert';
import AlertTitle from '@mui/material/AlertTitle';
import CircularProgress from '@mui/material/CircularProgress';
import Stack from '@mui/material/Stack';
import RocketLaunchIcon from '@mui/icons-material/RocketLaunch';
import { executeBacktest } from '../services/api';
import MetricsPanel from './MetricsPanel';
import PortfolioChart from './PortfolioChart';
import PurchasesTable from './PurchasesTable';

export default function BacktestTab() {
  const fiveYearsAgo = new Date();
  fiveYearsAgo.setFullYear(fiveYearsAgo.getFullYear() - 5);

  const [inicio, setInicio] = useState(fiveYearsAgo.toISOString().slice(0, 10));
  const [fim, setFim] = useState(new Date().toISOString().slice(0, 10));
  const [loading, setLoading] = useState(false);
  const [result, setResult] = useState(null);
  const [error, setError] = useState('');

  const handleRunBacktest = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError('');
    try {
      const res = await executeBacktest(inicio, fim);
      setResult(res.data);
    } catch (err) {
      setError(err.response?.data || 'Erro ao executar o motor de backtest');
    } finally {
      setLoading(false);
    }
  };

  return (
    <Box sx={{ display: 'flex', flexDirection: 'column', gap: 3 }}>
      <Card sx={{ p: 1 }}>
        <CardContent>
          <Typography variant="h5" component="h2" sx={{ fontWeight: 700, mb: 2 }}>
            Execução de Simulação de Backtest
          </Typography>

          {error && (
            <Alert severity="error" sx={{ mb: 2.5 }}>
              {error}
            </Alert>
          )}

          <Box
            component="form"
            onSubmit={handleRunBacktest}
            sx={{
              display: 'flex',
              alignItems: 'flex-end',
              gap: 2.5,
              flexWrap: 'wrap',
            }}
          >
            <TextField
              label="Data Inicial"
              type="date"
              value={inicio}
              onChange={(e) => setInicio(e.target.value)}
              slotProps={{ inputLabel: { shrink: true } }}
              required
              size="small"
              sx={{ minWidth: 180 }}
            />

            <TextField
              label="Data Final"
              type="date"
              value={fim}
              onChange={(e) => setFim(e.target.value)}
              slotProps={{ inputLabel: { shrink: true } }}
              required
              size="small"
              sx={{ minWidth: 180 }}
            />

            <Button
              type="submit"
              variant="contained"
              disabled={loading}
              startIcon={loading ? <CircularProgress size={18} color="inherit" /> : <RocketLaunchIcon />}
              sx={{ height: 40, px: 3 }}
            >
              {loading ? 'Simulando...' : 'Iniciar Motor de Backtest'}
            </Button>
          </Box>
        </CardContent>
      </Card>

      {result && (
        <Stack spacing={3}>
          {result.anosIgnorados && result.anosIgnorados.length > 0 && (
            <Alert severity="warning">
              <AlertTitle>Avisos de Anos Ignorados / Inelegíveis</AlertTitle>
              <ul style={{ margin: 0, paddingLeft: 20 }}>
                {result.anosIgnorados.map((msg, idx) => (
                  <li key={idx}>{msg}</li>
                ))}
              </ul>
            </Alert>
          )}

          <Card sx={{ p: 1 }}>
            <CardContent>
              <Typography variant="h6" sx={{ fontWeight: 700, mb: 2 }}>
                Painel de Métricas e Performance
              </Typography>
              <MetricsPanel result={result} />
            </CardContent>
          </Card>

          <Card sx={{ p: 1 }}>
            <CardContent>
              <Typography variant="h6" sx={{ fontWeight: 700, mb: 2 }}>
                Evolução Patrimonial x IBOVESPA (Base 100)
              </Typography>
              <PortfolioChart data={result.serieTemporal} />
            </CardContent>
          </Card>

          <Card sx={{ p: 1 }}>
            <CardContent>
              <PurchasesTable purchases={result.compras} timeSeries={result.serieTemporal} />
            </CardContent>
          </Card>
        </Stack>
      )}
    </Box>
  );
}

