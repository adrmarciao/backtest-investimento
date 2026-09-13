import React, { useState, useEffect } from 'react';
import { getAssets, getIndicators, saveIndicators, updateIndicators, deleteIndicators } from '../services/api';

export default function IndicatorsTab() {
  const [assets, setAssets] = useState([]);
  const [selectedTicker, setSelectedTicker] = useState('');
  const [indicatorsList, setIndicatorsList] = useState([]);

  // Form states for new entry
  const [ano, setAno] = useState(new Date().getFullYear() - 1);
  const [pl, setPl] = useState('');
  const [pvp, setPvp] = useState('');
  const [dividaEbitda, setDividaEbitda] = useState('');
  const [roe, setRoe] = useState('');
  const [dpa, setDpa] = useState('');
  const [lpa, setLpa] = useState('');
  const [vpa, setVpa] = useState('');
  const [loading, setLoading] = useState(false);

  // States for inline editing
  const [editingYear, setEditingYear] = useState(null);
  const [editForm, setEditForm] = useState({
    pl: '',
    pvp: '',
    dividaEbitda: '',
    roe: '',
    dpa: '',
    lpa: '',
    vpa: '',
  });

  useEffect(() => {
    getAssets().then((res) => {
      setAssets(res.data);
      if (res.data.length > 0) {
        setSelectedTicker(res.data[0].ticker);
      }
    }).catch(console.error);
  }, []);

  const loadIndicators = async (ticker) => {
    if (!ticker) return;
    try {
      const res = await getIndicators(ticker);
      setIndicatorsList(res.data);
    } catch (err) {
      console.error(err);
    }
  };

  useEffect(() => {
    if (selectedTicker) {
      setEditingYear(null);
      loadIndicators(selectedTicker);
    }
  }, [selectedTicker]);

  const calculateBazinPreview = (dpaVal) => {
    const val = parseFloat(dpaVal);
    if (isNaN(val) || val <= 0) return 'N/A';
    return `R$ ${(val / 0.06).toFixed(2)}`;
  };

  const calculateGrahamPreview = (lpaVal, vpaVal) => {
    const l = parseFloat(lpaVal);
    const v = parseFloat(vpaVal);
    if (isNaN(l) || isNaN(v) || l <= 0 || v <= 0) return 'N/A';
    const res = Math.sqrt(22.5 * l * v);
    return `R$ ${res.toFixed(2)}`;
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!selectedTicker) return alert('Selecione um ativo');
    setLoading(true);
    try {
      await saveIndicators(selectedTicker, {
        ticker: selectedTicker,
        ano: parseInt(ano),
        pl: parseFloat(pl),
        pvp: parseFloat(pvp),
        dividaEbitda: parseFloat(dividaEbitda),
        roe: parseFloat(roe),
        dpa: parseFloat(dpa),
        lpa: parseFloat(lpa),
        vpa: parseFloat(vpa),
      });
      await loadIndicators(selectedTicker);
      // Reset form
      setPl(''); setPvp(''); setDividaEbitda(''); setRoe(''); setDpa(''); setLpa(''); setVpa('');
    } catch (err) {
      alert('Erro ao salvar indicadores do ano');
    } finally {
      setLoading(false);
    }
  };

  const handleDeleteYear = async (yearToDelete) => {
    if (!window.confirm(`Excluir indicadores do ano ${yearToDelete}?`)) return;
    try {
      await deleteIndicators(selectedTicker, yearToDelete);
      await loadIndicators(selectedTicker);
    } catch (err) {
      alert('Erro ao excluir indicadores');
    }
  };

  const handleStartEdit = (ind) => {
    setEditingYear(ind.ano);
    setEditForm({
      pl: ind.pl ?? '',
      pvp: ind.pvp ?? '',
      dividaEbitda: ind.dividaEbitda ?? '',
      roe: ind.roe ?? '',
      dpa: ind.dpa ?? '',
      lpa: ind.lpa ?? '',
      vpa: ind.vpa ?? '',
    });
  };

  const handleCancelEdit = () => {
    setEditingYear(null);
    setEditForm({ pl: '', pvp: '', dividaEbitda: '', roe: '', dpa: '', lpa: '', vpa: '' });
  };

  const handleEditInputChange = (field, value) => {
    setEditForm((prev) => ({
      ...prev,
      [field]: value,
    }));
  };

  const handleSaveEdit = async (year) => {
    if (!selectedTicker) return;
    try {
      const updatedData = {
        ticker: selectedTicker,
        ano: parseInt(year),
        pl: parseFloat(editForm.pl),
        pvp: parseFloat(editForm.pvp),
        dividaEbitda: parseFloat(editForm.dividaEbitda),
        roe: parseFloat(editForm.roe),
        dpa: parseFloat(editForm.dpa),
        lpa: parseFloat(editForm.lpa),
        vpa: parseFloat(editForm.vpa),
      };
      await updateIndicators(selectedTicker, year, updatedData);
      setEditingYear(null);
      await loadIndicators(selectedTicker);
    } catch (err) {
      alert('Erro ao atualizar indicadores do ano');
    }
  };

  return (
    <div className="glass-card">
      <h2 style={{ marginBottom: '16px' }}>Indicadores Fundamentalistas por Ano</h2>

      <div className="form-group" style={{ maxWidth: '300px', marginBottom: '24px' }}>
        <label>Selecione o Ativo</label>
        <select
          className="form-control"
          value={selectedTicker}
          onChange={(e) => setSelectedTicker(e.target.value)}
        >
          {assets.map((a) => (
            <option key={a.ticker} value={a.ticker}>
              {a.ticker} (Aporte R$ {a.valorAporte})
            </option>
          ))}
        </select>
      </div>

      {selectedTicker && (
        <>
          <form onSubmit={handleSubmit} style={{ background: 'var(--bg-glass)', padding: '16px', borderRadius: '12px', border: '1px solid var(--border-color)', marginBottom: '24px' }}>
            <h3 style={{ fontSize: '1rem', marginBottom: '12px' }}>Cadastrar/Editar Ano para {selectedTicker}</h3>
            
            <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(130px, 1fr))', gap: '12px' }}>
              <div className="form-group">
                <label>Ano</label>
                <input type="number" className="form-control" value={ano} onChange={(e) => setAno(e.target.value)} required />
              </div>
              <div className="form-group">
                <label>P/L</label>
                <input type="number" step="0.01" className="form-control" value={pl} onChange={(e) => setPl(e.target.value)} placeholder="12.5" required />
              </div>
              <div className="form-group">
                <label>P/VP</label>
                <input type="number" step="0.01" className="form-control" value={pvp} onChange={(e) => setPvp(e.target.value)} placeholder="2.1" required />
              </div>
              <div className="form-group">
                <label>Dív/EBITDA</label>
                <input type="number" step="0.01" className="form-control" value={dividaEbitda} onChange={(e) => setDividaEbitda(e.target.value)} placeholder="1.4" required />
              </div>
              <div className="form-group">
                <label>ROE (%)</label>
                <input type="number" step="0.01" className="form-control" value={roe} onChange={(e) => setRoe(e.target.value)} placeholder="22.0" required />
              </div>
              <div className="form-group">
                <label>DPA (Div/Ação)</label>
                <input type="number" step="0.01" className="form-control" value={dpa} onChange={(e) => setDpa(e.target.value)} placeholder="1.50" required />
              </div>
              <div className="form-group">
                <label>LPA (Lucro/Ação)</label>
                <input type="number" step="0.01" className="form-control" value={lpa} onChange={(e) => setLpa(e.target.value)} placeholder="3.20" required />
              </div>
              <div className="form-group">
                <label>VPA (Val.Patr/Ação)</label>
                <input type="number" step="0.01" className="form-control" value={vpa} onChange={(e) => setVpa(e.target.value)} placeholder="18.00" required />
              </div>
            </div>

            <div style={{ marginTop: '12px', display: 'flex', alignItems: 'center', justifyContent: 'space-between' }}>
              <div style={{ fontSize: '0.85rem', color: 'var(--text-secondary)' }}>
                <strong>Tetos Calculados:</strong> Bazin: <span style={{ color: 'var(--success)' }}>{calculateBazinPreview(dpa)}</span> | Graham: <span style={{ color: 'var(--success)' }}>{calculateGrahamPreview(lpa, vpa)}</span>
              </div>
              <button type="submit" className="btn btn-primary" disabled={loading}>
                {loading ? <div className="spinner" /> : 'Salvar Indicadores do Ano'}
              </button>
            </div>
          </form>

          <h3 style={{ marginBottom: '12px', fontSize: '1.1rem' }}>Histórico Fundamentalista Cadastrado</h3>
          <div className="custom-table-container">
            <table className="custom-table">
              <thead>
                <tr>
                  <th>Ano</th>
                  <th>P/L</th>
                  <th>P/VP</th>
                  <th>Dív/EBITDA</th>
                  <th>ROE</th>
                  <th>DPA</th>
                  <th>LPA</th>
                  <th>VPA</th>
                  <th>Teto Bazin</th>
                  <th>Teto Graham</th>
                  <th>Ações</th>
                </tr>
              </thead>
              <tbody>
                {indicatorsList.length === 0 ? (
                  <tr>
                    <td colSpan="11" style={{ textAlign: 'center', color: 'var(--text-muted)' }}>
                      Nenhum ano cadastrado para este ativo.
                    </td>
                  </tr>
                ) : (
                  indicatorsList.map((ind) => {
                    const isEditing = ind.ano === editingYear;
                    return (
                      <tr key={ind.ano}>
                        <td><strong>{ind.ano}</strong></td>
                        {isEditing ? (
                          <>
                            <td>
                              <input
                                type="number"
                                step="0.01"
                                className="form-control"
                                style={{ width: '70px', padding: '4px 6px' }}
                                value={editForm.pl}
                                onChange={(e) => handleEditInputChange('pl', e.target.value)}
                              />
                            </td>
                            <td>
                              <input
                                type="number"
                                step="0.01"
                                className="form-control"
                                style={{ width: '70px', padding: '4px 6px' }}
                                value={editForm.pvp}
                                onChange={(e) => handleEditInputChange('pvp', e.target.value)}
                              />
                            </td>
                            <td>
                              <input
                                type="number"
                                step="0.01"
                                className="form-control"
                                style={{ width: '70px', padding: '4px 6px' }}
                                value={editForm.dividaEbitda}
                                onChange={(e) => handleEditInputChange('dividaEbitda', e.target.value)}
                              />
                            </td>
                            <td>
                              <input
                                type="number"
                                step="0.01"
                                className="form-control"
                                style={{ width: '70px', padding: '4px 6px' }}
                                value={editForm.roe}
                                onChange={(e) => handleEditInputChange('roe', e.target.value)}
                              />
                            </td>
                            <td>
                              <input
                                type="number"
                                step="0.01"
                                className="form-control"
                                style={{ width: '70px', padding: '4px 6px' }}
                                value={editForm.dpa}
                                onChange={(e) => handleEditInputChange('dpa', e.target.value)}
                              />
                            </td>
                            <td>
                              <input
                                type="number"
                                step="0.01"
                                className="form-control"
                                style={{ width: '70px', padding: '4px 6px' }}
                                value={editForm.lpa}
                                onChange={(e) => handleEditInputChange('lpa', e.target.value)}
                              />
                            </td>
                            <td>
                              <input
                                type="number"
                                step="0.01"
                                className="form-control"
                                style={{ width: '70px', padding: '4px 6px' }}
                                value={editForm.vpa}
                                onChange={(e) => handleEditInputChange('vpa', e.target.value)}
                              />
                            </td>
                            <td><span className="badge badge-success">{calculateBazinPreview(editForm.dpa)}</span></td>
                            <td><span className="badge badge-success">{calculateGrahamPreview(editForm.lpa, editForm.vpa)}</span></td>
                            <td>
                              <div style={{ display: 'flex', gap: '6px' }}>
                                <button
                                  className="btn btn-primary"
                                  style={{ padding: '6px 10px', fontSize: '0.85rem' }}
                                  onClick={() => handleSaveEdit(ind.ano)}
                                  title="Salvar alterações"
                                >
                                  ✓
                                </button>
                                <button
                                  className="btn btn-secondary"
                                  style={{ padding: '6px 10px', fontSize: '0.85rem' }}
                                  onClick={handleCancelEdit}
                                  title="Cancelar"
                                >
                                  ✕
                                </button>
                              </div>
                            </td>
                          </>
                        ) : (
                          <>
                            <td>{ind.pl}</td>
                            <td>{ind.pvp}</td>
                            <td>{ind.dividaEbitda}</td>
                            <td>{ind.roe}%</td>
                            <td>R$ {ind.dpa}</td>
                            <td>R$ {ind.lpa}</td>
                            <td>R$ {ind.vpa}</td>
                            <td><span className="badge badge-success">{calculateBazinPreview(ind.dpa)}</span></td>
                            <td><span className="badge badge-success">{calculateGrahamPreview(ind.lpa, ind.vpa)}</span></td>
                            <td>
                              <div style={{ display: 'flex', gap: '6px' }}>
                                <button
                                  className="btn btn-secondary"
                                  style={{ padding: '6px 10px', fontSize: '0.85rem' }}
                                  onClick={() => handleStartEdit(ind)}
                                  title="Editar indicador"
                                >
                                  ✏️ Editar
                                </button>
                                <button
                                  className="btn btn-danger"
                                  style={{ padding: '6px 10px', fontSize: '0.85rem' }}
                                  onClick={() => handleDeleteYear(ind.ano)}
                                  title="Excluir ano"
                                >
                                  Excluir
                                </button>
                              </div>
                            </td>
                          </>
                        )}
                      </tr>
                    );
                  })
                )}
              </tbody>
            </table>
          </div>
        </>
      )}
    </div>
  );
}

