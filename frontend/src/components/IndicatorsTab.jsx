import React, { useState, useEffect } from 'react';
import Box from '@mui/material/Box';
import Card from '@mui/material/Card';
import CardContent from '@mui/material/CardContent';
import Typography from '@mui/material/Typography';
import TextField from '@mui/material/TextField';
import Button from '@mui/material/Button';
import FormControl from '@mui/material/FormControl';
import InputLabel from '@mui/material/InputLabel';
import Select from '@mui/material/Select';
import MenuItem from '@mui/material/MenuItem';
import Table from '@mui/material/Table';
import TableBody from '@mui/material/TableBody';
import TableCell from '@mui/material/TableCell';
import TableContainer from '@mui/material/TableContainer';
import TableHead from '@mui/material/TableHead';
import TableRow from '@mui/material/TableRow';
import Paper from '@mui/material/Paper';
import Chip from '@mui/material/Chip';
import IconButton from '@mui/material/IconButton';
import CircularProgress from '@mui/material/CircularProgress';
import SaveIcon from '@mui/icons-material/Save';
import DeleteOutlineIcon from '@mui/icons-material/DeleteOutlined';
import CloseIcon from '@mui/icons-material/Close';
import AddIcon from '@mui/icons-material/Add';
import { getAssets, getIndicators, saveIndicators, updateIndicators, deleteIndicators } from '../services/api';

export default function IndicatorsTab() {
  const [assets, setAssets] = useState([]);
  const [selectedTicker, setSelectedTicker] = useState('');
  const [originalList, setOriginalList] = useState([]);
  const [draftList, setDraftList] = useState([]);

  // Form state for quick year entry
  const [ano, setAno] = useState(new Date().getFullYear() - 1);
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    getAssets()
      .then((res) => {
        setAssets(res.data);
        if (res.data.length > 0) {
          setSelectedTicker(res.data[0].ticker);
        }
      })
      .catch(console.error);
  }, []);

  const loadIndicators = async (ticker) => {
    if (!ticker) return;
    try {
      setLoading(true);
      const res = await getIndicators(ticker);
      const data = res.data || [];
      setOriginalList(data);
      setDraftList(
        data.map((item) => ({
          ...item,
          pl: item.pl ?? '',
          pvp: item.pvp ?? '',
          dividaEbitda: item.dividaEbitda ?? '',
          roe: item.roe ?? '',
          dpa: item.dpa ?? '',
          lpa: item.lpa ?? '',
          vpa: item.vpa ?? '',
          isDraft: false,
          isDirty: false,
        }))
      );
    } catch (err) {
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    if (selectedTicker) {
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

  const handleCellChange = (targetYear, field, value) => {
    setDraftList((prev) =>
      prev.map((item) => {
        if (item.ano === targetYear) {
          return {
            ...item,
            [field]: value,
            isDirty: true,
          };
        }
        return item;
      })
    );
  };

  const handleAddYear = (e) => {
    if (e) e.preventDefault();
    if (!selectedTicker) return alert('Selecione um ativo');
    const yearNum = parseInt(ano, 10);
    if (isNaN(yearNum) || yearNum < 1900 || yearNum > 2100) {
      return alert('Ano inválido');
    }

    const existing = draftList.find((ind) => ind.ano === yearNum);
    if (existing) {
      return alert(`O ano ${yearNum} já está presente na lista.`);
    }

    const newDraftItem = {
      ticker: selectedTicker,
      ano: yearNum,
      pl: '',
      pvp: '',
      dividaEbitda: '',
      roe: '',
      dpa: '',
      lpa: '',
      vpa: '',
      isDraft: true,
      isDirty: true,
    };

    setDraftList((prev) => [newDraftItem, ...prev].sort((a, b) => b.ano - a.ano));
  };

  const handleDeleteYear = async (yearToDelete) => {
    const itemToDelete = draftList.find((ind) => ind.ano === yearToDelete);
    if (itemToDelete && itemToDelete.isDraft) {
      // Remove from draft directly
      setDraftList((prev) => prev.filter((ind) => ind.ano !== yearToDelete));
      return;
    }

    if (!window.confirm(`Excluir indicadores do ano ${yearToDelete}?`)) return;
    try {
      setLoading(true);
      await deleteIndicators(selectedTicker, yearToDelete);
      await loadIndicators(selectedTicker);
    } catch (err) {
      alert('Erro ao excluir indicadores');
    } finally {
      setLoading(false);
    }
  };

  const parseNum = (val) => {
    if (val === '' || val === null || val === undefined) return null;
    const num = parseFloat(val);
    return isNaN(num) ? null : num;
  };

  const pendingItems = draftList.filter((item) => item.isDraft || item.isDirty);
  const pendingCount = pendingItems.length;

  const handleApplyChanges = async () => {
    if (pendingCount === 0) return;
    setLoading(true);
    try {
      const promises = pendingItems.map((item) => {
        const payload = {
          ticker: selectedTicker,
          ano: parseInt(item.ano, 10),
          pl: parseNum(item.pl),
          pvp: parseNum(item.pvp),
          dividaEbitda: parseNum(item.dividaEbitda),
          roe: parseNum(item.roe),
          dpa: parseNum(item.dpa),
          lpa: parseNum(item.lpa),
          vpa: parseNum(item.vpa),
        };
        if (item.isDraft) {
          return saveIndicators(selectedTicker, payload);
        } else {
          return updateIndicators(selectedTicker, item.ano, payload);
        }
      });

      await Promise.all(promises);
      await loadIndicators(selectedTicker);
    } catch (err) {
      console.error(err);
      alert('Erro ao aplicar algumas alterações de indicadores. Tente novamente.');
    } finally {
      setLoading(false);
    }
  };

  const handleDiscardChanges = () => {
    setDraftList(
      originalList.map((item) => ({
        ...item,
        pl: item.pl ?? '',
        pvp: item.pvp ?? '',
        dividaEbitda: item.dividaEbitda ?? '',
        roe: item.roe ?? '',
        dpa: item.dpa ?? '',
        lpa: item.lpa ?? '',
        vpa: item.vpa ?? '',
        isDraft: false,
        isDirty: false,
      }))
    );
  };

  return (
    <Card sx={{ p: 1 }}>
      <CardContent>
        <Typography variant="h5" component="h2" sx={{ fontWeight: 700, mb: 1 }}>
          Indicadores Fundamentalistas por Ano
        </Typography>
        <Typography variant="body2" sx={{ color: 'text.secondary', mb: 3 }}>
          Insira e gerencie o histórico de balanços anuais auditados para cálculo dos Tetos Bazin e Graham.
        </Typography>

        <FormControl size="small" sx={{ minWidth: 260, mb: 4 }}>
          <InputLabel id="ticker-select-label">Selecione o Ativo</InputLabel>
          <Select
            labelId="ticker-select-label"
            label="Selecione o Ativo"
            value={selectedTicker}
            onChange={(e) => setSelectedTicker(e.target.value)}
          >
            {assets.map((a) => (
              <MenuItem key={a.ticker} value={a.ticker}>
                {a.ticker} (Aporte R$ {a.valorAporte})
              </MenuItem>
            ))}
          </Select>
        </FormControl>

        {selectedTicker && (
          <>
            <Box
              component="form"
              onSubmit={handleAddYear}
              sx={{
                backgroundColor: '#171c26',
                p: 2.5,
                borderRadius: 3,
                border: '1px solid #43474e',
                mb: 4,
                display: 'flex',
                alignItems: 'center',
                gap: 2,
                flexWrap: 'wrap',
              }}
            >
              <Typography variant="subtitle1" sx={{ fontWeight: 700 }}>
                Adicionar Novo Ano para {selectedTicker}:
              </Typography>
              <TextField
                label="Ano"
                type="number"
                size="small"
                value={ano}
                onChange={(e) => setAno(e.target.value)}
                sx={{ width: 130 }}
                required
              />
              <Button type="submit" variant="contained" startIcon={<AddIcon />}>
                Adicionar Ano à Tabela
              </Button>
            </Box>

            <Box
              sx={{
                display: 'flex',
                justifyContent: 'space-between',
                alignItems: 'center',
                flexWrap: 'wrap',
                gap: 2,
                mb: 2,
              }}
            >
              <Box sx={{ display: 'flex', alignItems: 'center', gap: 1.5 }}>
                <Typography variant="h6" sx={{ fontWeight: 700 }}>
                  Histórico Fundamentalista
                </Typography>
                {pendingCount > 0 && (
                  <Chip
                    label={`${pendingCount} alterações pendentes`}
                    color="warning"
                    size="small"
                    variant="outlined"
                  />
                )}
              </Box>

              <Box sx={{ display: 'flex', gap: 1.5 }}>
                <Button
                  variant="outlined"
                  color="inherit"
                  disabled={pendingCount === 0 || loading}
                  startIcon={<CloseIcon />}
                  onClick={handleDiscardChanges}
                >
                  Descartar
                </Button>
                <Button
                  variant="contained"
                  color="success"
                  disabled={pendingCount === 0 || loading}
                  startIcon={
                    loading ? <CircularProgress size={18} color="inherit" /> : <SaveIcon />
                  }
                  onClick={handleApplyChanges}
                >
                  {loading ? 'Aplicando...' : `Aplicar Alterações (${pendingCount})`}
                </Button>
              </Box>
            </Box>

            <TableContainer component={Paper} sx={{ borderRadius: 2, border: '1px solid #43474e' }}>
              <Table size="small">
                <TableHead>
                  <TableRow>
                    <TableCell>Status</TableCell>
                    <TableCell>Ano</TableCell>
                    <TableCell>P/L</TableCell>
                    <TableCell>P/VP</TableCell>
                    <TableCell>Dív/EBITDA</TableCell>
                    <TableCell>ROE (%)</TableCell>
                    <TableCell>DPA (R$)</TableCell>
                    <TableCell>LPA (R$)</TableCell>
                    <TableCell>VPA (R$)</TableCell>
                    <TableCell>Teto Bazin</TableCell>
                    <TableCell>Teto Graham</TableCell>
                    <TableCell align="right">Ações</TableCell>
                  </TableRow>
                </TableHead>
                <TableBody>
                  {draftList.length === 0 ? (
                    <TableRow>
                      <TableCell colSpan={12} align="center" sx={{ py: 4, color: 'text.secondary' }}>
                        Nenhum ano cadastrado para este ativo.
                      </TableCell>
                    </TableRow>
                  ) : (
                    draftList.map((ind) => {
                      return (
                        <TableRow key={ind.ano} hover>
                          <TableCell>
                            {ind.isDraft ? (
                              <Chip label="Novo" color="success" size="small" />
                            ) : ind.isDirty ? (
                              <Chip label="Modificado" color="warning" size="small" />
                            ) : (
                              <Chip label="Salvo" color="default" size="small" variant="outlined" />
                            )}
                          </TableCell>

                          <TableCell sx={{ fontWeight: 700 }}>{ind.ano}</TableCell>

                          <TableCell>
                            <TextField
                              size="small"
                              type="number"
                              inputProps={{ step: '0.01' }}
                              value={ind.pl}
                              onChange={(e) => handleCellChange(ind.ano, 'pl', e.target.value)}
                              sx={{ width: 75 }}
                              placeholder="-"
                            />
                          </TableCell>

                          <TableCell>
                            <TextField
                              size="small"
                              type="number"
                              inputProps={{ step: '0.01' }}
                              value={ind.pvp}
                              onChange={(e) => handleCellChange(ind.ano, 'pvp', e.target.value)}
                              sx={{ width: 75 }}
                              placeholder="-"
                            />
                          </TableCell>

                          <TableCell>
                            <TextField
                              size="small"
                              type="number"
                              inputProps={{ step: '0.01' }}
                              value={ind.dividaEbitda}
                              onChange={(e) =>
                                handleCellChange(ind.ano, 'dividaEbitda', e.target.value)
                              }
                              sx={{ width: 75 }}
                              placeholder="-"
                            />
                          </TableCell>

                          <TableCell>
                            <TextField
                              size="small"
                              type="number"
                              inputProps={{ step: '0.01' }}
                              value={ind.roe}
                              onChange={(e) => handleCellChange(ind.ano, 'roe', e.target.value)}
                              sx={{ width: 75 }}
                              placeholder="-"
                            />
                          </TableCell>

                          <TableCell>
                            <TextField
                              size="small"
                              type="number"
                              inputProps={{ step: '0.01' }}
                              value={ind.dpa}
                              onChange={(e) => handleCellChange(ind.ano, 'dpa', e.target.value)}
                              sx={{ width: 75 }}
                              placeholder="-"
                            />
                          </TableCell>

                          <TableCell>
                            <TextField
                              size="small"
                              type="number"
                              inputProps={{ step: '0.01' }}
                              value={ind.lpa}
                              onChange={(e) => handleCellChange(ind.ano, 'lpa', e.target.value)}
                              sx={{ width: 75 }}
                              placeholder="-"
                            />
                          </TableCell>

                          <TableCell>
                            <TextField
                              size="small"
                              type="number"
                              inputProps={{ step: '0.01' }}
                              value={ind.vpa}
                              onChange={(e) => handleCellChange(ind.ano, 'vpa', e.target.value)}
                              sx={{ width: 75 }}
                              placeholder="-"
                            />
                          </TableCell>

                          <TableCell>
                            <Chip
                              label={calculateBazinPreview(ind.dpa)}
                              color="success"
                              size="small"
                              variant="outlined"
                            />
                          </TableCell>

                          <TableCell>
                            <Chip
                              label={calculateGrahamPreview(ind.lpa, ind.vpa)}
                              color="success"
                              size="small"
                              variant="outlined"
                            />
                          </TableCell>

                          <TableCell align="right">
                            <IconButton
                              size="small"
                              color="error"
                              onClick={() => handleDeleteYear(ind.ano)}
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
          </>
        )}
      </CardContent>
    </Card>
  );
}
