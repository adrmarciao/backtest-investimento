import React, { useState } from 'react';
import Box from '@mui/material/Box';
import AppBar from '@mui/material/AppBar';
import Toolbar from '@mui/material/Toolbar';
import Typography from '@mui/material/Typography';
import IconButton from '@mui/material/IconButton';
import MenuIcon from '@mui/icons-material/Menu';
import TrendingUpIcon from '@mui/icons-material/TrendingUp';
import Sidebar from './components/Sidebar/Sidebar';
import AssetsTab from './components/AssetsTab';
import CriteriaTab from './components/CriteriaTab';
import IndicatorsTab from './components/IndicatorsTab';
import BacktestTab from './components/BacktestTab';

export default function App() {
  const [activeTab, setActiveTab] = useState('backtest');
  const [isMobileOpen, setIsMobileOpen] = useState(false);

  return (
    <Box sx={{ display: 'flex', minHeight: '100vh', width: '100%' }}>
      <Sidebar
        activeTab={activeTab}
        setActiveTab={setActiveTab}
        isMobileOpen={isMobileOpen}
        setIsMobileOpen={setIsMobileOpen}
      />

      <Box
        component="div"
        sx={{
          flexGrow: 1,
          display: 'flex',
          flexDirection: 'column',
          minWidth: 0,
          overflowX: 'hidden',
          backgroundColor: '#0a0e17',
        }}
      >
        {/* Mobile Top Bar */}
        <AppBar
          position="static"
          sx={{
            display: { xs: 'block', md: 'none' },
            backgroundColor: '#0f141d',
            borderBottom: '1px solid #43474e',
            boxShadow: 'none',
          }}
        >
          <Toolbar sx={{ justifyContent: 'space-between' }}>
            <Box sx={{ display: 'flex', alignItems: 'center', gap: 1.5 }}>
              <Box
                sx={{
                  width: 32,
                  height: 32,
                  borderRadius: '50%',
                  background: 'linear-gradient(135deg, #6366f1, #8b5cf6)',
                  display: 'flex',
                  alignItems: 'center',
                  justifyContent: 'center',
                  color: '#fff',
                }}
              >
                <TrendingUpIcon sx={{ fontSize: 18 }} />
              </Box>
              <Typography
                variant="subtitle1"
                sx={{
                  fontWeight: 700,
                  background: 'linear-gradient(135deg, #6366f1 0%, #8b5cf6 50%, #ec4899 100%)',
                  WebkitBackgroundClip: 'text',
                  WebkitTextFillColor: 'transparent',
                }}
              >
                Value Investing
              </Typography>
            </Box>
            <IconButton
              edge="end"
              color="inherit"
              aria-label="menu"
              onClick={() => setIsMobileOpen(!isMobileOpen)}
            >
              <MenuIcon />
            </IconButton>
          </Toolbar>
        </AppBar>

        {/* Application Header */}
        <Box
          component="header"
          sx={{
            p: { xs: 2.5, md: 4 },
            pb: 2,
            borderBottom: '1px solid #43474e',
            backgroundColor: '#0f141d',
          }}
        >
          <Typography
            variant="h4"
            component="h1"
            sx={{
              fontWeight: 800,
              mb: 0.75,
              fontSize: { xs: '1.5rem', md: '1.875rem' },
              background: 'linear-gradient(135deg, #6366f1 0%, #8b5cf6 50%, #ec4899 100%)',
              WebkitBackgroundClip: 'text',
              WebkitTextFillColor: 'transparent',
            }}
          >
            Value Investing Backtest System
          </Typography>
          <Typography
            variant="body2"
            sx={{
              color: 'text.secondary',
              maxWidth: 850,
              lineHeight: 1.6,
            }}
          >
            Simulador de acumulação de capital buy-only com aportes periódicos na B3, aplicando filtros de Décio Bazin e Benjamin Graham sobre fundamentos reais ano a ano.
          </Typography>
        </Box>

        {/* Tab Contents */}
        <Box component="main" sx={{ flexGrow: 1, p: { xs: 2, md: 4 }, overflowY: 'auto' }}>
          {activeTab === 'assets' && <AssetsTab />}
          {activeTab === 'criteria' && <CriteriaTab />}
          {activeTab === 'indicators' && <IndicatorsTab />}
          {activeTab === 'backtest' && <BacktestTab />}
        </Box>
      </Box>
    </Box>
  );
}

