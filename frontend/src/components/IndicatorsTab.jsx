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
import EditIcon from '@mui/icons-material/Edit';
import DeleteOutlineIcon from '@mui/icons-material/DeleteOutlined';
import CheckIcon from '@mui/icons-material/Check';
import CloseIcon from '@mui/icons-material/Close';
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
      setPl('');
      setPvp('');
      setDividaEbitda('');
      setRoe('');
      setDpa('');
      setLpa('');
      setVpa('');
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
              onSubmit={handleSubmit}
              sx={{
                backgroundColor: '#171c26',
                p: 3,
                borderRadius: 3,
                border: '1px solid #43474e',
                mb: 4,
              }}
            >
              <Typography variant="subtitle1" sx={{ fontWeight: 700, mb: 2 }}>
                Cadastrar/Editar Ano para {selectedTicker}
              </Typography>

              <Box
                sx={{
                  display: 'grid',
                  gridTemplateColumns: {
                    xs: 'repeat(2, 1fr)',
                    sm: 'repeat(4, 1fr)',
                    md: 'repeat(8, 1fr)',
                  },
                  gap: 1.5,
                  mb: 2,
                }}
              >
                <TextField
                  label="Ano"
                  type="number"
                  size="small"
                  value={ano}
                  onChange={(e) => setAno(e.target.value)}
                  required
                />
                <TextField
                  label="P/L"
                  type="number"
                  inputProps={{ step: '0.01' }}
                  size="small"
                  value={pl}
                  onChange={(e) => setPl(e.target.value)}
                  placeholder="12.5"
                  required
                />
                <TextField
                  label="P/VP"
                  type="number"
                  inputProps={{ step: '0.01' }}
                  size="small"
                  value={pvp}
                  onChange={(e) => setPvp(e.target.value)}
                  placeholder="2.1"
                  required
                />
                <TextField
                  label="Dív/EBITDA"
                  type="number"
                  inputProps={{ step: '0.01' }}
                  size="small"
                  value={dividaEbitda}
                  onChange={(e) => setDividaEbitda(e.target.value)}
                  placeholder="1.4"
                  required
                />
                <TextField
                  label="ROE (%)"
                  type="number"
                  inputProps={{ step: '0.01' }}
                  size="small"
                  value={roe}
                  onChange={(e) => setRoe(e.target.value)}
                  placeholder="22.0"
                  required
                />
                <TextField
                  label="DPA (R$)"
                  type="number"
                  inputProps={{ step: '0.01' }}
                  size="small"
                  value={dpa}
                  onChange={(e) => setDpa(e.target.value)}
                  placeholder="1.50"
                  required
                />
                <TextField
                  label="LPA (R$)"
                  type="number"
                  inputProps={{ step: '0.01' }}
                  size="small"
                  value={lpa}
                  onChange={(e) => setLpa(e.target.value)}
                  placeholder="3.20"
                  required
                />
                <TextField
                  label="VPA (R$)"
                  type="number"
                  inputProps={{ step: '0.01' }}
                  size="small"
                  value={vpa}
                  onChange={(e) => setVpa(e.target.value)}
                  placeholder="18.00"
                  required
                />
              </Box>

              <Box
                sx={{
                  display: 'flex',
                  alignItems: 'center',
                  justifyContent: 'space-between',
                  flexWrap: 'wrap',
                  gap: 2,
                }}
              >
                <Typography variant="body2" sx={{ color: 'text.secondary' }}>
                  <strong>Tetos Calculados:</strong> Bazin:{' '}
                  <span style={{ color: '#10b981', fontWeight: 600 }}>
                    {calculateBazinPreview(dpa)}
                  </span>{' '}
                  | Graham:{' '}
                  <span style={{ color: '#10b981', fontWeight: 600 }}>
                    {calculateGrahamPreview(lpa, vpa)}
                  </span>
                </Typography>
                <Button
                  type="submit"
                  variant="contained"
                  disabled={loading}
                  startIcon={loading ? <CircularProgress size={18} color="inherit" /> : <SaveIcon />}
                >
                  {loading ? 'Salvando...' : 'Salvar Indicadores do Ano'}
                </Button>
              </Box>
            </Box>

            <Typography variant="h6" sx={{ fontWeight: 700, mb: 2 }}>
              Histórico Fundamentalista Cadastrado
            </Typography>

            <TableContainer component={Paper} sx={{ borderRadius: 2, border: '1px solid #43474e' }}>
              <Table size="small">
                <TableHead>
                  <TableRow>
                    <TableCell>Ano</TableCell>
                    <TableCell>P/L</TableCell>
                    <TableCell>P/VP</TableCell>
                    <TableCell>Dív/EBITDA</TableCell>
                    <TableCell>ROE</TableCell>
                    <TableCell>DPA</TableCell>
                    <TableCell>LPA</TableCell>
                    <TableCell>VPA</TableCell>
                    <TableCell>Teto Bazin</TableCell>
                    <TableCell>Teto Graham</TableCell>
                    <TableCell align="right">Ações</TableCell>
                  </TableRow>
                </TableHead>
                <TableBody>
                  {indicatorsList.length === 0 ? (
                    <TableRow>
                      <TableCell colSpan={11} align="center" sx={{ py: 4, color: 'text.secondary' }}>
                        Nenhum ano cadastrado para este ativo.
                      </TableCell>
                    </TableRow>
                  ) : (
                    indicatorsList.map((ind) => {
                      const isEditing = ind.ano === editingYear;
                      return (
                        <TableRow key={ind.ano} hover>
                          <TableCell sx={{ fontWeight: 700 }}>{ind.ano}</TableCell>
                          {isEditing ? (
                            <>
                              <TableCell>
                                <TextField
                                  size="small"
                                  type="number"
                                  inputProps={{ step: '0.01' }}
                                  value={editForm.pl}
                                  onChange={(e) => handleEditInputChange('pl', e.target.value)}
                                  sx={{ width: 75 }}
                                />
                              </TableCell>
                              <TableCell>
                                <TextField
                                  size="small"
                                  type="number"
                                  inputProps={{ step: '0.01' }}
                                  value={editForm.pvp}
                                  onChange={(e) => handleEditInputChange('pvp', e.target.value)}
                                  sx={{ width: 75 }}
                                />
                              </TableCell>
                              <TableCell>
                                <TextField
                                  size="small"
                                  type="number"
                                  inputProps={{ step: '0.01' }}
                                  value={editForm.dividaEbitda}
                                  onChange={(e) =>
                                    handleEditInputChange('dividaEbitda', e.target.value)
                                  }
                                  sx={{ width: 75 }}
                                />
                              </TableCell>
                              <TableCell>
                                <TextField
                                  size="small"
                                  type="number"
                                  inputProps={{ step: '0.01' }}
                                  value={editForm.roe}
                                  onChange={(e) => handleEditInputChange('roe', e.target.value)}
                                  sx={{ width: 75 }}
                                />
                              </TableCell>
                              <TableCell>
                                <TextField
                                  size="small"
                                  type="number"
                                  inputProps={{ step: '0.01' }}
                                  value={editForm.dpa}
                                  onChange={(e) => handleEditInputChange('dpa', e.target.value)}
                                  sx={{ width: 75 }}
                                />
                              </TableCell>
                              <TableCell>
                                <TextField
                                  size="small"
                                  type="number"
                                  inputProps={{ step: '0.01' }}
                                  value={editForm.lpa}
                                  onChange={(e) => handleEditInputChange('lpa', e.target.value)}
                                  sx={{ width: 75 }}
                                />
                              </TableCell>
                              <TableCell>
                                <TextField
                                  size="small"
                                  type="number"
                                  inputProps={{ step: '0.01' }}
                                  value={editForm.vpa}
                                  onChange={(e) => handleEditInputChange('vpa', e.target.value)}
                                  sx={{ width: 75 }}
                                />
                              </TableCell>
                              <TableCell>
                                <Chip
                                  label={calculateBazinPreview(editForm.dpa)}
                                  color="success"
                                  size="small"
                                  variant="outlined"
                                />
                              </TableCell>
                              <TableCell>
                                <Chip
                                  label={calculateGrahamPreview(editForm.lpa, editForm.vpa)}
                                  color="success"
                                  size="small"
                                  variant="outlined"
                                />
                              </TableCell>
                              <TableCell align="right">
                                <Box sx={{ display: 'flex', gap: 0.5, justifyContent: 'flex-end' }}>
                                  <IconButton
                                    size="small"
                                    color="primary"
                                    onClick={() => handleSaveEdit(ind.ano)}
                                  >
                                    <CheckIcon fontSize="small" />
                                  </IconButton>
                                  <IconButton
                                    size="small"
                                    color="inherit"
                                    onClick={handleCancelEdit}
                                  >
                                    <CloseIcon fontSize="small" />
                                  </IconButton>
                                </Box>
                              </TableCell>
                            </>
                          ) : (
                            <>
                              <TableCell>{ind.pl}</TableCell>
                              <TableCell>{ind.pvp}</TableCell>
                              <TableCell>{ind.dividaEbitda}</TableCell>
                              <TableCell>{ind.roe}%</TableCell>
                              <TableCell>R$ {ind.dpa}</TableCell>
                              <TableCell>R$ {ind.lpa}</TableCell>
                              <TableCell>R$ {ind.vpa}</TableCell>
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
                                <Box sx={{ display: 'flex', gap: 0.5, justifyContent: 'flex-end' }}>
                                  <IconButton
                                    size="small"
                                    color="primary"
                                    onClick={() => handleStartEdit(ind)}
                                  >
                                    <EditIcon fontSize="small" />
                                  </IconButton>
                                  <IconButton
                                    size="small"
                                    color="error"
                                    onClick={() => handleDeleteYear(ind.ano)}
                                  >
                                    <DeleteOutlineIcon fontSize="small" />
                                  </IconButton>
                                </Box>
                              </TableCell>
                            </>
                          )}
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


