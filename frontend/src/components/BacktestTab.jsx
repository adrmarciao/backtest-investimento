import React, { useState, useEffect } from 'react';
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
import Autocomplete from '@mui/material/Autocomplete';
import Chip from '@mui/material/Chip';
import RocketLaunchIcon from '@mui/icons-material/RocketLaunch';
import DoneAllIcon from '@mui/icons-material/DoneAll';
import ClearIcon from '@mui/icons-material/Clear';
import { executeBacktest, getAssets } from '../services/api';
import MetricsPanel from './MetricsPanel';
import PortfolioChart from './PortfolioChart';
import PurchasesTable from './PurchasesTable';

export default function BacktestTab() {
  const fiveYearsAgo = new Date();
  fiveYearsAgo.setFullYear(fiveYearsAgo.getFullYear() - 5);

  const [inicio, setInicio] = useState(fiveYearsAgo.toISOString().slice(0, 10));
  const [fim, setFim] = useState(new Date().toISOString().slice(0, 10));
  const [availableAssets, setAvailableAssets] = useState([]);
  const [selectedTickers, setSelectedTickers] = useState([]);
  const [loadingAssets, setLoadingAssets] = useState(true);
  const [loading, setLoading] = useState(false);
  const [result, setResult] = useState(null);
  const [error, setError] = useState('');

  useEffect(() => {
    fetchAssets();
  }, []);

  const fetchAssets = async () => {
    setLoadingAssets(true);
    try {
      const res = await getAssets();
      const assets = res.data || [];
      setAvailableAssets(assets);
      const tickers = assets.map((a) => a.ticker);
      setSelectedTickers(tickers);
    } catch (err) {
      setError('Erro ao carregar lista de ativos cadastrados');
    } finally {
      setLoadingAssets(false);
    }
  };

  const handleSelectAll = () => {
    setSelectedTickers(availableAssets.map((a) => a.ticker));
  };

  const handleClearAll = () => {
    setSelectedTickers([]);
  };

  const handleRunBacktest = async (e) => {
    e.preventDefault();
    if (selectedTickers.length === 0) {
      setError('Selecione ao menos 1 ativo para realizar o backtest.');
      return;
    }

    setLoading(true);
    setError('');
    try {
      const res = await executeBacktest(inicio, fim, selectedTickers);
      setResult(res.data);
    } catch (err) {
      setError(err.response?.data || 'Erro ao executar o motor de backtest');
    } finally {
      setLoading(false);
    }
  };

  const allTickers = availableAssets.map((a) => a.ticker);

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

          {selectedTickers.length === 0 && !loadingAssets && availableAssets.length > 0 && (
            <Alert severity="warning" sx={{ mb: 2.5 }}>
              Selecione ao menos 1 ativo para poder iniciar a simulação de backtest.
            </Alert>
          )}

          <Box
            component="form"
            onSubmit={handleRunBacktest}
            sx={{
              display: 'flex',
              flexDirection: 'column',
              gap: 2.5,
            }}
          >
            {/* Seção de Seleção de Tickers */}
            <Box sx={{ display: 'flex', flexDirection: 'column', gap: 1 }}>
              <Box sx={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', flexWrap: 'wrap', gap: 1 }}>
                <Typography variant="subtitle2" sx={{ fontWeight: 600 }}>
                  Ativos Selecionados ({selectedTickers.length} de {availableAssets.length} ativos)
                </Typography>
                <Stack direction="row" spacing={1}>
                  <Button
                    size="small"
                    variant="outlined"
                    startIcon={<DoneAllIcon />}
                    onClick={handleSelectAll}
                    disabled={loadingAssets || selectedTickers.length === availableAssets.length}
                  >
                    Selecionar Todos
                  </Button>
                  <Button
                    size="small"
                    variant="outlined"
                    color="secondary"
                    startIcon={<ClearIcon />}
                    onClick={handleClearAll}
                    disabled={loadingAssets || selectedTickers.length === 0}
                  >
                    Limpar Seleção
                  </Button>
                </Stack>
              </Box>

              <Autocomplete
                multiple
                options={allTickers}
                value={selectedTickers}
                onChange={(event, newValue) => setSelectedTickers(newValue)}
                disabled={loadingAssets || loading}
                loading={loadingAssets}
                renderTags={(value, getTagProps) =>
                  value.map((option, index) => {
                    const { key, ...tagProps } = getTagProps({ index });
                    return (
                      <Chip
                        key={key}
                        label={option}
                        size="small"
                        color="primary"
                        variant="outlined"
                        {...tagProps}
                      />
                    );
                  })
                }
                renderInput={(params) => (
                  <TextField
                    {...params}
                    variant="outlined"
                    label="Tickers do Backtest"
                    placeholder={selectedTickers.length === 0 ? "Selecione os tickers..." : ""}
                    size="small"
                  />
                )}
              />
            </Box>

            {/* Datas e Submissão */}
            <Box
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
                disabled={loading || loadingAssets || selectedTickers.length === 0}
                startIcon={loading ? <CircularProgress size={18} color="inherit" /> : <RocketLaunchIcon />}
                sx={{ height: 40, px: 3 }}
              >
                {loading ? 'Simulando...' : 'Iniciar Motor de Backtest'}
              </Button>
            </Box>
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


