import React from 'react';

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

  const formatCurrency = (val) => val != null ? `R$ ${val.toLocaleString('pt-BR', { minimumFractionDigits: 2 })}` : 'R$ 0,00';
  const formatPct = (val) => val != null ? `${val >= 0 ? '+' : ''}${val.toFixed(2)}%` : '0,00%';

  return (
    <div className="metrics-grid">
      <div className="metric-card">
        <div className="title">Retorno Total</div>
        <div className="value" style={{ color: retornoTotal >= 0 ? 'var(--success)' : 'var(--danger)' }}>
          {formatPct(retornoTotal)}
        </div>
        <div className="subtitle">Lucro/Prejuízo sobre aportes</div>
      </div>

      <div className="metric-card">
        <div className="title">CAGR (Retorno Anualizado)</div>
        <div className="value" style={{ color: cagr >= 0 ? 'var(--success)' : 'var(--danger)' }}>
          {formatPct(cagr)}
        </div>
        <div className="subtitle">Taxa composta de crescimento ao ano</div>
      </div>

      <div className="metric-card">
        <div className="title">Max Drawdown</div>
        <div className="value" style={{ color: 'var(--danger)' }}>
          -{maxDrawdown != null ? Math.abs(maxDrawdown).toFixed(2) : '0.00'}%
        </div>
        <div className="subtitle">Maior queda pico-a-fundo</div>
      </div>

      <div className="metric-card">
        <div className="title">Sharpe Ratio</div>
        <div className="value" style={{ color: sharpeRatio >= 1 ? 'var(--success)' : sharpeRatio >= 0 ? 'var(--warning)' : 'var(--danger)' }}>
          {sharpeRatio != null ? sharpeRatio.toFixed(2) : '0.00'}
        </div>
        <div className="subtitle">Retorno ajustado ao risco (vs Selic)</div>
      </div>

      <div className="metric-card">
        <div className="title">Alfa vs IBOVESPA</div>
        <div className="value" style={{ color: alfaIbov >= 0 ? 'var(--success)' : 'var(--danger)' }}>
          {formatPct(alfaIbov)}
        </div>
        <div className="subtitle">Desempenho relativo ao benchmark</div>
      </div>

      <div className="metric-card">
        <div className="title">Total Aportado</div>
        <div className="value" style={{ color: 'var(--text-primary)' }}>
          {formatCurrency(totalAportado)}
        </div>
        <div className="subtitle">Soma de todos os aportes executados</div>
      </div>

      <div className="metric-card">
        <div className="title">Patrimônio Final</div>
        <div className="value" style={{ color: 'var(--accent-primary)' }}>
          {formatCurrency(valorFinal)}
        </div>
        <div className="subtitle">Valor atual das cotas acumuladas</div>
      </div>

      <div className="metric-card">
        <div className="title">Dividendos Recebidos</div>
        <div className="value" style={{ color: 'var(--success)' }}>
          {formatCurrency(totalDividendosRecebidos)}
        </div>
        <div className="subtitle">Total de proventos creditados</div>
      </div>

      <div className="metric-card">
        <div className="title">Dividendos Reinvestidos</div>
        <div className="value" style={{ color: 'var(--accent-secondary)' }}>
          {formatCurrency(totalDividendosReinvestidos)}
        </div>
        <div className="subtitle">Valor convertido em novas cotas</div>
      </div>

      <div className="metric-card">
        <div className="title">Caixa de Dividendos</div>
        <div className="value" style={{ color: 'var(--warning)' }}>
          {formatCurrency(saldoCaixaDividendos)}
        </div>
        <div className="subtitle">Saldo retido aguardando oportunidade</div>
      </div>
    </div>
  );
}

