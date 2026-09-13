import React, { useState, useEffect } from 'react';
import { getCriteria, saveCriteria } from '../services/api';

export default function CriteriaTab() {
  const [plMax, setPlMax] = useState('20');
  const [pvpMax, setPvpMax] = useState('5');
  const [dividaEbitdaMax, setDividaEbitdaMax] = useState('2.5');
  const [roeMin, setRoeMin] = useState('15');
  const [loading, setLoading] = useState(false);
  const [message, setMessage] = useState('');

  useEffect(() => {
    getCriteria().then((res) => {
      if (res.data) {
        if (res.data.plMax) setPlMax(res.data.plMax.toString());
        if (res.data.pvpMax) setPvpMax(res.data.pvpMax.toString());
        if (res.data.dividaEbitdaMax) setDividaEbitdaMax(res.data.dividaEbitdaMax.toString());
        if (res.data.roeMin) setRoeMin(res.data.roeMin.toString());
      }
    }).catch(console.error);
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
    <div className="glass-card">
      <h2 style={{ marginBottom: '8px' }}>Estratégia de Compra (Critérios Fixos)</h2>
      <p style={{ color: 'var(--text-secondary)', fontSize: '0.9rem', marginBottom: '24px' }}>
        Estes critérios qualitativos são verificados contra os indicadores fundamentalistas do ano corrente (Fase 1 do motor de backtest). Se o ano não passar nestes filtros, nenhuma compra será realizada naquele ano.
      </p>

      {message && <div className="badge badge-success" style={{ marginBottom: '16px', display: 'block' }}>{message}</div>}

      <form onSubmit={handleSubmit} style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(220px, 1fr))', gap: '20px' }}>
        <div className="form-group">
          <label>P/L Máximo (Preço / Lucro)</label>
          <input
            type="number"
            step="0.1"
            className="form-control"
            value={plMax}
            onChange={(e) => setPlMax(e.target.value)}
            placeholder="Ex: 20.0"
          />
        </div>

        <div className="form-group">
          <label>P/VP Máximo (Preço / Valor Patrimonial)</label>
          <input
            type="number"
            step="0.1"
            className="form-control"
            value={pvpMax}
            onChange={(e) => setPvpMax(e.target.value)}
            placeholder="Ex: 5.0"
          />
        </div>

        <div className="form-group">
          <label>Dívida / EBITDA Máximo</label>
          <input
            type="number"
            step="0.1"
            className="form-control"
            value={dividaEbitdaMax}
            onChange={(e) => setDividaEbitdaMax(e.target.value)}
            placeholder="Ex: 2.5"
          />
        </div>

        <div className="form-group">
          <label>ROE Mínimo (%)</label>
          <input
            type="number"
            step="0.1"
            className="form-control"
            value={roeMin}
            onChange={(e) => setRoeMin(e.target.value)}
            placeholder="Ex: 15.0"
          />
        </div>

        <div style={{ gridColumn: '1 / -1', marginTop: '12px' }}>
          <button type="submit" className="btn btn-primary" disabled={loading}>
            {loading ? <div className="spinner" /> : 'Salvar Estratégia de Compra'}
          </button>
        </div>
      </form>
    </div>
  );
}
