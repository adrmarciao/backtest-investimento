import React, { useState, useEffect } from 'react';
import Box from '@mui/material/Box';
import Drawer from '@mui/material/Drawer';
import List from '@mui/material/List';
import ListItemButton from '@mui/material/ListItemButton';
import ListItemIcon from '@mui/material/ListItemIcon';
import ListItemText from '@mui/material/ListItemText';
import Collapse from '@mui/material/Collapse';
import Typography from '@mui/material/Typography';
import IconButton from '@mui/material/IconButton';
import Tooltip from '@mui/material/Tooltip';
import TrendingUpIcon from '@mui/icons-material/TrendingUp';
import ChevronLeftIcon from '@mui/icons-material/ChevronLeft';
import ChevronRightIcon from '@mui/icons-material/ChevronRight';
import ExpandLess from '@mui/icons-material/ExpandLess';
import ExpandMore from '@mui/icons-material/ExpandMore';
import PlayCircleOutlineIcon from '@mui/icons-material/PlayCircleOutlined';
import TuneIcon from '@mui/icons-material/Tune';
import AssessmentIcon from '@mui/icons-material/Assessment';
import AddCircleOutlineIcon from '@mui/icons-material/AddCircleOutlined';
import FiberManualRecordIcon from '@mui/icons-material/FiberManualRecord';

const EXPANDED_WIDTH = 260;
const COLLAPSED_WIDTH = 76;

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
      items: [
        {
          id: 'backtest',
          label: 'Motor de Backtest',
          icon: <PlayCircleOutlineIcon fontSize="small" />,
        },
        {
          id: 'criteria',
          label: 'Estratégia de Compra',
          icon: <TuneIcon fontSize="small" />,
        },
      ],
    },
    {
      id: 'market_data',
      title: 'Dados de Mercado',
      items: [
        {
          id: 'indicators',
          label: 'Indicadores Anuais',
          icon: <AssessmentIcon fontSize="small" />,
        },
        {
          id: 'assets',
          label: 'Cadastrar Ativos',
          icon: <AddCircleOutlineIcon fontSize="small" />,
        },
      ],
    },
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

  const drawerContent = (
    <Box
      sx={{
        height: '100%',
        display: 'flex',
        flexDirection: 'column',
        backgroundColor: '#0f141d',
        color: '#e0e2ed',
        borderRight: '1px solid #43474e',
      }}
    >
      {/* Header */}
      <Box
        sx={{
          display: 'flex',
          alignItems: 'center',
          justifyContent: isCollapsed ? 'center' : 'space-between',
          p: 2,
          borderBottom: '1px solid #43474e',
          minHeight: 64,
        }}
      >
        <Box sx={{ display: 'flex', alignItems: 'center', gap: 1.5, overflow: 'hidden' }}>
          <Box
            sx={{
              minWidth: 36,
              height: 36,
              borderRadius: '10px',
              background: 'linear-gradient(135deg, #6366f1, #8b5cf6)',
              display: 'flex',
              alignItems: 'center',
              justifyContent: 'center',
              color: '#ffffff',
            }}
          >
            <TrendingUpIcon fontSize="small" />
          </Box>
          {!isCollapsed && (
            <Box sx={{ overflow: 'hidden', whiteSpace: 'nowrap' }}>
              <Typography
                variant="subtitle2"
                sx={{
                  fontWeight: 800,
                  fontSize: '0.95rem',
                  background: 'linear-gradient(135deg, #6366f1 0%, #8b5cf6 50%, #ec4899 100%)',
                  WebkitBackgroundClip: 'text',
                  WebkitTextFillColor: 'transparent',
                }}
              >
                Value Investing
              </Typography>
              <Typography variant="caption" sx={{ color: 'text.secondary', display: 'block', mt: -0.5 }}>
                Backtest Engine
              </Typography>
            </Box>
          )}
        </Box>

        <IconButton
          onClick={toggleCollapse}
          size="small"
          sx={{
            display: { xs: 'none', md: 'flex' },
            color: 'text.secondary',
            '&:hover': { color: 'primary.main', backgroundColor: '#262a35' },
          }}
          title={isCollapsed ? 'Expandir menu' : 'Recolher menu'}
        >
          {isCollapsed ? <ChevronRightIcon fontSize="small" /> : <ChevronLeftIcon fontSize="small" />}
        </IconButton>
      </Box>

      {/* Navigation Groups */}
      <Box sx={{ flexGrow: 1, py: 1.5, overflowY: 'auto' }}>
        <List component="nav" disablePadding>
          {navCategories.map((category) => {
            const isOpen = openMenuId === category.id;
            const hasActiveChild = category.items.some(item => item.id === activeTab);

            return (
              <Box key={category.id} sx={{ mb: 1 }}>
                {!isCollapsed && (
                  <ListItemButton
                    onClick={() => handleToggleMenu(category.id)}
                    sx={{
                      px: 2,
                      py: 1,
                      color: hasActiveChild ? 'primary.main' : 'text.secondary',
                      '&:hover': { backgroundColor: '#171c26' },
                    }}
                  >
                    <ListItemText
                      primary={category.title}
                      primaryTypographyProps={{
                        fontSize: '0.75rem',
                        fontWeight: 700,
                        textTransform: 'uppercase',
                        letterSpacing: '0.05em',
                      }}
                    />
                    {isOpen ? <ExpandLess fontSize="small" /> : <ExpandMore fontSize="small" />}
                  </ListItemButton>
                )}

                {isCollapsed ? (
                  <List disablePadding>
                    {category.items.map((item) => {
                      const isActive = activeTab === item.id;
                      return (
                        <Tooltip key={item.id} title={item.label} placement="right">
                          <ListItemButton
                            onClick={() => handleSelectTab(item.id)}
                            sx={{
                              minHeight: 48,
                              justifyContent: 'center',
                              px: 2.5,
                              backgroundColor: isActive ? '#004785' : 'transparent',
                              color: isActive ? '#d4e3ff' : 'text.secondary',
                              '&:hover': {
                                backgroundColor: isActive ? '#004785' : '#262a35',
                                color: '#ffffff',
                              },
                            }}
                          >
                            <ListItemIcon
                              sx={{
                                minWidth: 0,
                                mr: 'auto',
                                ml: 'auto',
                                justifyContent: 'center',
                                color: 'inherit',
                              }}
                            >
                              {item.icon}
                            </ListItemIcon>
                          </ListItemButton>
                        </Tooltip>
                      );
                    })}
                  </List>
                ) : (
                  <Collapse in={isOpen} timeout="auto" unmountOnExit>
                    <List component="div" disablePadding>
                      {category.items.map((item) => {
                        const isActive = activeTab === item.id;
                        return (
                          <ListItemButton
                            key={item.id}
                            onClick={() => handleSelectTab(item.id)}
                            sx={{
                              pl: 3,
                              py: 1.2,
                              my: 0.25,
                              mx: 1,
                              borderRadius: '8px',
                              backgroundColor: isActive ? '#004785' : 'transparent',
                              color: isActive ? '#d4e3ff' : 'text.primary',
                              fontWeight: isActive ? 600 : 400,
                              '&:hover': {
                                backgroundColor: isActive ? '#004785' : '#171c26',
                              },
                            }}
                          >
                            <ListItemIcon sx={{ minWidth: 32, color: isActive ? '#d4e3ff' : 'text.secondary' }}>
                              {item.icon}
                            </ListItemIcon>
                            <ListItemText
                              primary={item.label}
                              primaryTypographyProps={{
                                fontSize: '0.875rem',
                                fontWeight: isActive ? 600 : 400,
                              }}
                            />
                          </ListItemButton>
                        );
                      })}
                    </List>
                  </Collapse>
                )}
              </Box>
            );
          })}
        </List>
      </Box>

      {/* Footer */}
      <Box
        sx={{
          p: 2,
          borderTop: '1px solid #43474e',
          display: 'flex',
          alignItems: 'center',
          gap: 1.5,
          justifyContent: isCollapsed ? 'center' : 'flex-start',
        }}
      >
        <FiberManualRecordIcon sx={{ fontSize: 10, color: 'success.main' }} />
        {!isCollapsed && (
          <Typography variant="caption" sx={{ color: 'text.secondary', fontWeight: 500 }}>
            B3 System Online
          </Typography>
        )}
      </Box>
    </Box>
  );

  return (
    <>
      {/* Mobile Drawer */}
      <Drawer
        variant="temporary"
        open={isMobileOpen}
        onClose={() => setIsMobileOpen(false)}
        ModalProps={{ keepMounted: true }}
        sx={{
          display: { xs: 'block', md: 'none' },
          '& .MuiDrawer-paper': { width: EXPANDED_WIDTH, boxSizing: 'border-box' },
        }}
      >
        {drawerContent}
      </Drawer>

      {/* Desktop Drawer */}
      <Drawer
        variant="permanent"
        sx={{
          display: { xs: 'none', md: 'block' },
          width: isCollapsed ? COLLAPSED_WIDTH : EXPANDED_WIDTH,
          flexShrink: 0,
          '& .MuiDrawer-paper': {
            width: isCollapsed ? COLLAPSED_WIDTH : EXPANDED_WIDTH,
            transition: 'width 0.2s cubic-bezier(0.4, 0, 0.2, 1)',
            overflowX: 'hidden',
            boxSizing: 'border-box',
          },
        }}
      >
        {drawerContent}
      </Drawer>
    </>
  );
}


