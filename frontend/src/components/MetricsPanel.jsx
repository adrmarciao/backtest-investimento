import React from 'react';
import Box from '@mui/material/Box';
import Card from '@mui/material/Card';
import CardContent from '@mui/material/CardContent';
import Typography from '@mui/material/Typography';

export default function MetricsPanel({ result }) {
  if (!result) return null;

  const {
    retornoTotal,
    cagr,
    maxDrawdown,
    sharpeRatio,
    alfaIbov,
    totalAportado,
    valorFinal,
    totalDividendosRecebidos,
    totalDividendosReinvestidos,
    saldoCaixaDividendos,
  } = result;

  const formatCurrency = (val) =>
    val != null ? `R$ ${val.toLocaleString('pt-BR', { minimumFractionDigits: 2 })}` : 'R$ 0,00';
  const formatPct = (val) =>
    val != null ? `${val >= 0 ? '+' : ''}${val.toFixed(2)}%` : '0,00%';

  const metrics = [
    {
      title: 'Retorno Total',
      value: formatPct(retornoTotal),
      color: retornoTotal >= 0 ? 'success.main' : 'error.main',
      subtitle: 'Lucro/Prejuízo sobre aportes',
    },
    {
      title: 'CAGR (Retorno Anualizado)',
      value: formatPct(cagr),
      color: cagr >= 0 ? 'success.main' : 'error.main',
      subtitle: 'Taxa composta de crescimento ao ano',
    },
    {
      title: 'Max Drawdown',
      value: `-${maxDrawdown != null ? Math.abs(maxDrawdown).toFixed(2) : '0.00'}%`,
      color: 'error.main',
      subtitle: 'Maior queda pico-a-fundo',
    },
    {
      title: 'Sharpe Ratio',
      value: sharpeRatio != null ? sharpeRatio.toFixed(2) : '0.00',
      color: sharpeRatio >= 1 ? 'success.main' : sharpeRatio >= 0 ? 'warning.main' : 'error.main',
      subtitle: 'Retorno ajustado ao risco (vs Selic)',
    },
    {
      title: 'Alfa vs IBOVESPA',
      value: formatPct(alfaIbov),
      color: alfaIbov >= 0 ? 'success.main' : 'error.main',
      subtitle: 'Desempenho relativo ao benchmark',
    },
    {
      title: 'Total Aportado',
      value: formatCurrency(totalAportado),
      color: 'text.primary',
      subtitle: 'Soma de todos os aportes executados',
    },
    {
      title: 'Patrimônio Final',
      value: formatCurrency(valorFinal),
      color: 'primary.main',
      subtitle: 'Valor atual das cotas acumuladas',
    },
    {
      title: 'Dividendos Recebidos',
      value: formatCurrency(totalDividendosRecebidos),
      color: 'success.main',
      subtitle: 'Total de proventos creditados',
    },
    {
      title: 'Dividendos Reinvestidos',
      value: formatCurrency(totalDividendosReinvestidos),
      color: 'secondary.main',
      subtitle: 'Valor convertido em novas cotas',
    },
    {
      title: 'Caixa de Dividendos',
      value: formatCurrency(saldoCaixaDividendos),
      color: 'warning.main',
      subtitle: 'Saldo retido aguardando oportunidade',
    },
  ];

  return (
    <Box
      sx={{
        display: 'grid',
        gridTemplateColumns: {
          xs: '1fr',
          sm: 'repeat(2, 1fr)',
          md: 'repeat(3, 1fr)',
          lg: 'repeat(5, 1fr)',
        },
        gap: 2,
      }}
    >
      {metrics.map((m, idx) => (
        <Card
          key={idx}
          sx={{
            backgroundColor: '#1b202a',
            border: '1px solid #43474e',
            borderRadius: 3,
            transition: 'transform 0.2s ease, border-color 0.2s ease',
            '&:hover': {
              borderColor: '#8d9199',
              backgroundColor: '#262a35',
              transform: 'translateY(-2px)',
            },
          }}
        >
          <CardContent sx={{ p: 2.5, '&:last-child': { pb: 2.5 } }}>
            <Typography
              variant="caption"
              sx={{
                fontWeight: 700,
                color: 'text.secondary',
                textTransform: 'uppercase',
                letterSpacing: '0.05em',
                display: 'block',
                mb: 1,
              }}
            >
              {m.title}
            </Typography>
            <Typography
              variant="h5"
              sx={{
                fontWeight: 700,
                color: m.color,
                fontFamily: "'JetBrains Mono', 'Plus Jakarta Sans', monospace",
                mb: 0.5,
              }}
            >
              {m.value}
            </Typography>
            <Typography variant="caption" sx={{ color: 'text.secondary', display: 'block' }}>
              {m.subtitle}
            </Typography>
          </CardContent>
        </Card>
      ))}
    </Box>
  );
}


