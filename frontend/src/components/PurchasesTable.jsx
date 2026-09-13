import React, { useState } from 'react';

export default function PurchasesTable({ purchases, timeSeries }) {
  const [currentPage, setCurrentPage] = useState(1);
  const itemsPerPage = 20;

  if (!purchases || purchases.length === 0) {
    return (
      <div style={{ textAlign: 'center', padding: '30px', color: 'var(--text-muted)' }}>
        Nenhuma compra foi realizada no período analisado. Os critérios fixos ou preços teto não foram satisfeitos.
      </div>
    );
  }

  const totalPages = Math.ceil(purchases.length / itemsPerPage);
  const startIndex = (currentPage - 1) * itemsPerPage;
  const currentItems = purchases.slice(startIndex, startIndex + itemsPerPage);

  const exportToCSV = () => {
    let csv = 'Data,Ticker,Preco,ValorAportado,Cotas,TetoBazin,TetoGraham,Tipo\n';
    purchases.forEach((p) => {
      const tipo = p.isReinvestimento ? 'Reinvestimento' : 'Aporte';
      csv += `${p.data},${p.ticker},${p.preco},${p.valorAportado},${p.cotas},${p.tetoBazin},${p.tetoGraham},${tipo}\n`;
    });

    const blob = new Blob([csv], { type: 'text/csv;charset=utf-8;' });
    const url = URL.createObjectURL(blob);
    const link = document.createElement('a');
    link.href = url;
    link.setAttribute('download', `backtest_compras_${new Date().toISOString().slice(0, 10)}.csv`);
    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);
  };

  return (
    <div>
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '16px' }}>
        <h3 style={{ fontSize: '1.1rem' }}>Histórico de Compras Executadas ({purchases.length} compras)</h3>
        <button className="btn btn-secondary" onClick={exportToCSV}>
          📥 Exportar CSV
        </button>
      </div>

      <div className="custom-table-container">
        <table className="custom-table">
          <thead>
            <tr>
              <th>Data</th>
              <th>Ticker</th>
              <th>Tipo</th>
              <th>Preço de Compra</th>
              <th>Aporte / Reinvestimento</th>
              <th>Cotas Adquiridas</th>
              <th>Teto Bazin do Ano</th>
              <th>Teto Graham do Ano</th>
            </tr>
          </thead>
          <tbody>
            {currentItems.map((p, index) => (
              <tr key={index}>
                <td>{p.data}</td>
                <td><strong>{p.ticker}</strong></td>
                <td>
                  {p.isReinvestimento ? (
                    <span className="badge badge-warning">💰 Reinvestimento</span>
                  ) : (
                    <span className="badge" style={{ background: 'rgba(99, 102, 241, 0.2)', color: 'var(--accent-primary)' }}>💵 Aporte</span>
                  )}
                </td>
                <td>R$ {p.preco?.toFixed(2)}</td>
                <td>R$ {p.valorAportado?.toFixed(2)}</td>
                <td>{p.cotas?.toFixed(4)}</td>
                <td><span className="badge badge-success">R$ {p.tetoBazin?.toFixed(2)}</span></td>
                <td><span className="badge badge-success">R$ {p.tetoGraham?.toFixed(2)}</span></td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>

      {totalPages > 1 && (
        <div className="pagination">
          <span style={{ fontSize: '0.85rem', color: 'var(--text-secondary)' }}>
            Página {currentPage} de {totalPages}
          </span>
          <div style={{ display: 'flex', gap: '8px' }}>
            <button
              className="btn btn-secondary"
              disabled={currentPage === 1}
              onClick={() => setCurrentPage((prev) => Math.max(prev - 1, 1))}
            >
              Anterior
            </button>
            <button
              className="btn btn-secondary"
              disabled={currentPage === totalPages}
              onClick={() => setCurrentPage((prev) => Math.min(prev + 1, totalPages))}
            >
              Próxima
            </button>
          </div>
        </div>
      )}
    </div>
  );
}

