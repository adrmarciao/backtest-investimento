import React from 'react';
import {
  ResponsiveContainer,
  LineChart,
  Line,
  XAxis,
  YAxis,
  Tooltip,
  Legend,
  CartesianGrid
} from 'recharts';

export default function PortfolioChart({ data }) {
  if (!data || data.length === 0) {
    return (
      <div style={{ textAlign: 'center', padding: '40px', color: 'var(--text-muted)' }}>
        Nenhum dado de série temporal para exibir no gráfico.
      </div>
    );
  }

  const CustomTooltip = ({ active, payload, label }) => {
    if (active && payload && payload.length) {
      const point = payload[0].payload;
      return (
        <div style={{
          background: 'rgba(15, 23, 42, 0.95)',
          border: '1px solid var(--border-color)',
          borderRadius: '8px',
          padding: '12px',
          boxShadow: '0 10px 25px rgba(0,0,0,0.5)',
          fontSize: '0.85rem'
        }}>
          <div style={{ fontWeight: 'bold', marginBottom: '6px', color: '#fff' }}>{label}</div>
          <div style={{ color: '#6366f1' }}>
            Portfólio: <strong>R$ {point.valorPatrimonio?.toLocaleString('pt-BR', { minimumFractionDigits: 2 })}</strong> (Base 100: {point.patrimonioNormalizado})
          </div>
          <div style={{ color: '#10b981' }}>
            Aportado Acumulado: <strong>R$ {point.valorInvestidoAcumulado?.toLocaleString('pt-BR', { minimumFractionDigits: 2 })}</strong>
          </div>
          <div style={{ color: '#ec4899' }}>
            IBOVESPA: <strong>{point.valorIbovespa} pts</strong> (Base 100: {point.ibovespaNormalizado})
          </div>
        </div>
      );
    }
    return null;
  };

  return (
    <div style={{ width: '100%', height: 420 }}>
      <ResponsiveContainer>
        <LineChart data={data} margin={{ top: 20, right: 30, left: 10, bottom: 10 }}>
          <CartesianGrid strokeDasharray="3 3" stroke="rgba(255,255,255,0.05)" />
          <XAxis dataKey="data" stroke="var(--text-muted)" fontSize={12} />
          <YAxis stroke="var(--text-muted)" fontSize={12} domain={['auto', 'auto']} />
          <Tooltip content={<CustomTooltip />} />
          <Legend wrapperStyle={{ paddingTop: '10px' }} />
          <Line
            type="monotone"
            dataKey="patrimonioNormalizado"
            name="Evolução Portfólio (Base 100)"
            stroke="#6366f1"
            strokeWidth={3}
            dot={false}
            activeDot={{ r: 6 }}
          />
          <Line
            type="monotone"
            dataKey="ibovespaNormalizado"
            name="IBOVESPA (Base 100)"
            stroke="#ec4899"
            strokeWidth={2}
            strokeDasharray="4 4"
            dot={false}
          />
        </LineChart>
      </ResponsiveContainer>
    </div>
  );
}
