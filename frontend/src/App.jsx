import React, { useState } from 'react';
import Sidebar from './components/Sidebar/Sidebar';
import AssetsTab from './components/AssetsTab';
import CriteriaTab from './components/CriteriaTab';
import IndicatorsTab from './components/IndicatorsTab';
import BacktestTab from './components/BacktestTab';

export default function App() {
  const [activeTab, setActiveTab] = useState('backtest');
  const [isMobileOpen, setIsMobileOpen] = useState(false);

  return (
    <div className="app-container">
      <Sidebar
        activeTab={activeTab}
        setActiveTab={setActiveTab}
        isMobileOpen={isMobileOpen}
        setIsMobileOpen={setIsMobileOpen}
      />

      <div className="main-wrapper">
        <header className="mobile-top-bar">
          <div className="sidebar-brand">
            <div className="brand-icon">
              <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2.5" strokeLinecap="round" strokeLinejoin="round">
                <line x1="12" y1="1" x2="12" y2="23" />
                <path d="M17 5H9.5a3.5 3.5 0 0 0 0 7h5a3.5 3.5 0 0 1 0 7H6" />
              </svg>
            </div>
            <span className="brand-title gradient-text" style={{ fontWeight: 700 }}>Value Investing</span>
          </div>
          <button
            className="hamburger-btn"
            onClick={() => setIsMobileOpen(!isMobileOpen)}
            aria-label="Abrir menu navigation"
          >
            <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2.5" strokeLinecap="round" strokeLinejoin="round">
              <line x1="3" y1="12" x2="21" y2="12" />
              <line x1="3" y1="6" x2="21" y2="6" />
              <line x1="3" y1="18" x2="21" y2="18" />
            </svg>
          </button>
        </header>

        <header className="app-header">
          <h1 className="gradient-text" style={{ fontSize: '1.875rem', fontWeight: 800, marginBottom: '6px' }}>
            Value Investing Backtest System
          </h1>
          <p style={{ color: 'var(--md-sys-color-on-surface-variant)', fontSize: '0.95rem', maxWidth: '800px' }}>
            Simulador de acumulação de capital buy-only com aportes periódicos na B3, aplicando filtros de Décio Bazin e Benjamin Graham sobre fundamentos reais ano a ano.
          </p>
        </header>

        <main className="main-content">
          {activeTab === 'assets' && <AssetsTab />}
          {activeTab === 'criteria' && <CriteriaTab />}
          {activeTab === 'indicators' && <IndicatorsTab />}
          {activeTab === 'backtest' && <BacktestTab />}
        </main>
      </div>
    </div>
  );
}
