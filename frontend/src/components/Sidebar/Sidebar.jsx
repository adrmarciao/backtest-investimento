import React, { useState, useEffect } from 'react';
import './Sidebar.css';

export default function Sidebar({ activeTab, setActiveTab, isMobileOpen, setIsMobileOpen }) {
  const [isCollapsed, setIsCollapsed] = useState(() => {
    const saved = localStorage.getItem('sidebar_collapsed');
    return saved === 'true';
  });

  const toggleCollapse = () => {
    setIsCollapsed(prev => {
      const next = !prev;
      localStorage.setItem('sidebar_collapsed', String(next));
      return next;
    });
  };

  const navCategories = [
    {
      id: 'simulation',
      title: 'Simulador & Estratégia',
      icon: (
        <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
          <polygon points="12 2 2 7 12 12 22 7 12 2" />
          <polyline points="2 17 12 22 22 17" />
          <polyline points="2 12 12 17 22 12" />
        </svg>
      ),
      items: [
        {
          id: 'backtest',
          label: 'Motor de Backtest',
          tooltip: 'Motor de Backtest',
          icon: (
            <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
              <polyline points="22 12 18 12 15 21 9 3 6 12 2 12" />
            </svg>
          )
        },
        {
          id: 'criteria',
          label: 'Estratégia de Compra',
          tooltip: 'Estratégia de Compra',
          icon: (
            <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
              <line x1="4" y1="21" x2="4" y2="14" />
              <line x1="4" y1="10" x2="4" y2="3" />
              <line x1="12" y1="21" x2="12" y2="12" />
              <line x1="12" y1="8" x2="12" y2="3" />
              <line x1="20" y1="21" x2="20" y2="16" />
              <line x1="20" y1="12" x2="20" y2="3" />
              <line x1="1" y1="14" x2="7" y2="14" />
              <line x1="9" y1="8" x2="15" y2="8" />
              <line x1="17" y1="16" x2="23" y2="16" />
            </svg>
          )
        }
      ]
    },
    {
      id: 'market_data',
      title: 'Dados de Mercado',
      icon: (
        <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
          <ellipse cx="12" cy="5" rx="9" ry="3" />
          <path d="M21 12c0 1.66-4 3-9 3s-9-1.34-9-3" />
          <path d="M21 19c0 1.66-4 3-9 3s-9-1.34-9-3" />
        </svg>
      ),
      items: [
        {
          id: 'indicators',
          label: 'Indicadores Anuais',
          tooltip: 'Indicadores Anuais',
          icon: (
            <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
              <rect x="3" y="3" width="18" height="18" rx="2" ry="2" />
              <line x1="3" y1="9" x2="21" y2="9" />
              <line x1="3" y1="15" x2="21" y2="15" />
              <line x1="9" y1="3" x2="9" y2="21" />
              <line x1="15" y1="3" x2="15" y2="21" />
            </svg>
          )
        },
        {
          id: 'assets',
          label: 'Cadastrar Ativos',
          tooltip: 'Cadastrar Ativos',
          icon: (
            <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
              <path d="M12 2L2 7l10 5 10-5-10-5z" />
              <path d="M2 17l10 5 10-5" />
              <path d="M2 12l10 5 10-5" />
            </svg>
          )
        }
      ]
    }
  ];

  const getParentCategoryId = (tabId) => {
    const category = navCategories.find(cat => cat.items.some(item => item.id === tabId));
    return category ? category.id : 'simulation';
  };

  const [openMenuId, setOpenMenuId] = useState(() => getParentCategoryId(activeTab));

  useEffect(() => {
    const parentId = getParentCategoryId(activeTab);
    if (parentId && parentId !== openMenuId) {
      setOpenMenuId(parentId);
    }
  }, [activeTab]);

  const handleToggleMenu = (categoryId) => {
    setOpenMenuId(prev => (prev === categoryId ? null : categoryId));
  };

  const handleSelectTab = (tabId) => {
    setActiveTab(tabId);
    if (isMobileOpen && setIsMobileOpen) {
      setIsMobileOpen(false);
    }
  };

  return (
    <>
      {/* Mobile Drawer Overlay */}
      <div 
        className={`sidebar-overlay ${isMobileOpen ? 'active' : ''}`}
        onClick={() => setIsMobileOpen && setIsMobileOpen(false)}
      />

      <aside className={`sidebar ${isCollapsed ? 'collapsed' : ''} ${isMobileOpen ? 'mobile-open' : ''}`}>
        <div className="sidebar-header">
          <div className="sidebar-brand">
            <div className="brand-icon">
              <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2.5" strokeLinecap="round" strokeLinejoin="round">
                <line x1="12" y1="1" x2="12" y2="23" />
                <path d="M17 5H9.5a3.5 3.5 0 0 0 0 7h5a3.5 3.5 0 0 1 0 7H6" />
              </svg>
            </div>
            <div className="brand-title-wrapper">
              <span className="brand-title gradient-text">Value Investing</span>
              <span className="brand-subtitle">Backtest Engine</span>
            </div>
          </div>

          <button 
            className="collapse-btn" 
            onClick={toggleCollapse} 
            title={isCollapsed ? "Expandir menu" : "Recolher menu"}
            aria-label="Toggle Sidebar"
          >
            <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
              {isCollapsed ? (
                <polyline points="9 18 15 12 9 6" />
              ) : (
                <polyline points="15 18 9 12 15 6" />
              )}
            </svg>
          </button>
        </div>

        <nav className="sidebar-content">
          {navCategories.map((category) => {
            const isOpen = openMenuId === category.id;
            const hasActiveChild = category.items.some(item => item.id === activeTab);

            return (
              <div key={category.id} className={`nav-group ${isOpen ? 'expanded' : ''}`}>
                {isCollapsed ? (
                  <div className="nav-collapsed-wrapper">
                    <button 
                      className={`nav-collapsed-btn ${hasActiveChild ? 'active-parent' : ''}`}
                      onClick={() => handleToggleMenu(category.id)}
                      data-tooltip={category.title}
                      aria-label={category.title}
                    >
                      <span className="nav-group-icon">{category.icon}</span>
                    </button>

                    {/* Flyout Popover for Collapsed State */}
                    <div className="nav-flyout">
                      <div className="nav-flyout-header">{category.title}</div>
                      <div className="nav-flyout-items">
                        {category.items.map((item) => {
                          const isActive = activeTab === item.id;
                          return (
                            <button
                              key={item.id}
                              className={`nav-flyout-item ${isActive ? 'active' : ''}`}
                              onClick={() => handleSelectTab(item.id)}
                            >
                              <span className="nav-item-icon">{item.icon}</span>
                              <span className="nav-item-label">{item.label}</span>
                            </button>
                          );
                        })}
                      </div>
                    </div>
                  </div>
                ) : (
                  <>
                    <button
                      className={`nav-group-header-btn ${isOpen ? 'open' : ''} ${hasActiveChild ? 'active-parent' : ''}`}
                      onClick={() => handleToggleMenu(category.id)}
                      aria-expanded={isOpen}
                    >
                      <div className="nav-group-header-left">
                        <span className="nav-group-icon">{category.icon}</span>
                        <span className="nav-group-title">{category.title}</span>
                      </div>
                      <svg 
                        className={`chevron-icon ${isOpen ? 'open' : ''}`} 
                        width="14" 
                        height="14" 
                        viewBox="0 0 24 24" 
                        fill="none" 
                        stroke="currentColor" 
                        strokeWidth="2" 
                        strokeLinecap="round" 
                        strokeLinejoin="round"
                      >
                        <polyline points="6 9 12 15 18 9" />
                      </svg>
                    </button>

                    <div className={`nav-subitems-container ${isOpen ? 'open' : ''}`}>
                      <div className="nav-subitems-content">
                        {category.items.map((item) => {
                          const isActive = activeTab === item.id;
                          return (
                            <button
                              key={item.id}
                              className={`nav-subitem ${isActive ? 'active' : ''}`}
                              onClick={() => handleSelectTab(item.id)}
                            >
                              <span className="nav-item-icon">{item.icon}</span>
                              <span className="nav-item-label">{item.label}</span>
                            </button>
                          );
                        })}
                      </div>
                    </div>
                  </>
                )}
              </div>
            );
          })}
        </nav>

        <div className="sidebar-footer">
          <div className="status-indicator">
            <span className="status-dot"></span>
            <span className="sidebar-footer-text">B3 System Online</span>
          </div>
        </div>
      </aside>
    </>
  );
}

