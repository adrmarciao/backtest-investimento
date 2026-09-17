import React, { useState, useEffect, useMemo } from 'react';
import Box from '@mui/material/Box';
import Card from '@mui/material/Card';
import CardContent from '@mui/material/CardContent';
import Typography from '@mui/material/Typography';
import TextField from '@mui/material/TextField';
import Button from '@mui/material/Button';
import IconButton from '@mui/material/IconButton';
import ToggleButton from '@mui/material/ToggleButton';
import ToggleButtonGroup from '@mui/material/ToggleButtonGroup';
import Table from '@mui/material/Table';
import TableBody from '@mui/material/TableBody';
import TableCell from '@mui/material/TableCell';
import TableContainer from '@mui/material/TableContainer';
import TableHead from '@mui/material/TableHead';
import TableRow from '@mui/material/TableRow';
import Paper from '@mui/material/Paper';
import Chip from '@mui/material/Chip';
import Switch from '@mui/material/Switch';
import Tooltip from '@mui/material/Tooltip';
import Alert from '@mui/material/Alert';
import Snackbar from '@mui/material/Snackbar';
import CircularProgress from '@mui/material/CircularProgress';
import Dialog from '@mui/material/Dialog';
import DialogTitle from '@mui/material/DialogTitle';
import DialogContent from '@mui/material/DialogContent';
import DialogActions from '@mui/material/DialogActions';
import LinearProgress from '@mui/material/LinearProgress';
import Divider from '@mui/material/Divider';

// Ícones
import AccountBalanceWalletOutlinedIcon from '@mui/icons-material/AccountBalanceWalletOutlined';
import TrendingDownIcon from '@mui/icons-material/TrendingDown';
import AddCircleOutlineIcon from '@mui/icons-material/AddCircleOutlined';
import BoltIcon from '@mui/icons-material/Bolt';
import RefreshIcon from '@mui/icons-material/Refresh';
import SaveIcon from '@mui/icons-material/Save';
import DeleteOutlineIcon from '@mui/icons-material/DeleteOutlined';
import ContentCopyIcon from '@mui/icons-material/ContentCopy';
import SettingsOutlinedIcon from '@mui/icons-material/SettingsOutlined';
import AddIcon from '@mui/icons-material/Add';
import RemoveIcon from '@mui/icons-material/Remove';
import CheckCircleOutlineIcon from '@mui/icons-material/CheckCircleOutlined';

import purchasePlanService from '../services/purchasePlanService';

export default function PurchasePlanTab() {
  // Estado de Configuração
  const [config, setConfig] = useState({
    saldoTotal: 60000,
    parcelasMeses: 12,
    periodicidade: 'SEMANAL',
    aporteRodadaManual: null,
    benchmark: 'IBOV',
    altaAno: 138000,
    fiboUp: 138000,
    fiboDown: 118000,
    tokenBrapi: '',
  });

  // Estado de Benchmark / Macro
  const [benchmarkStatus, setBenchmarkStatus] = useState(null);
  const [loadingBenchmark, setLoadingBenchmark] = useState(false);

  // Estado dos Ativos
  const [assets, setAssets] = useState([]);
  const [loading, setLoading] = useState(true);
  const [saving, setSaving] = useState(false);
  const [syncingQuotes, setSyncingQuotes] = useState(false);
  const [syncingBrapi, setSyncingBrapi] = useState(false);

  // Notificações
  const [toast, setToast] = useState({ open: false, message: '', severity: 'info' });

  // Diálogos Modais
  const [openAddDialog, setOpenAddDialog] = useState(false);
  const [newAsset, setNewAsset] = useState({
    ticker: '',
    setor: '',
    peso: 1.0,
    dpaForecast: '',
    precoAtual: '',
    lpa: '',
    vpa: '',
  });

  const [openBrapiDialog, setOpenBrapiDialog] = useState(false);
  const [brapiTokenInput, setBrapiTokenInput] = useState('');

  const [openSettingsDialog, setOpenSettingsDialog] = useState(false);
  const [tempSettings, setTempSettings] = useState({
    altaAno: '',
    fiboUp: '',
    fiboDown: '',
  });

  // Filtro de Ativos Habilitados na Tabela
  const [filterOnlyEnabled, setFilterOnlyEnabled] = useState(false);

  // Carregamento Inicial
  useEffect(() => {
    loadAllData();
  }, []);

  const showToast = (message, severity = 'info') => {
    setToast({ open: true, message, severity });
  };

  const loadAllData = async () => {
    setLoading(true);
    try {
      // 1. Carregar Config
      const configRes = await purchasePlanService.getConfig();
      const loadedConfig = configRes.data || {};
      setConfig(prev => ({
        ...prev,
        ...loadedConfig,
        saldoTotal: Number(loadedConfig.saldoTotal) || 0,
        parcelasMeses: Number(loadedConfig.parcelasMeses) || 12,
        periodicidade: loadedConfig.periodicidade || 'SEMANAL',
        aporteRodadaManual: loadedConfig.aporteRodadaManual != null ? Number(loadedConfig.aporteRodadaManual) : null,
      }));
      setBrapiTokenInput(loadedConfig.tokenBrapi || '');

      // 2. Carregar Ativos
      const assetsRes = await purchasePlanService.getAssets();
      const rawAssets = assetsRes.data || [];
      setAssets(normalizeAssets(rawAssets));

      // 3. Carregar Status do Benchmark
      fetchBenchmark(loadedConfig.benchmark || 'IBOV');

      // 4. Auto-sincronizar cotações do Google Finance na inicialização
      if (rawAssets.length > 0) {
        handleAutoSyncQuotes();
      }
    } catch (err) {
      console.error(err);
      showToast('Erro ao carregar dados do Plano de Compras', 'error');
    } finally {
      setLoading(false);
    }
  };

  const normalizeAssets = (list) => {
    return list.map(item => ({
      ...item,
      precoAtual: item.precoAtual != null ? Number(item.precoAtual) : null,
      dpaForecast: item.dpaForecast != null ? Number(item.dpaForecast) : null,
      lpa: item.lpa != null ? Number(item.lpa) : null,
      vpa: item.vpa != null ? Number(item.vpa) : null,
      peso: item.peso != null ? Number(item.peso) : 1.0,
      habilitado: item.habilitado != null ? Number(item.habilitado) : 1.0,
      ajusteManualQtd: Number(item.ajusteManualQtd) || 0,
    }));
  };

  const fetchBenchmark = async (benchmarkName) => {
    setLoadingBenchmark(true);
    try {
      const res = await purchasePlanService.getBenchmarkStatus(benchmarkName);
      setBenchmarkStatus(res.data);
    } catch (err) {
      console.warn('Não foi possível obter status do benchmark:', err);
    } finally {
      setLoadingBenchmark(false);
    }
  };

  const handleAutoSyncQuotes = async () => {
    try {
      const res = await purchasePlanService.syncQuotes();
      if (res.data && res.data.length > 0) {
        setAssets(normalizeAssets(res.data));
      }
    } catch (err) {
      console.warn('Falha na atualização automática de cotações:', err);
    }
  };

  // Cálculo Dinâmico do Aporte da Rodada
  const aporteCalculado = useMemo(() => {
    const saldo = Number(config.saldoTotal) || 0;
    const parcelas = Number(config.parcelasMeses) || 12;
    if (saldo <= 0 || parcelas <= 0) return 0;
    if (config.periodicidade === 'MENSAL') {
      return saldo / parcelas;
    }
    // Semanal = Saldo / (Parcelas * 4)
    return saldo / (parcelas * 4);
  }, [config.saldoTotal, config.parcelasMeses, config.periodicidade]);

  const aporteAtivo = useMemo(() => {
    if (config.aporteRodadaManual != null && Number(config.aporteRodadaManual) > 0) {
      return Number(config.aporteRodadaManual);
    }
    return aporteCalculado;
  }, [config.aporteRodadaManual, aporteCalculado]);

  // Cálculos de Rateio e Valuations na Tabela
  const { calculatedItems, roundTotals } = useMemo(() => {
    // 1. Soma dos pesos efetivos (apenas ativos habilitados)
    const somaPesos = assets.reduce((acc, curr) => {
      const habilitado = curr.habilitado != null ? Number(curr.habilitado) : 1.0;
      const isHab = habilitado > 0;
      const preco = Number(curr.precoAtual) || 0;
      const peso = Number(curr.peso) || 0;
      if (isHab && preco > 0 && peso > 0) {
        return acc + (peso * habilitado);
      }
      return acc;
    }, 0);

    let totalGasto = 0;
    let totalAcoes = 0;
    let totalHabilitados = 0;

    const items = assets.map((asset) => {
      const preco = Number(asset.precoAtual) || null;
      const dpa = Number(asset.dpaForecast) || null;
      const lpa = Number(asset.lpa) || null;
      const vpa = Number(asset.vpa) || null;
      const peso = Number(asset.peso) || 1.0;
      const habilitado = asset.habilitado != null ? Number(asset.habilitado) : 1.0;
      const isHab = habilitado > 0;
      const ajuste = isHab ? (Number(asset.ajusteManualQtd) || 0) : 0;

      if (isHab) totalHabilitados++;

      // Teto Bazin: DPA / 0.06
      const tetoBazin = dpa && dpa > 0 ? Number((dpa / 0.06).toFixed(2)) : null;

      // Preço Graham: sqrt(22.5 * LPA * VPA)
      let precoGraham = null;
      if (lpa && vpa && lpa > 0 && vpa > 0) {
        precoGraham = Number(Math.sqrt(22.5 * lpa * vpa).toFixed(2));
      }

      // Margem Bazin: (Teto - Preco) / Preco * 100
      let margemBazin = null;
      if (preco && preco > 0 && tetoBazin != null) {
        margemBazin = Number((((tetoBazin - preco) / preco) * 100).toFixed(2));
      }

      // Rateio
      const pesoEfetivo = isHab ? peso * habilitado : 0;
      let pctAlocado = 0;
      let valorAlocado = 0;
      let qtdSugerida = 0;

      if (isHab && preco && preco > 0 && somaPesos > 0 && pesoEfetivo > 0) {
        pctAlocado = pesoEfetivo / somaPesos;
        valorAlocado = aporteAtivo * pctAlocado;
        qtdSugerida = Math.floor(valorAlocado / preco);
      }

      const qtdFinal = isHab ? Math.max(0, qtdSugerida + ajuste) : 0;
      const totalItem = isHab && preco && preco > 0 ? Number((qtdFinal * preco).toFixed(2)) : 0;

      totalGasto += totalItem;
      totalAcoes += qtdFinal;

      return {
        ...asset,
        tetoBazin,
        precoGraham,
        margemBazin,
        pesoEfetivo,
        pctAlocado: Number((pctAlocado * 100).toFixed(2)),
        valorAlocado: Number(valorAlocado.toFixed(2)),
        qtdSugerida,
        qtdFinal,
        totalItem,
      };
    });

    const sobraCaixa = Number((aporteAtivo - totalGasto).toFixed(2));

    return {
      calculatedItems: items,
      roundTotals: {
        totalGasto: Number(totalGasto.toFixed(2)),
        sobraCaixa,
        totalAcoes,
        totalHabilitados,
        somaPesos: Number(somaPesos.toFixed(2)),
      },
    };
  }, [assets, aporteAtivo]);

  // Ativos filtrados para exibição na tabela
  const displayedItems = useMemo(() => {
    if (!filterOnlyEnabled) return calculatedItems;
    return calculatedItems.filter(item => {
      const hab = item.habilitado != null ? Number(item.habilitado) : 1.0;
      return hab > 0;
    });
  }, [calculatedItems, filterOnlyEnabled]);

  // Handlers de Edição na Tabela
  const handleAssetFieldChange = (ticker, field, value) => {
    setAssets(prev =>
      prev.map(item => {
        if (item.ticker === ticker) {
          return { ...item, [field]: value };
        }
        return item;
      })
    );
  };

  const handleToggleHabilitado = (ticker) => {
    setAssets(prev =>
      prev.map(item => {
        if (item.ticker === ticker) {
          const current = item.habilitado != null ? Number(item.habilitado) : 1.0;
          return { ...item, habilitado: current > 0 ? 0.0 : 1.0 };
        }
        return item;
      })
    );
  };

  const handleAjusteManualChange = (ticker, delta) => {
    setAssets(prev =>
      prev.map(item => {
        if (item.ticker === ticker) {
          const current = Number(item.ajusteManualQtd) || 0;
          return { ...item, ajusteManualQtd: current + delta };
        }
        return item;
      })
    );
  };

  // Salvar Alterações Gerais (Config e Ativos)
  const handleSaveAll = async () => {
    setSaving(true);
    try {
      // 1. Salvar Config
      await purchasePlanService.saveConfig({
        ...config,
        saldoTotal: Number(config.saldoTotal) || 0,
        parcelasMeses: Number(config.parcelasMeses) || 12,
        aporteRodadaManual: config.aporteRodadaManual ? Number(config.aporteRodadaManual) : null,
      });

      // 2. Salvar Ativos
      const payloadAssets = assets.map((a, index) => ({
        ...a,
        ordem: index,
        peso: Number(a.peso) || 1.0,
        habilitado: a.habilitado != null ? Number(a.habilitado) : 1.0,
        ajusteManualQtd: Number(a.ajusteManualQtd) || 0,
        precoAtual: a.precoAtual !== null && a.precoAtual !== '' ? Number(a.precoAtual) : null,
        dpaForecast: a.dpaForecast !== null && a.dpaForecast !== '' ? Number(a.dpaForecast) : null,
        lpa: a.lpa !== null && a.lpa !== '' ? Number(a.lpa) : null,
        vpa: a.vpa !== null && a.vpa !== '' ? Number(a.vpa) : null,
      }));

      const res = await purchasePlanService.saveAllAssets(payloadAssets);
      setAssets(normalizeAssets(res.data));
      showToast('Configurações e ativos salvos com sucesso!', 'success');
    } catch (err) {
      console.error(err);
      showToast('Erro ao salvar alterações no banco de dados', 'error');
    } finally {
      setSaving(false);
    }
  };

  // Atalho Enter nas células da tabela
  const handleKeyDown = (e) => {
    if (e.key === 'Enter') {
      e.target.blur();
      handleSaveAll();
    }
  };

  // Sincronizar Cotações Google Finance
  const handleSyncQuotes = async () => {
    setSyncingQuotes(true);
    try {
      const res = await purchasePlanService.syncQuotes();
      setAssets(normalizeAssets(res.data));
      if (config.benchmark) {
        fetchBenchmark(config.benchmark);
      }
      showToast('Cotações atualizadas com sucesso via Google Finance!', 'success');
    } catch (err) {
      console.error(err);
      showToast('Falha ao atualizar cotações no Google Finance', 'error');
    } finally {
      setSyncingQuotes(false);
    }
  };

  // Sincronizar Brapi
  const handleOpenBrapiSync = () => {
    if (!config.tokenBrapi && !brapiTokenInput) {
      setOpenBrapiDialog(true);
    } else {
      executeBrapiSync(config.tokenBrapi || brapiTokenInput);
    }
  };

  const executeBrapiSync = async (token) => {
    setSyncingBrapi(true);
    try {
      const res = await purchasePlanService.syncFundamentals(token);
      setAssets(normalizeAssets(res.data));
      // Salvar token na config se ainda não estiver salvo
      if (token && token !== config.tokenBrapi) {
        setConfig(prev => ({ ...prev, tokenBrapi: token }));
        await purchasePlanService.saveConfig({ ...config, tokenBrapi: token });
      }
      setOpenBrapiDialog(false);
      showToast('Fundamentos (LPA e VPA) atualizados com sucesso via Brapi!', 'success');
    } catch (err) {
      console.error(err);
      showToast(err.response?.data || 'Erro ao sincronizar fundamentos na Brapi. Verifique o token.', 'error');
    } finally {
      setSyncingBrapi(false);
    }
  };

  // Excluir Ativo
  const handleDeleteAsset = async (ticker) => {
    if (!window.confirm(`Confirma a exclusão do ativo ${ticker} do Plano de Compras?`)) return;
    try {
      await purchasePlanService.deleteAsset(ticker);
      setAssets(prev => prev.filter(item => item.ticker !== ticker));
      showToast(`Ativo ${ticker} excluído com sucesso.`, 'info');
    } catch (err) {
      console.error(err);
      showToast(`Erro ao excluir ativo ${ticker}`, 'error');
    }
  };

  // Adicionar Novo Ativo
  const handleCreateAsset = async (e) => {
    e.preventDefault();
    if (!newAsset.ticker) {
      showToast('Informe o ticker do ativo', 'warning');
      return;
    }

    try {
      const payload = {
        ticker: newAsset.ticker.trim().toUpperCase(),
        setor: newAsset.setor.trim() || 'Geral',
        peso: Number(newAsset.peso) || 1.0,
        habilitado: 1.0,
        ajusteManualQtd: 0,
        dpaForecast: newAsset.dpaForecast ? Number(newAsset.dpaForecast) : null,
        precoAtual: newAsset.precoAtual ? Number(newAsset.precoAtual) : null,
        lpa: newAsset.lpa ? Number(newAsset.lpa) : null,
        vpa: newAsset.vpa ? Number(newAsset.vpa) : null,
        ordem: assets.length,
      };

      const res = await purchasePlanService.saveAsset(payload);
      setAssets(prev => [...prev, ...normalizeAssets([res.data])]);
      setOpenAddDialog(false);
      setNewAsset({ ticker: '', setor: '', peso: 1.0, dpaForecast: '', precoAtual: '', lpa: '', vpa: '' });
      showToast(`Ativo ${payload.ticker} adicionado com sucesso!`, 'success');
    } catch (err) {
      showToast(err.response?.data || 'Erro ao cadastrar ativo', 'error');
    }
  };

  // Copiar Boleta de Ordens para Área de Transferência
  const handleCopyBoleta = () => {
    const dateStr = new Date().toLocaleDateString('pt-BR');
    let texto = `=== BOLETA DE ORDENS - PLANO DE COMPRAS ===\n`;
    texto += `Data: ${dateStr}\n`;
    texto += `Aporte da Rodada: R$ ${aporteAtivo.toLocaleString('pt-BR', { minimumFractionDigits: 2, maximumFractionDigits: 2 })}\n`;
    texto += `--------------------------------------------------\n`;
    texto += `TICKER   | QTD    | PREÇO        | TOTAL\n`;
    texto += `--------------------------------------------------\n`;

    calculatedItems.forEach(item => {
      if (item.qtdFinal > 0) {
        const t = item.ticker.padEnd(8, ' ');
        const q = String(item.qtdFinal).padEnd(6, ' ');
        const p = `R$ ${(item.precoAtual || 0).toLocaleString('pt-BR', { minimumFractionDigits: 2, maximumFractionDigits: 2 })}`.padEnd(12, ' ');
        const tot = `R$ ${item.totalItem.toLocaleString('pt-BR', { minimumFractionDigits: 2, maximumFractionDigits: 2 })}`;
        texto += `${t} | ${q} | ${p} | ${tot}\n`;
      }
    });

    texto += `--------------------------------------------------\n`;
    texto += `Total a Pagar: R$ ${roundTotals.totalGasto.toLocaleString('pt-BR', { minimumFractionDigits: 2, maximumFractionDigits: 2 })}\n`;
    texto += `Sobra de Caixa: R$ ${roundTotals.sobraCaixa.toLocaleString('pt-BR', { minimumFractionDigits: 2, maximumFractionDigits: 2 })}\n`;
    texto += `Total de Ações: ${roundTotals.totalAcoes} un.\n`;
    texto += `==================================================`;

    navigator.clipboard.writeText(texto).then(() => {
      showToast('Boleta de ordens copiada para a área de transferência!', 'success');
    }).catch(() => {
      showToast('Não foi possível copiar automaticamente para o clipboard', 'warning');
    });
  };

  // Salvar Parâmetros Macro
  const handleSaveMacroSettings = async () => {
    try {
      const updated = {
        ...config,
        altaAno: tempSettings.altaAno !== '' && tempSettings.altaAno !== null ? Number(tempSettings.altaAno) : null,
        fiboUp: tempSettings.fiboUp !== '' && tempSettings.fiboUp !== null ? Number(tempSettings.fiboUp) : null,
        fiboDown: tempSettings.fiboDown !== '' && tempSettings.fiboDown !== null ? Number(tempSettings.fiboDown) : null,
      };
      await purchasePlanService.saveConfig(updated);
      setConfig(updated);
      setOpenSettingsDialog(false);
      fetchBenchmark(config.benchmark);
      showToast('Parâmetros macro atualizados com sucesso!', 'success');
    } catch (err) {
      showToast('Erro ao salvar parâmetros macro', 'error');
    }
  };

  const handleResetMacroSettingsToAuto = () => {
    setTempSettings({
      altaAno: '',
      fiboUp: '',
      fiboDown: '',
    });
  };


  if (loading) {
    return (
      <Box sx={{ display: 'flex', justifyContent: 'center', alignItems: 'center', minHeight: 400 }}>
        <CircularProgress size={48} sx={{ color: 'primary.main' }} />
      </Box>
    );
  }

  return (
    <Box sx={{ display: 'flex', flexDirection: 'column', gap: 3 }}>
      {/* =========================================================
          CABEÇALHO & BARRA DE AÇÕES SUPERIOR
      ========================================================= */}
      <Box
        sx={{
          display: 'flex',
          flexDirection: { xs: 'column', lg: 'row' },
          justifyContent: 'space-between',
          alignItems: { xs: 'flex-start', lg: 'center' },
          gap: 2,
        }}
      >
        <Box>
          <Typography
            variant="h5"
            sx={{
              fontWeight: 800,
              letterSpacing: '-0.01em',
              color: 'text.primary',
              display: 'flex',
              alignItems: 'center',
              gap: 1.5,
            }}
          >
            <AccountBalanceWalletOutlinedIcon sx={{ color: 'primary.main', fontSize: 30 }} />
            Plano de Compras & Alocação de Ativos
          </Typography>
          <Typography variant="body2" sx={{ color: 'text.secondary', mt: 0.5 }}>
            Gestão de aportes periódicos na B3 com rateio por pesos e margem de segurança fundamentalista (Décio Bazin e Benjamin Graham).
          </Typography>
        </Box>

        <Box sx={{ display: 'flex', flexWrap: 'wrap', gap: 1.5, alignItems: 'center' }}>
          <Button
            variant="outlined"
            size="small"
            startIcon={<AddCircleOutlineIcon />}
            onClick={() => setOpenAddDialog(true)}
            sx={{ borderRadius: '9999px', textTransform: 'none', fontWeight: 600 }}
          >
            Adicionar Ativo
          </Button>

          <Button
            variant="outlined"
            size="small"
            color="secondary"
            startIcon={syncingBrapi ? <CircularProgress size={16} color="inherit" /> : <BoltIcon sx={{ color: '#fbbf24' }} />}
            onClick={handleOpenBrapiSync}
            disabled={syncingBrapi}
            sx={{ borderRadius: '9999px', textTransform: 'none', fontWeight: 600 }}
          >
            {syncingBrapi ? 'Sincronizando...' : 'Preencher LPA e VPA (Brapi)'}
          </Button>

          <Button
            variant="outlined"
            size="small"
            startIcon={syncingQuotes ? <CircularProgress size={16} color="inherit" /> : <RefreshIcon />}
            onClick={handleSyncQuotes}
            disabled={syncingQuotes}
            sx={{ borderRadius: '9999px', textTransform: 'none', fontWeight: 600 }}
          >
            {syncingQuotes ? 'Buscando...' : 'Atualizar Cotações'}
          </Button>

          <Button
            variant="contained"
            size="small"
            color="primary"
            startIcon={saving ? <CircularProgress size={16} color="inherit" /> : <SaveIcon />}
            onClick={handleSaveAll}
            disabled={saving}
            sx={{
              borderRadius: '9999px',
              textTransform: 'none',
              fontWeight: 700,
              px: 2.5,
            }}
          >
            {saving ? 'Salvando...' : 'Salvar Alterações'}
          </Button>
        </Box>
      </Box>

      {/* =========================================================
          CARDS SUPERIORES: ORÇAMENTO & TERMÔMETRO MACRO
      ========================================================= */}
      <Box
        sx={{
          display: 'grid',
          gridTemplateColumns: { xs: '1fr', lg: '1fr 1fr' },
          gap: 3,
        }}
      >
        {/* Card 1: Orçamento e Aporte da Rodada */}
        <Card sx={{ borderRadius: 3, border: '1px solid #43474e', backgroundColor: '#171c26' }}>
          <CardContent sx={{ p: 2.5, display: 'flex', flexDirection: 'column', gap: 2 }}>
            <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
              <Box sx={{ display: 'flex', alignItems: 'center', gap: 1 }}>
                <AccountBalanceWalletOutlinedIcon sx={{ color: 'primary.light', fontSize: 22 }} />
                <Typography variant="subtitle1" sx={{ fontWeight: 700, color: 'text.primary' }}>
                  Orçamento & Periodicidade
                </Typography>
              </Box>
              <Chip
                label={config.periodicidade === 'SEMANAL' ? 'Ciclo Semanal' : 'Ciclo Mensal'}
                color="primary"
                variant="outlined"
                size="small"
                sx={{ fontWeight: 600 }}
              />
            </Box>

            <Box sx={{ display: 'grid', gridTemplateColumns: { xs: '1fr', sm: '1.2fr 0.8fr 1.2fr' }, gap: 2 }}>
              <TextField
                label="Saldo Total a Investir"
                size="small"
                type="number"
                value={config.saldoTotal}
                onChange={(e) => setConfig({ ...config, saldoTotal: e.target.value })}
                onKeyDown={handleKeyDown}
                InputProps={{
                  startAdornment: <Typography variant="body2" sx={{ mr: 1, color: 'text.secondary' }}>R$</Typography>,
                }}
              />

              <TextField
                label="Parcelas (Meses)"
                size="small"
                type="number"
                value={config.parcelasMeses}
                onChange={(e) => setConfig({ ...config, parcelasMeses: e.target.value })}
                onKeyDown={handleKeyDown}
                inputProps={{ min: 1, max: 120 }}
              />

              <ToggleButtonGroup
                value={config.periodicidade}
                exclusive
                onChange={(e, val) => val && setConfig({ ...config, periodicidade: val })}
                size="small"
                sx={{ height: 40 }}
              >
                <ToggleButton value="SEMANAL" sx={{ flex: 1, fontWeight: 600, fontSize: '0.75rem' }}>
                  Semanal
                </ToggleButton>
                <ToggleButton value="MENSAL" sx={{ flex: 1, fontWeight: 600, fontSize: '0.75rem' }}>
                  Mensal
                </ToggleButton>
              </ToggleButtonGroup>
            </Box>

            <Divider sx={{ borderColor: 'rgba(255,255,255,0.08)' }} />

            {/* Linha do Aporte da Rodada */}
            <Box
              sx={{
                display: 'flex',
                flexDirection: { xs: 'column', sm: 'row' },
                justifyContent: 'space-between',
                alignItems: { xs: 'flex-start', sm: 'center' },
                gap: 2,
                backgroundColor: '#1f2533',
                p: 1.5,
                borderRadius: 2,
              }}
            >
              <Box>
                <Typography variant="caption" sx={{ color: 'text.secondary', display: 'block' }}>
                  Aporte Calculado da Rodada
                </Typography>
                <Typography variant="h6" sx={{ fontWeight: 800, color: 'primary.light' }}>
                  R$ {aporteCalculado.toLocaleString('pt-BR', { minimumFractionDigits: 2, maximumFractionDigits: 2 })}
                </Typography>
                <Typography variant="caption" sx={{ color: 'text.disabled' }}>
                  {config.periodicidade === 'SEMANAL' ? '(Saldo ÷ Parcelas ÷ 4 semanas)' : '(Saldo ÷ Parcelas)'}
                </Typography>
              </Box>

              <Box sx={{ display: 'flex', alignItems: 'center', gap: 1 }}>
                <TextField
                  label="Sobrescrever Aporte (R$)"
                  size="small"
                  type="number"
                  placeholder="Automático"
                  autoComplete="off"
                  name="aporte_manual_valor_custom"
                  id="aporte_manual_valor_custom"
                  inputProps={{
                    autoComplete: 'new-password',
                    inputMode: 'decimal',
                    min: 0,
                    step: '0.01',
                  }}
                  value={config.aporteRodadaManual ?? ''}
                  onChange={(e) =>
                    setConfig({
                      ...config,
                      aporteRodadaManual: e.target.value === '' ? null : e.target.value,
                    })
                  }
                  onKeyDown={handleKeyDown}
                  sx={{ width: 170 }}
                />
                {config.aporteRodadaManual && (
                  <Button
                    size="small"
                    variant="text"
                    color="inherit"
                    onClick={() => setConfig({ ...config, aporteRodadaManual: null })}
                    sx={{ textTransform: 'none', fontSize: '0.75rem' }}
                  >
                    Limpar
                  </Button>
                )}
              </Box>
            </Box>
          </CardContent>
        </Card>

        {/* Card 2: Termômetro Macro (IBOV / IDIV & Fibonacci) */}
        <Card sx={{ borderRadius: 3, border: '1px solid #43474e', backgroundColor: '#171c26' }}>
          <CardContent sx={{ p: 2.5, display: 'flex', flexDirection: 'column', gap: 2 }}>
            <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
              <Box sx={{ display: 'flex', alignItems: 'center', gap: 1 }}>
                <TrendingDownIcon sx={{ color: '#ec4899', fontSize: 22 }} />
                <Typography variant="subtitle1" sx={{ fontWeight: 700, color: 'text.primary' }}>
                  Termômetro Macro de Mercado
                </Typography>
              </Box>

              <Box sx={{ display: 'flex', alignItems: 'center', gap: 1 }}>
                <ToggleButtonGroup
                  value={config.benchmark || 'IBOV'}
                  exclusive
                  onChange={(e, val) => {
                    if (val) {
                      setConfig({ ...config, benchmark: val });
                      fetchBenchmark(val);
                    }
                  }}
                  size="small"
                >
                  <ToggleButton value="IBOV" sx={{ fontWeight: 700, px: 1.5, py: 0.2 }}>
                    IBOV
                  </ToggleButton>
                  <ToggleButton value="IDIV" sx={{ fontWeight: 700, px: 1.5, py: 0.2 }}>
                    IDIV
                  </ToggleButton>
                </ToggleButtonGroup>

                <IconButton
                  size="small"
                  onClick={() => {
                    setTempSettings({
                      altaAno: config.altaAno || '',
                      fiboUp: config.fiboUp || '',
                      fiboDown: config.fiboDown || '',
                    });
                    setOpenSettingsDialog(true);
                  }}
                  title="Calibrar Parâmetros Fibonacci / Máximas"
                  sx={{ color: 'text.secondary' }}
                >
                  <SettingsOutlinedIcon fontSize="small" />
                </IconButton>
              </Box>
            </Box>

            {loadingBenchmark ? (
              <Box sx={{ py: 3, textAlign: 'center' }}>
                <CircularProgress size={24} color="primary" />
              </Box>
            ) : (
              <>
                {/* Linha de Cotação, Máxima, Mínima e Drawdown */}
                <Box sx={{ display: 'grid', gridTemplateColumns: { xs: 'repeat(2, 1fr)', sm: 'repeat(4, 1fr)' }, gap: 1.5, textAlign: 'center' }}>
                  <Box sx={{ backgroundColor: '#1f2533', p: 1.2, borderRadius: 2 }}>
                    <Typography variant="caption" sx={{ color: 'text.secondary' }}>Cotação Atual</Typography>
                    <Typography variant="h6" sx={{ fontWeight: 800, color: 'text.primary' }}>
                      {benchmarkStatus?.precoAtual != null
                        ? Number(benchmarkStatus.precoAtual).toLocaleString('pt-BR')
                        : '---'}
                    </Typography>
                  </Box>

                  <Box sx={{ backgroundColor: '#1f2533', p: 1.2, borderRadius: 2 }}>
                    <Box sx={{ display: 'flex', alignItems: 'center', justifyContent: 'center', gap: 0.5 }}>
                      <Typography variant="caption" sx={{ color: 'text.secondary' }}>Máxima (52 sem)</Typography>
                      {config.altaAno != null && (
                        <Chip label="Manual" size="small" sx={{ fontSize: '0.6rem', height: 16, backgroundColor: '#374151' }} />
                      )}
                    </Box>
                    <Typography variant="h6" sx={{ fontWeight: 800, color: 'text.secondary' }}>
                      {benchmarkStatus?.altaAno != null
                        ? Number(benchmarkStatus.altaAno).toLocaleString('pt-BR')
                        : '---'}
                    </Typography>
                  </Box>

                  <Box sx={{ backgroundColor: '#1f2533', p: 1.2, borderRadius: 2 }}>
                    <Box sx={{ display: 'flex', alignItems: 'center', justifyContent: 'center', gap: 0.5 }}>
                      <Typography variant="caption" sx={{ color: 'text.secondary' }}>Mínima (52 sem)</Typography>
                      {config.fiboDown != null && (
                        <Chip label="Manual" size="small" sx={{ fontSize: '0.6rem', height: 16, backgroundColor: '#374151' }} />
                      )}
                    </Box>
                    <Typography variant="h6" sx={{ fontWeight: 800, color: 'text.secondary' }}>
                      {benchmarkStatus?.fiboDown != null
                        ? Number(benchmarkStatus.fiboDown).toLocaleString('pt-BR')
                        : '---'}
                    </Typography>
                  </Box>

                  <Box sx={{ backgroundColor: '#1f2533', p: 1.2, borderRadius: 2 }}>
                    <Typography variant="caption" sx={{ color: 'text.secondary' }}>Drawdown</Typography>
                    <Typography
                      variant="h6"
                      sx={{
                        fontWeight: 800,
                        color:
                          benchmarkStatus?.drawdownPercent < -15
                            ? 'error.main'
                            : benchmarkStatus?.drawdownPercent < -5
                            ? 'warning.main'
                            : 'success.main',
                      }}
                    >
                      {benchmarkStatus?.drawdownPercent != null ? `${benchmarkStatus.drawdownPercent}%` : '---'}
                    </Typography>
                  </Box>
                </Box>

                {/* Régua Gradiente de Retração Fibonacci */}
                <Box sx={{ mt: 0.5 }}>
                  <Box sx={{ display: 'flex', justifyContent: 'space-between', mb: 0.5 }}>
                    <Typography variant="caption" sx={{ color: 'text.secondary', fontWeight: 600 }}>
                      Retração Fibonacci
                    </Typography>
                    <Typography variant="caption" sx={{ color: 'primary.light', fontWeight: 700 }}>
                      {benchmarkStatus?.fiboRetractionPercent != null
                        ? `${benchmarkStatus.fiboRetractionPercent}% Retraído`
                        : 'Aguardando dados'}
                    </Typography>
                  </Box>

                  {/* Barra Visual */}
                  <Box
                    sx={{
                      position: 'relative',
                      height: 12,
                      borderRadius: 6,
                      background: 'linear-gradient(90deg, #10b981 0%, #f59e0b 50%, #ef4444 100%)',
                      overflow: 'hidden',
                    }}
                  >
                    {benchmarkStatus?.fiboRetractionPercent != null && (
                      <Box
                        sx={{
                          position: 'absolute',
                          left: `${Math.min(100, Math.max(0, benchmarkStatus.fiboRetractionPercent))}%`,
                          top: 0,
                          bottom: 0,
                          width: 4,
                          backgroundColor: '#ffffff',
                          boxShadow: '0 0 8px #ffffff',
                          transform: 'translateX(-50%)',
                        }}
                      />
                    )}
                  </Box>

                  {/* Marcadores de Nível */}
                  <Box sx={{ display: 'flex', justifyContent: 'space-between', mt: 0.5, px: 0.5 }}>
                    <Typography variant="caption" sx={{ color: 'text.disabled', fontSize: '0.65rem' }}>0%</Typography>
                    <Typography variant="caption" sx={{ color: 'text.disabled', fontSize: '0.65rem' }}>38.2%</Typography>
                    <Typography variant="caption" sx={{ color: 'text.disabled', fontSize: '0.65rem' }}>50%</Typography>
                    <Typography variant="caption" sx={{ color: 'text.disabled', fontSize: '0.65rem' }}>61.8%</Typography>
                    <Typography variant="caption" sx={{ color: 'text.disabled', fontSize: '0.65rem' }}>100%</Typography>
                  </Box>
                </Box>
              </>
            )}
          </CardContent>
        </Card>
      </Box>

      {/* =========================================================
          TABELA EDITÁVEL DE ATIVOS DA CARTEIRA
      ========================================================= */}
      <Card sx={{ borderRadius: 3, border: '1px solid #43474e', overflow: 'hidden' }}>
        <Box
          sx={{
            p: 2,
            px: 2.5,
            borderBottom: '1px solid #43474e',
            display: 'flex',
            justifyContent: 'space-between',
            alignItems: 'center',
            backgroundColor: '#171c26',
          }}
        >
          <Box>
            <Typography variant="h6" sx={{ fontWeight: 700 }}>
              Carteira Operacional ({calculatedItems.length} ativos monitorados)
            </Typography>
            <Typography variant="caption" sx={{ color: 'text.secondary' }}>
              Edite diretamente os campos nas células e pressione <strong>Enter</strong> para salvar tudo.
            </Typography>
          </Box>
          <Box sx={{ display: 'flex', alignItems: 'center', gap: 1.5 }}>
            <ToggleButtonGroup
              value={filterOnlyEnabled ? 'enabled' : 'all'}
              exclusive
              onChange={(e, val) => {
                if (val !== null) setFilterOnlyEnabled(val === 'enabled');
              }}
              size="small"
              sx={{ height: 32 }}
            >
              <ToggleButton value="all" sx={{ px: 1.5, fontSize: '0.75rem', fontWeight: 600 }}>
                Todos ({calculatedItems.length})
              </ToggleButton>
              <ToggleButton value="enabled" sx={{ px: 1.5, fontSize: '0.75rem', fontWeight: 600 }}>
                Apenas Habilitados ({roundTotals.totalHabilitados})
              </ToggleButton>
            </ToggleButtonGroup>

            <Chip
              label={`${roundTotals.totalHabilitados} ativos ativos na rodada`}
              color="success"
              size="small"
              variant="outlined"
              sx={{ fontWeight: 600 }}
            />
          </Box>
        </Box>

        <TableContainer component={Paper} sx={{ backgroundColor: 'transparent', boxShadow: 'none', overflowX: 'auto' }}>
          <Table size="small" sx={{ minWidth: 1650 }}>
            <TableHead>
              <TableRow sx={{ backgroundColor: '#131822' }}>
                <TableCell sx={{ fontWeight: 700, width: 100, minWidth: 100, whiteSpace: 'nowrap' }}>Ticker</TableCell>
                <TableCell sx={{ fontWeight: 700, width: 110, minWidth: 110, whiteSpace: 'nowrap' }}>Setor</TableCell>
                <TableCell sx={{ fontWeight: 700, width: 110, minWidth: 110, whiteSpace: 'nowrap' }} align="right">Preço Atual</TableCell>
                <TableCell sx={{ fontWeight: 700, width: 100, minWidth: 100, whiteSpace: 'nowrap' }} align="right">DPA Proj.</TableCell>
                <TableCell sx={{ fontWeight: 700, width: 135, minWidth: 135, whiteSpace: 'nowrap' }} align="right">Teto Bazin</TableCell>
                <TableCell sx={{ fontWeight: 700, width: 95, minWidth: 95, whiteSpace: 'nowrap' }} align="right">LPA</TableCell>
                <TableCell sx={{ fontWeight: 700, width: 95, minWidth: 95, whiteSpace: 'nowrap' }} align="right">VPA</TableCell>
                <TableCell sx={{ fontWeight: 700, width: 115, minWidth: 115, whiteSpace: 'nowrap' }} align="right">P. Graham</TableCell>
                <TableCell sx={{ fontWeight: 700, width: 105, minWidth: 105, whiteSpace: 'nowrap' }} align="center">Margem %</TableCell>
                <TableCell sx={{ fontWeight: 700, width: 85, minWidth: 85, whiteSpace: 'nowrap' }} align="center">Habilita</TableCell>
                <TableCell sx={{ fontWeight: 700, width: 85, minWidth: 85, whiteSpace: 'nowrap' }} align="center">Peso</TableCell>
                <TableCell sx={{ fontWeight: 700, width: 95, minWidth: 95, whiteSpace: 'nowrap' }} align="right">Qtd Sug.</TableCell>
                <TableCell sx={{ fontWeight: 700, width: 120, minWidth: 120, whiteSpace: 'nowrap' }} align="center">Ajuste (+/-)</TableCell>
                <TableCell sx={{ fontWeight: 700, width: 95, minWidth: 95, whiteSpace: 'nowrap' }} align="right">Qtd Final</TableCell>
                <TableCell sx={{ fontWeight: 700, width: 135, minWidth: 135, whiteSpace: 'nowrap' }} align="right">Total a Pagar</TableCell>
                <TableCell sx={{ fontWeight: 700, width: 65, minWidth: 65, whiteSpace: 'nowrap' }} align="center">Ações</TableCell>
              </TableRow>
            </TableHead>

            <TableBody>
              {calculatedItems.length === 0 ? (
                <TableRow>
                  <TableCell colSpan={16} align="center" sx={{ py: 6, color: 'text.secondary', whiteSpace: 'nowrap' }}>
                    <Typography variant="body1" sx={{ mb: 1.5 }}>
                      Nenhum ativo cadastrado na carteira de compras.
                    </Typography>
                    <Button
                      variant="contained"
                      size="small"
                      startIcon={<AddCircleOutlineIcon />}
                      onClick={() => setOpenAddDialog(true)}
                    >
                      Adicionar Primeiro Ativo
                    </Button>
                  </TableCell>
                </TableRow>
              ) : displayedItems.length === 0 ? (
                <TableRow>
                  <TableCell colSpan={16} align="center" sx={{ py: 6, color: 'text.secondary', whiteSpace: 'nowrap' }}>
                    <Typography variant="body1" sx={{ mb: 1.5 }}>
                      Nenhum ativo habilitado na rodada.
                    </Typography>
                    <Button
                      variant="outlined"
                      size="small"
                      onClick={() => setFilterOnlyEnabled(false)}
                      sx={{ borderRadius: '9999px', textTransform: 'none' }}
                    >
                      Exibir Todos os Ativos ({calculatedItems.length})
                    </Button>
                  </TableCell>
                </TableRow>
              ) : (
                displayedItems.map((row) => {
                  const isHabilitado = (row.habilitado != null ? Number(row.habilitado) : 1.0) > 0;
                  const isBelowBazin =
                    row.precoAtual != null && row.tetoBazin != null && row.precoAtual <= row.tetoBazin;
                  const isBelowGraham =
                    row.precoAtual != null && row.precoGraham != null && row.precoAtual <= row.precoGraham;

                  return (
                    <TableRow
                      key={row.ticker}
                      hover
                      sx={{
                        opacity: isHabilitado ? 1 : 0.45,
                        backgroundColor: isHabilitado ? 'transparent' : 'rgba(0,0,0,0.15)',
                        transition: 'background-color 0.2s, opacity 0.2s',
                      }}
                    >
                      {/* Ticker */}
                      <TableCell sx={{ fontWeight: 800, color: isHabilitado ? 'primary.light' : 'text.disabled', whiteSpace: 'nowrap', minWidth: 100 }}>
                        {row.ticker}
                      </TableCell>

                      {/* Setor */}
                      <TableCell sx={{ whiteSpace: 'nowrap', minWidth: 110 }}>
                        <Chip
                          label={row.setor || 'Geral'}
                          size="small"
                          sx={{
                            fontSize: '0.7rem',
                            height: 22,
                            backgroundColor: '#262d3d',
                            color: isHabilitado ? 'text.secondary' : 'text.disabled',
                          }}
                        />
                      </TableCell>

                      {/* Preço Atual */}
                      <TableCell align="right" sx={{ whiteSpace: 'nowrap', minWidth: 110 }}>
                        <TextField
                          size="small"
                          type="number"
                          variant="standard"
                          value={row.precoAtual ?? ''}
                          onChange={(e) => handleAssetFieldChange(row.ticker, 'precoAtual', e.target.value)}
                          onKeyDown={handleKeyDown}
                          inputProps={{ step: '0.01', style: { textAlign: 'right', fontWeight: 600 } }}
                          sx={{ width: 85 }}
                        />
                      </TableCell>

                      {/* DPA Forecast */}
                      <TableCell align="right" sx={{ whiteSpace: 'nowrap', minWidth: 100 }}>
                        <TextField
                          size="small"
                          type="number"
                          variant="standard"
                          value={row.dpaForecast ?? ''}
                          onChange={(e) => handleAssetFieldChange(row.ticker, 'dpaForecast', e.target.value)}
                          onKeyDown={handleKeyDown}
                          inputProps={{ step: '0.01', style: { textAlign: 'right' } }}
                          sx={{ width: 75 }}
                        />
                      </TableCell>

                      {/* Teto Bazin */}
                      <TableCell align="right" sx={{ whiteSpace: 'nowrap', minWidth: 135 }}>
                        {row.tetoBazin != null ? (
                          <Box sx={{ display: 'inline-flex', alignItems: 'center', justifyContent: 'flex-end', gap: 0.5, flexWrap: 'nowrap', whiteSpace: 'nowrap' }}>
                            <Typography
                              variant="body2"
                              sx={{
                                fontWeight: 700,
                                whiteSpace: 'nowrap',
                                color: !isHabilitado ? 'text.disabled' : (isBelowBazin ? 'success.light' : 'text.primary'),
                              }}
                            >
                              R$ {row.tetoBazin.toFixed(2)}
                            </Typography>
                            {isBelowBazin && (
                              <Tooltip title="Preço atual abaixo ou igual ao teto de Bazin!">
                                <CheckCircleOutlineIcon sx={{ color: isHabilitado ? 'success.light' : 'text.disabled', fontSize: 16, flexShrink: 0 }} />
                              </Tooltip>
                            )}
                          </Box>
                        ) : (
                          '---'
                        )}
                      </TableCell>

                      {/* LPA */}
                      <TableCell align="right" sx={{ whiteSpace: 'nowrap', minWidth: 95 }}>
                        <TextField
                          size="small"
                          type="number"
                          variant="standard"
                          value={row.lpa ?? ''}
                          onChange={(e) => handleAssetFieldChange(row.ticker, 'lpa', e.target.value)}
                          onKeyDown={handleKeyDown}
                          inputProps={{ step: '0.01', style: { textAlign: 'right' } }}
                          sx={{ width: 70 }}
                        />
                      </TableCell>

                      {/* VPA */}
                      <TableCell align="right" sx={{ whiteSpace: 'nowrap', minWidth: 95 }}>
                        <TextField
                          size="small"
                          type="number"
                          variant="standard"
                          value={row.vpa ?? ''}
                          onChange={(e) => handleAssetFieldChange(row.ticker, 'vpa', e.target.value)}
                          onKeyDown={handleKeyDown}
                          inputProps={{ step: '0.01', style: { textAlign: 'right' } }}
                          sx={{ width: 70 }}
                        />
                      </TableCell>

                      {/* Preço Graham */}
                      <TableCell align="right" sx={{ whiteSpace: 'nowrap', minWidth: 115 }}>
                        {row.precoGraham != null ? (
                          <Typography
                            variant="body2"
                            sx={{
                              fontWeight: 600,
                              whiteSpace: 'nowrap',
                              color: !isHabilitado ? 'text.disabled' : (isBelowGraham ? 'success.light' : 'text.secondary'),
                            }}
                          >
                            R$ {row.precoGraham.toFixed(2)}
                          </Typography>
                        ) : (
                          '---'
                        )}
                      </TableCell>

                      {/* Margem Bazin % */}
                      <TableCell align="center" sx={{ whiteSpace: 'nowrap', minWidth: 105 }}>
                        {row.margemBazin != null ? (
                          <Chip
                            label={`${row.margemBazin > 0 ? '+' : ''}${row.margemBazin}%`}
                            size="small"
                            color={isHabilitado ? (row.margemBazin >= 0 ? 'success' : 'error') : 'default'}
                            sx={{
                              fontWeight: 700,
                              fontSize: '0.72rem',
                              height: 22,
                              whiteSpace: 'nowrap',
                              opacity: isHabilitado ? 1 : 0.6,
                            }}
                          />
                        ) : (
                          '---'
                        )}
                      </TableCell>

                      {/* Switch Habilita */}
                      <TableCell align="center" sx={{ whiteSpace: 'nowrap', minWidth: 85 }}>
                        <Switch
                          size="small"
                          checked={isHabilitado}
                          onChange={() => handleToggleHabilitado(row.ticker)}
                          color="primary"
                        />
                      </TableCell>

                      {/* Peso */}
                      <TableCell align="center" sx={{ whiteSpace: 'nowrap', minWidth: 85 }}>
                        <TextField
                          size="small"
                          type="number"
                          variant="standard"
                          value={row.peso ?? 1.0}
                          onChange={(e) => handleAssetFieldChange(row.ticker, 'peso', e.target.value)}
                          onKeyDown={handleKeyDown}
                          disabled={!isHabilitado}
                          inputProps={{ step: '0.1', min: '0', style: { textAlign: 'center', width: 45 } }}
                        />
                      </TableCell>

                      {/* Qtd Sugerida */}
                      <TableCell align="right" sx={{ fontWeight: 600, color: isHabilitado ? 'text.secondary' : 'text.disabled', whiteSpace: 'nowrap', minWidth: 95 }}>
                        {isHabilitado ? row.qtdSugerida : 0}
                      </TableCell>

                      {/* Ajuste Manual (+/-) */}
                      <TableCell align="center" sx={{ whiteSpace: 'nowrap', minWidth: 120 }}>
                        <Box sx={{ display: 'inline-flex', alignItems: 'center', gap: 0.5, whiteSpace: 'nowrap' }}>
                          <IconButton
                            size="small"
                            disabled={!isHabilitado}
                            onClick={() => handleAjusteManualChange(row.ticker, -1)}
                            sx={{ p: 0.4, border: '1px solid rgba(255,255,255,0.1)' }}
                          >
                            <RemoveIcon sx={{ fontSize: 14 }} />
                          </IconButton>
                          <Typography variant="body2" sx={{ minWidth: 24, textAlign: 'center', fontWeight: 600, color: isHabilitado ? 'text.primary' : 'text.disabled', whiteSpace: 'nowrap' }}>
                            {isHabilitado ? (row.ajusteManualQtd > 0 ? `+${row.ajusteManualQtd}` : row.ajusteManualQtd) : 0}
                          </Typography>
                          <IconButton
                            size="small"
                            disabled={!isHabilitado}
                            onClick={() => handleAjusteManualChange(row.ticker, 1)}
                            sx={{ p: 0.4, border: '1px solid rgba(255,255,255,0.1)' }}
                          >
                            <AddIcon sx={{ fontSize: 14 }} />
                          </IconButton>
                        </Box>
                      </TableCell>

                      {/* Qtd Final */}
                      <TableCell align="right" sx={{ fontWeight: 800, color: isHabilitado ? 'primary.light' : 'text.disabled', fontSize: '0.95rem', whiteSpace: 'nowrap', minWidth: 95 }}>
                        {isHabilitado ? row.qtdFinal : 0}
                      </TableCell>

                      {/* Total a Pagar */}
                      <TableCell align="right" sx={{ fontWeight: 700, color: isHabilitado ? 'text.primary' : 'text.disabled', whiteSpace: 'nowrap', minWidth: 135 }}>
                        R$ {(isHabilitado ? row.totalItem : 0).toLocaleString('pt-BR', { minimumFractionDigits: 2, maximumFractionDigits: 2 })}
                      </TableCell>

                      {/* Ações */}
                      <TableCell align="center" sx={{ whiteSpace: 'nowrap', minWidth: 65 }}>
                        <IconButton
                          size="small"
                          color="error"
                          onClick={() => handleDeleteAsset(row.ticker)}
                          title="Excluir ativo"
                          sx={{ p: 0.5 }}
                        >
                          <DeleteOutlineIcon fontSize="small" />
                        </IconButton>
                      </TableCell>
                    </TableRow>
                  );
                })
              )}
            </TableBody>
          </Table>
        </TableContainer>
      </Card>

      {/* =========================================================
          RODAPÉ: RESUMO DA RODADA & COPIAR BOLETA
      ========================================================= */}
      <Card
        sx={{
          borderRadius: 3,
          border: '1px solid #43474e',
          backgroundColor: '#171c26',
          p: 2.5,
        }}
      >
        <Box
          sx={{
            display: 'flex',
            flexDirection: { xs: 'column', md: 'row' },
            justifyContent: 'space-between',
            alignItems: { xs: 'flex-start', md: 'center' },
            gap: 3,
          }}
        >
          {/* Estatísticas de Resumo */}
          <Box
            sx={{
              display: 'grid',
              gridTemplateColumns: { xs: 'repeat(2, 1fr)', sm: 'repeat(4, 1fr)' },
              gap: { xs: 2, sm: 4 },
              flexGrow: 1,
            }}
          >
            <Box>
              <Typography variant="caption" sx={{ color: 'text.secondary', display: 'block' }}>
                Aporte da Rodada
              </Typography>
              <Typography variant="h6" sx={{ fontWeight: 800, color: 'primary.light' }}>
                R$ {aporteAtivo.toLocaleString('pt-BR', { minimumFractionDigits: 2, maximumFractionDigits: 2 })}
              </Typography>
            </Box>

            <Box>
              <Typography variant="caption" sx={{ color: 'text.secondary', display: 'block' }}>
                Total a Pagar
              </Typography>
              <Typography variant="h6" sx={{ fontWeight: 800, color: 'text.primary' }}>
                R$ {roundTotals.totalGasto.toLocaleString('pt-BR', { minimumFractionDigits: 2, maximumFractionDigits: 2 })}
              </Typography>
            </Box>

            <Box>
              <Typography variant="caption" sx={{ color: 'text.secondary', display: 'block' }}>
                Sobra de Caixa
              </Typography>
              <Typography
                variant="h6"
                sx={{
                  fontWeight: 800,
                  color: roundTotals.sobraCaixa >= 0 ? 'success.light' : 'error.main',
                }}
              >
                R$ {roundTotals.sobraCaixa.toLocaleString('pt-BR', { minimumFractionDigits: 2, maximumFractionDigits: 2 })}
              </Typography>
            </Box>

            <Box>
              <Typography variant="caption" sx={{ color: 'text.secondary', display: 'block' }}>
                Total de Ações Compradas
              </Typography>
              <Typography variant="h6" sx={{ fontWeight: 800, color: 'text.primary' }}>
                {roundTotals.totalAcoes} <Typography component="span" variant="caption" sx={{ color: 'text.secondary' }}>ações</Typography>
              </Typography>
            </Box>
          </Box>

          {/* Botão Copiar Boleta */}
          <Button
            variant="contained"
            color="primary"
            startIcon={<ContentCopyIcon />}
            onClick={handleCopyBoleta}
            disabled={roundTotals.totalAcoes === 0}
            sx={{
              borderRadius: '9999px',
              py: 1.2,
              px: 3,
              fontWeight: 700,
              fontSize: '0.9rem',
              whiteSpace: 'nowrap',
              boxShadow: '0 4px 15px rgba(99, 102, 241, 0.4)',
            }}
          >
            Copiar Boleta de Ordens
          </Button>
        </Box>
      </Card>

      {/* =========================================================
          MODAIS E DIÁLOGOS
      ========================================================= */}

      {/* Diálogo: Adicionar Ativo */}
      <Dialog
        open={openAddDialog}
        onClose={() => setOpenAddDialog(false)}
        maxWidth="sm"
        fullWidth
        PaperProps={{ sx: { borderRadius: 3, backgroundColor: '#1b202a', border: '1px solid #43474e' } }}
      >
        <DialogTitle sx={{ fontWeight: 700 }}>Adicionar Novo Ativo à Carteira</DialogTitle>
        <Box component="form" onSubmit={handleCreateAsset}>
          <DialogContent sx={{ display: 'flex', flexDirection: 'column', gap: 2, pt: 1 }}>
            <Box sx={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: 2 }}>
              <TextField
                label="Ticker (ex: WEGE3)"
                required
                autoFocus
                value={newAsset.ticker}
                onChange={(e) => setNewAsset({ ...newAsset, ticker: e.target.value.toUpperCase() })}
              />
              <TextField
                label="Setor (ex: Elétrico, Bancos)"
                value={newAsset.setor}
                onChange={(e) => setNewAsset({ ...newAsset, setor: e.target.value })}
              />
            </Box>

            <Box sx={{ display: 'grid', gridTemplateColumns: 'repeat(3, 1fr)', gap: 2 }}>
              <TextField
                label="Preço Atual (R$)"
                type="number"
                inputProps={{ step: '0.01' }}
                value={newAsset.precoAtual}
                onChange={(e) => setNewAsset({ ...newAsset, precoAtual: e.target.value })}
              />
              <TextField
                label="DPA Forecast (R$)"
                type="number"
                inputProps={{ step: '0.01' }}
                value={newAsset.dpaForecast}
                onChange={(e) => setNewAsset({ ...newAsset, dpaForecast: e.target.value })}
              />
              <TextField
                label="Peso Inicial"
                type="number"
                inputProps={{ step: '0.1', min: 0 }}
                value={newAsset.peso}
                onChange={(e) => setNewAsset({ ...newAsset, peso: e.target.value })}
              />
            </Box>

            <Box sx={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: 2 }}>
              <TextField
                label="LPA (Opcional)"
                type="number"
                inputProps={{ step: '0.01' }}
                value={newAsset.lpa}
                onChange={(e) => setNewAsset({ ...newAsset, lpa: e.target.value })}
              />
              <TextField
                label="VPA (Opcional)"
                type="number"
                inputProps={{ step: '0.01' }}
                value={newAsset.vpa}
                onChange={(e) => setNewAsset({ ...newAsset, vpa: e.target.value })}
              />
            </Box>
          </DialogContent>
          <DialogActions sx={{ p: 2.5, pt: 1 }}>
            <Button onClick={() => setOpenAddDialog(false)} color="inherit">
              Cancelar
            </Button>
            <Button type="submit" variant="contained" color="primary">
              Cadastrar Ativo
            </Button>
          </DialogActions>
        </Box>
      </Dialog>

      {/* Diálogo: Configurar Token Brapi & Sincronizar */}
      <Dialog
        open={openBrapiDialog}
        onClose={() => setOpenBrapiDialog(false)}
        maxWidth="xs"
        fullWidth
        PaperProps={{ sx: { borderRadius: 3, backgroundColor: '#1b202a', border: '1px solid #43474e' } }}
      >
        <DialogTitle sx={{ fontWeight: 700, display: 'flex', alignItems: 'center', gap: 1 }}>
          <BoltIcon sx={{ color: '#fbbf24' }} />
          Sincronização Brapi (LPA e VPA)
        </DialogTitle>
        <DialogContent sx={{ display: 'flex', flexDirection: 'column', gap: 2, pt: 1 }}>
          <Typography variant="body2" sx={{ color: 'text.secondary' }}>
            Para buscar os fundamentos automaticamente em lote, informe seu token da API Brapi. Se ainda não possui um token gratuito, obtenha em{' '}
            <a
              href="https://brapi.dev"
              target="_blank"
              rel="noreferrer"
              style={{ color: '#a5c8ff', textDecoration: 'underline' }}
            >
              brapi.dev
            </a>.
          </Typography>
          <TextField
            label="Token Brapi"
            placeholder="ex: vBf9...xyz"
            fullWidth
            value={brapiTokenInput}
            onChange={(e) => setBrapiTokenInput(e.target.value)}
          />
        </DialogContent>
        <DialogActions sx={{ p: 2.5, pt: 1 }}>
          <Button onClick={() => setOpenBrapiDialog(false)} color="inherit">
            Cancelar
          </Button>
          <Button
            variant="contained"
            color="primary"
            onClick={() => executeBrapiSync(brapiTokenInput)}
            disabled={!brapiTokenInput || syncingBrapi}
          >
            {syncingBrapi ? 'Sincronizando...' : 'Sincronizar Agora'}
          </Button>
        </DialogActions>
      </Dialog>

      {/* Diálogo: Calibração Parâmetros Macro */}
      <Dialog
        open={openSettingsDialog}
        onClose={() => setOpenSettingsDialog(false)}
        maxWidth="xs"
        fullWidth
        PaperProps={{ sx: { borderRadius: 3, backgroundColor: '#1b202a', border: '1px solid #43474e' } }}
      >
        <DialogTitle sx={{ fontWeight: 700 }}>Parâmetros do Termômetro Macro</DialogTitle>
        <DialogContent sx={{ display: 'flex', flexDirection: 'column', gap: 2, pt: 1 }}>
          <Typography variant="body2" sx={{ color: 'text.secondary' }}>
            Ajuste os parâmetros de referência para o cálculo do Drawdown e da retração de Fibonacci. Deixe em branco para utilizar os dados automáticos capturados do Google Finance.
          </Typography>
          <TextField
            label="Máxima do Ano (Pts)"
            type="number"
            placeholder={benchmarkStatus?.altaAno ? `Automático (${benchmarkStatus.altaAno})` : 'Automático (Google Finance)'}
            value={tempSettings.altaAno}
            onChange={(e) => setTempSettings({ ...tempSettings, altaAno: e.target.value })}
          />
          <TextField
            label="Fibo Up (Topo da Onda)"
            type="number"
            placeholder={benchmarkStatus?.fiboUp ? `Automático (${benchmarkStatus.fiboUp})` : 'Automático (Google Finance)'}
            value={tempSettings.fiboUp}
            onChange={(e) => setTempSettings({ ...tempSettings, fiboUp: e.target.value })}
          />
          <TextField
            label="Fibo Down (Fundo da Onda)"
            type="number"
            placeholder={benchmarkStatus?.fiboDown ? `Automático (${benchmarkStatus.fiboDown})` : 'Automático (Google Finance)'}
            value={tempSettings.fiboDown}
            onChange={(e) => setTempSettings({ ...tempSettings, fiboDown: e.target.value })}
          />
        </DialogContent>
        <DialogActions sx={{ p: 2.5, pt: 1, justifyContent: 'space-between' }}>
          <Button
            size="small"
            color="secondary"
            onClick={handleResetMacroSettingsToAuto}
            sx={{ textTransform: 'none' }}
          >
            Usar Automático (Reset)
          </Button>
          <Box sx={{ display: 'flex', gap: 1 }}>
            <Button onClick={() => setOpenSettingsDialog(false)} color="inherit">
              Cancelar
            </Button>
            <Button variant="contained" color="primary" onClick={handleSaveMacroSettings}>
              Salvar Parâmetros
            </Button>
          </Box>
        </DialogActions>
      </Dialog>

      {/* Snackbar de Feedback */}
      <Snackbar
        open={toast.open}
        autoHideDuration={5000}
        onClose={() => setToast({ ...toast, open: false })}
        anchorOrigin={{ vertical: 'bottom', horizontal: 'right' }}
      >
        <Alert
          onClose={() => setToast({ ...toast, open: false })}
          severity={toast.severity}
          variant="filled"
          sx={{ width: '100%', borderRadius: 2 }}
        >
          {toast.message}
        </Alert>
      </Snackbar>
    </Box>
  );
}
