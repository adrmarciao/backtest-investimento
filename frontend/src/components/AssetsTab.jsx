import React, { useState, useEffect } from 'react';
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
    <div className="glass-card">
      <h2 style={{ marginBottom: '16px' }}>Cadastro de Ativos (B3)</h2>
      {error && <div className="badge badge-danger" style={{ marginBottom: '16px', display: 'block' }}>{error}</div>}

      <form onSubmit={handleSubmit} style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(200px, 1fr))', gap: '16px', marginBottom: '24px' }}>
        <div className="form-group">
          <label>Ticker (ex: WEGE3)</label>
          <input
            type="text"
            className="form-control"
            value={ticker}
            onChange={(e) => setTicker(e.target.value)}
            placeholder="PETR4, VALE3, WEGE3"
          />
        </div>

        <div className="form-group">
          <label>Valor do Aporte (R$)</label>
          <input
            type="number"
            step="0.01"
            className="form-control"
            value={valorAporte}
            onChange={(e) => setValorAporte(e.target.value)}
            placeholder="500.00"
          />
        </div>

        <div className="form-group">
          <label>Periodicidade de Aporte</label>
          <select
            className="form-control"
            value={periodicidade}
            onChange={(e) => setPeriodicidade(e.target.value)}
          >
            <option value="MENSAL">Mensal</option>
            <option value="SEMANAL">Semanal</option>
          </select>
        </div>

        <div className="form-group" style={{ justifyContent: 'flex-end' }}>
          <button type="submit" className="btn btn-primary" disabled={loading}>
            {loading ? <div className="spinner" /> : 'Cadastrar Ativo'}
          </button>
        </div>
      </form>

      <h3 style={{ marginBottom: '12px', fontSize: '1.1rem' }}>Ativos Cadastrados</h3>
      <div className="custom-table-container">
        <table className="custom-table">
          <thead>
            <tr>
              <th>Ticker</th>
              <th>Aporte por Período</th>
              <th>Periodicidade</th>
              <th>Ações</th>
            </tr>
          </thead>
          <tbody>
            {assets.length === 0 ? (
              <tr>
                <td colSpan="4" style={{ textAlign: 'center', color: 'var(--text-muted)' }}>
                  Nenhum ativo cadastrado. Cadastre o primeiro acima!
                </td>
              </tr>
            ) : (
              assets.map((asset) => (
                <tr key={asset.ticker}>
                  <td><strong>{asset.ticker}</strong></td>
                  <td>R$ {asset.valorAporte?.toFixed(2)}</td>
                  <td>
                    <span className="badge badge-success">{asset.periodicidade}</span>
                  </td>
                  <td>
                    <button className="btn btn-danger" onClick={() => handleDelete(asset.ticker)}>
                      Excluir
                    </button>
                  </td>
                </tr>
              ))
            )}
          </tbody>
        </table>
      </div>
    </div>
  );
}
