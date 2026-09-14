import React, { useState } from 'react';
import Box from '@mui/material/Box';
import Typography from '@mui/material/Typography';
import Button from '@mui/material/Button';
import Table from '@mui/material/Table';
import TableBody from '@mui/material/TableBody';
import TableCell from '@mui/material/TableCell';
import TableContainer from '@mui/material/TableContainer';
import TableHead from '@mui/material/TableHead';
import TableRow from '@mui/material/TableRow';
import Paper from '@mui/material/Paper';
import Chip from '@mui/material/Chip';
import Pagination from '@mui/material/Pagination';
import FileDownloadIcon from '@mui/icons-material/FileDownload';

export default function PurchasesTable({ purchases, timeSeries }) {
  const [currentPage, setCurrentPage] = useState(1);
  const itemsPerPage = 20;

  if (!purchases || purchases.length === 0) {
    return (
      <Box sx={{ textAlign: 'center', py: 4 }}>
        <Typography variant="body1" sx={{ color: 'text.secondary' }}>
          Nenhuma compra foi realizada no período analisado. Os critérios fixos ou preços teto não foram satisfeitos.
        </Typography>
      </Box>
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
    <Box>
      <Box
        sx={{
          display: 'flex',
          justifyContent: 'space-between',
          alignItems: 'center',
          mb: 2,
          flexWrap: 'wrap',
          gap: 1.5,
        }}
      >
        <Typography variant="h6" sx={{ fontWeight: 700 }}>
          Histórico de Compras Executadas ({purchases.length} compras)
        </Typography>
        <Button
          variant="outlined"
          size="small"
          startIcon={<FileDownloadIcon />}
          onClick={exportToCSV}
        >
          Exportar CSV
        </Button>
      </Box>

      <TableContainer component={Paper} sx={{ borderRadius: 2, border: '1px solid #43474e', mb: 2 }}>
        <Table size="small">
          <TableHead>
            <TableRow>
              <TableCell>Data</TableCell>
              <TableCell>Ticker</TableCell>
              <TableCell>Tipo</TableCell>
              <TableCell>Preço de Compra</TableCell>
              <TableCell>Aporte / Reinvestimento</TableCell>
              <TableCell>Cotas Adquiridas</TableCell>
              <TableCell>Teto Bazin do Ano</TableCell>
              <TableCell>Teto Graham do Ano</TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {currentItems.map((p, index) => (
              <TableRow key={index} hover>
                <TableCell>{p.data}</TableCell>
                <TableCell sx={{ fontWeight: 700, color: 'primary.main' }}>
                  {p.ticker}
                </TableCell>
                <TableCell>
                  {p.isReinvestimento ? (
                    <Chip
                      label="Reinvestimento"
                      color="warning"
                      size="small"
                      sx={{ fontWeight: 600 }}
                    />
                  ) : (
                    <Chip
                      label="Aporte"
                      color="primary"
                      size="small"
                      sx={{ fontWeight: 600 }}
                    />
                  )}
                </TableCell>
                <TableCell>R$ {p.preco?.toFixed(2)}</TableCell>
                <TableCell>R$ {p.valorAportado?.toFixed(2)}</TableCell>
                <TableCell sx={{ fontFamily: "'JetBrains Mono', monospace" }}>
                  {p.cotas?.toFixed(4)}
                </TableCell>
                <TableCell>
                  <Chip
                    label={`R$ ${p.tetoBazin?.toFixed(2)}`}
                    color="success"
                    size="small"
                    variant="outlined"
                  />
                </TableCell>
                <TableCell>
                  <Chip
                    label={`R$ ${p.tetoGraham?.toFixed(2)}`}
                    color="success"
                    size="small"
                    variant="outlined"
                  />
                </TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>
      </TableContainer>

      {totalPages > 1 && (
        <Box
          sx={{
            display: 'flex',
            justifyContent: 'space-between',
            alignItems: 'center',
            pt: 1,
            flexWrap: 'wrap',
            gap: 2,
          }}
        >
          <Typography variant="caption" sx={{ color: 'text.secondary' }}>
            Página {currentPage} de {totalPages}
          </Typography>
          <Pagination
            count={totalPages}
            page={currentPage}
            onChange={(_, page) => setCurrentPage(page)}
            color="primary"
            size="small"
          />
        </Box>
      )}
    </Box>
  );
}


