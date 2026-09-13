import React, { useState } from 'react';
import AssetsTab from './components/AssetsTab';
import CriteriaTab from './components/CriteriaTab';
import IndicatorsTab from './components/IndicatorsTab';
import BacktestTab from './components/BacktestTab';

export default function App() {
  const [activeTab, setActiveTab] = useState('backtest');

  return (
    <div className="app-container">
      <header style={{ marginBottom: '32px', textAlign: 'center' }}>
        <h1 className="gradient-text" style={{ fontSize: '2.5rem', marginBottom: '8px' }}>
          Value Investing Backtest System
        </h1>
        <p style={{ color: 'var(--text-secondary)', fontSize: '1.05rem', maxWidth: '700px', margin: '0 auto' }}>
          Simulador de acumulação de capital buy-only com aportes periódicos na B3, aplicando filtros de Décio Bazin e Benjamin Graham sobre fundamentos reais ano a ano.
        </p>
      </header>

      <nav className="tabs-nav" style={{ justifyContent: 'center' }}>
        <button
          className={`tab-button ${activeTab === 'assets' ? 'active' : ''}`}
          onClick={() => setActiveTab('assets')}
        >
          1. Cadastrar Ativos
        </button>
        <button
          className={`tab-button ${activeTab === 'criteria' ? 'active' : ''}`}
          onClick={() => setActiveTab('criteria')}
        >
          2. Estratégia de Compra
        </button>
        <button
          className={`tab-button ${activeTab === 'indicators' ? 'active' : ''}`}
          onClick={() => setActiveTab('indicators')}
        >
          3. Indicadores Anuais
        </button>
        <button
          className={`tab-button ${activeTab === 'backtest' ? 'active' : ''}`}
          onClick={() => setActiveTab('backtest')}
        >
          4. Motor de Backtest
        </button>
      </nav>

      <main>
        {activeTab === 'assets' && <AssetsTab />}
        {activeTab === 'criteria' && <CriteriaTab />}
        {activeTab === 'indicators' && <IndicatorsTab />}
        {activeTab === 'backtest' && <BacktestTab />}
      </main>
    </div>
  );
}
