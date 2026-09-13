import React, { useState } from 'react';
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
    <div>
      <div className="glass-card" style={{ marginBottom: '24px' }}>
        <h2 style={{ marginBottom: '16px' }}>Execução de Simulação de Backtest</h2>
        
        {error && <div className="badge badge-danger" style={{ marginBottom: '16px', display: 'block' }}>{error}</div>}

        <form onSubmit={handleRunBacktest} style={{ display: 'flex', alignItems: 'flex-end', gap: '20px', flexWrap: 'wrap' }}>
          <div className="form-group" style={{ marginBottom: 0 }}>
            <label>Data Inicial</label>
            <input
              type="date"
              className="form-control"
              value={inicio}
              onChange={(e) => setInicio(e.target.value)}
              required
            />
          </div>

          <div className="form-group" style={{ marginBottom: 0 }}>
            <label>Data Final</label>
            <input
              type="date"
              className="form-control"
              value={fim}
              onChange={(e) => setFim(e.target.value)}
              required
            />
          </div>

          <button type="submit" className="btn btn-primary" disabled={loading} style={{ height: '42px' }}>
            {loading ? <div className="spinner" /> : '🚀 Iniciar Motor de Backtest'}
          </button>
        </form>
      </div>

      {result && (
        <div style={{ display: 'flex', flexDirection: 'column', gap: '24px' }}>
          {result.anosIgnorados && result.anosIgnorados.length > 0 && (
            <div className="glass-card" style={{ borderLeft: '4px solid var(--warning)' }}>
              <h4 style={{ color: 'var(--warning)', marginBottom: '8px' }}>⚠️ Avisos de Anos Ignorados / Inelegíveis</h4>
              <ul style={{ paddingLeft: '20px', color: 'var(--text-secondary)', fontSize: '0.85rem' }}>
                {result.anosIgnorados.map((msg, idx) => (
                  <li key={idx}>{msg}</li>
                ))}
              </ul>
            </div>
          )}

          <div className="glass-card">
            <h3 style={{ marginBottom: '16px' }}>Painel de Métricas e Performance</h3>
            <MetricsPanel result={result} />
          </div>

          <div className="glass-card">
            <h3 style={{ marginBottom: '16px' }}>Evolução Patrimonial x IBOVESPA (Base 100)</h3>
            <PortfolioChart data={result.serieTemporal} />
          </div>

          <div className="glass-card">
            <PurchasesTable purchases={result.compras} timeSeries={result.serieTemporal} />
          </div>
        </div>
      )}
    </div>
  );
}
