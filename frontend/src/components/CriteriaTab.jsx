import React, { useState, useEffect } from 'react';
import Box from '@mui/material/Box';
import Card from '@mui/material/Card';
import CardContent from '@mui/material/CardContent';
import Typography from '@mui/material/Typography';
import TextField from '@mui/material/TextField';
import Button from '@mui/material/Button';
import Alert from '@mui/material/Alert';
import CircularProgress from '@mui/material/CircularProgress';
import SaveIcon from '@mui/icons-material/Save';
import { getCriteria, saveCriteria } from '../services/api';

export default function CriteriaTab() {
  const [plMax, setPlMax] = useState('20');
  const [pvpMax, setPvpMax] = useState('5');
  const [dividaEbitdaMax, setDividaEbitdaMax] = useState('2.5');
  const [roeMin, setRoeMin] = useState('15');
  const [loading, setLoading] = useState(false);
  const [message, setMessage] = useState('');

  useEffect(() => {
    getCriteria()
      .then((res) => {
        if (res.data) {
          if (res.data.plMax) setPlMax(res.data.plMax.toString());
          if (res.data.pvpMax) setPvpMax(res.data.pvpMax.toString());
          if (res.data.dividaEbitdaMax) setDividaEbitdaMax(res.data.dividaEbitdaMax.toString());
          if (res.data.roeMin) setRoeMin(res.data.roeMin.toString());
        }
      })
      .catch(console.error);
  }, []);

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setMessage('');
    try {
      await saveCriteria({
        plMax: plMax ? parseFloat(plMax) : null,
        pvpMax: pvpMax ? parseFloat(pvpMax) : null,
        dividaEbitdaMax: dividaEbitdaMax ? parseFloat(dividaEbitdaMax) : null,
        roeMin: roeMin ? parseFloat(roeMin) : null,
      });
      setMessage('Critérios fixos de compra salvos com sucesso!');
    } catch (err) {
      alert('Erro ao salvar critérios');
    } finally {
      setLoading(false);
    }
  };

  return (
    <Card sx={{ p: 1 }}>
      <CardContent>
        <Typography variant="h5" component="h2" sx={{ fontWeight: 700, mb: 1 }}>
          Estratégia de Compra (Critérios Fixos)
        </Typography>
        <Typography variant="body2" sx={{ color: 'text.secondary', mb: 3 }}>
          Estes critérios qualitativos são verificados contra os indicadores fundamentalistas do ano corrente (Fase 1 do motor de backtest). Se o ano não passar nestes filtros, nenhuma compra será realizada naquele ano.
        </Typography>

        {message && (
          <Alert severity="success" sx={{ mb: 3 }}>
            {message}
          </Alert>
        )}

        <Box
          component="form"
          onSubmit={handleSubmit}
          sx={{
            display: 'grid',
            gridTemplateColumns: { xs: '1fr', sm: 'repeat(2, 1fr)', md: 'repeat(4, 1fr)' },
            gap: 2.5,
          }}
        >
          <TextField
            label="P/L Máximo (Preço / Lucro)"
            type="number"
            inputProps={{ step: '0.1' }}
            variant="outlined"
            size="small"
            value={plMax}
            onChange={(e) => setPlMax(e.target.value)}
            placeholder="Ex: 20.0"
          />

          <TextField
            label="P/VP Máximo (Preço / V.P.)"
            type="number"
            inputProps={{ step: '0.1' }}
            variant="outlined"
            size="small"
            value={pvpMax}
            onChange={(e) => setPvpMax(e.target.value)}
            placeholder="Ex: 5.0"
          />

          <TextField
            label="Dívida / EBITDA Máximo"
            type="number"
            inputProps={{ step: '0.1' }}
            variant="outlined"
            size="small"
            value={dividaEbitdaMax}
            onChange={(e) => setDividaEbitdaMax(e.target.value)}
            placeholder="Ex: 2.5"
          />

          <TextField
            label="ROE Mínimo (%)"
            type="number"
            inputProps={{ step: '0.1' }}
            variant="outlined"
            size="small"
            value={roeMin}
            onChange={(e) => setRoeMin(e.target.value)}
            placeholder="Ex: 15.0"
          />

          <Box sx={{ gridColumn: '1 / -1', mt: 1 }}>
            <Button
              type="submit"
              variant="contained"
              disabled={loading}
              startIcon={loading ? <CircularProgress size={18} color="inherit" /> : <SaveIcon />}
              sx={{ px: 3, height: 40 }}
            >
              {loading ? 'Salvando...' : 'Salvar Estratégia de Compra'}
            </Button>
          </Box>
        </Box>
      </CardContent>
    </Card>
  );
}

