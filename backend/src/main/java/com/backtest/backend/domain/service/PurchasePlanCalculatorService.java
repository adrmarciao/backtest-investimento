package com.backtest.backend.domain.service;

import com.backtest.backend.domain.entity.*;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class PurchasePlanCalculatorService {

    public RoundAllocationResult calculateAllocation(PurchasePlanConfig config, List<PurchasePlanAsset> assets) {
        BigDecimal aporteRodada = config != null ? config.calculateAporteRodada() : BigDecimal.ZERO;
        boolean isManual = config != null && config.getAporteRodadaManual() != null
                && config.getAporteRodadaManual().compareTo(BigDecimal.ZERO) > 0;

        List<PurchasePlanAsset> activeAssets = (assets != null) ? assets : new ArrayList<>();

        BigDecimal somaPesosEfetivos = activeAssets.stream()
                .filter(a -> a.getPrecoAtual() != null && a.getPrecoAtual().compareTo(BigDecimal.ZERO) > 0)
                .map(PurchasePlanAsset::getEffectiveWeight)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        List<AssetAllocationItem> itens = new ArrayList<>();
        BigDecimal totalGasto = BigDecimal.ZERO;
        int totalAcoesCompradas = 0;

        for (PurchasePlanAsset asset : activeAssets) {
            BigDecimal preco = asset.getPrecoAtual();
            BigDecimal pesoEfetivo = asset.getEffectiveWeight();
            BigDecimal percentualAlocado = BigDecimal.ZERO;
            BigDecimal valorAlocado = BigDecimal.ZERO;
            int qtdSugerida = 0;

            if (preco != null && preco.compareTo(BigDecimal.ZERO) > 0
                    && somaPesosEfetivos.compareTo(BigDecimal.ZERO) > 0
                    && pesoEfetivo.compareTo(BigDecimal.ZERO) > 0) {
                percentualAlocado = pesoEfetivo.divide(somaPesosEfetivos, 6, RoundingMode.HALF_UP);
                valorAlocado = aporteRodada.multiply(percentualAlocado).setScale(2, RoundingMode.HALF_UP);
                qtdSugerida = valorAlocado.divide(preco, 0, RoundingMode.FLOOR).intValue();
            }

            boolean isHabilitado = asset.isHabilitado();
            int ajuste = isHabilitado ? asset.getAjusteManualQtd() : 0;
            int qtdFinal = isHabilitado ? Math.max(0, qtdSugerida + ajuste) : 0;
            BigDecimal itemTotalGasto = (isHabilitado && preco != null && preco.compareTo(BigDecimal.ZERO) > 0)
                    ? preco.multiply(BigDecimal.valueOf(qtdFinal)).setScale(2, RoundingMode.HALF_UP)
                    : BigDecimal.ZERO;

            totalGasto = totalGasto.add(itemTotalGasto);
            totalAcoesCompradas += qtdFinal;

            BigDecimal pctExibicao = percentualAlocado.multiply(BigDecimal.valueOf(100)).setScale(2, RoundingMode.HALF_UP);

            itens.add(new AssetAllocationItem(
                    asset.getTicker(),
                    asset.getSetor(),
                    preco,
                    pesoEfetivo,
                    pctExibicao,
                    valorAlocado,
                    qtdSugerida,
                    ajuste,
                    qtdFinal,
                    itemTotalGasto,
                    asset.getMargemBazin(),
                    asset.getTetoBazin(),
                    asset.getPrecoGraham()
            ));
        }

        BigDecimal sobraCaixa = aporteRodada.subtract(totalGasto).setScale(2, RoundingMode.HALF_UP);
        String boleta = buildBoletaTexto(itens, aporteRodada, totalGasto, sobraCaixa);

        return new RoundAllocationResult(
                config != null ? config.getSaldoTotal() : BigDecimal.ZERO,
                aporteRodada,
                isManual,
                totalGasto,
                sobraCaixa,
                totalAcoesCompradas,
                itens,
                boleta
        );
    }

    public BenchmarkStatus calculateBenchmarkStatus(String benchmark, BigDecimal precoAtual, BigDecimal altaAno,
                                                    BigDecimal fiboUp, BigDecimal fiboDown) {
        String bm = (benchmark != null && !benchmark.isBlank()) ? benchmark.trim().toUpperCase() : "IBOV";
        BigDecimal drawdown = null;
        if (precoAtual != null && altaAno != null && altaAno.compareTo(BigDecimal.ZERO) > 0) {
            drawdown = precoAtual.subtract(altaAno)
                    .divide(altaAno, 4, RoundingMode.HALF_UP)
                    .multiply(BigDecimal.valueOf(100))
                    .setScale(2, RoundingMode.HALF_UP);
        }

        BigDecimal fiboRetraction = null;
        if (precoAtual != null && fiboUp != null && fiboDown != null && fiboUp.compareTo(fiboDown) > 0) {
            BigDecimal amplitude = fiboUp.subtract(fiboDown);
            fiboRetraction = fiboUp.subtract(precoAtual)
                    .divide(amplitude, 4, RoundingMode.HALF_UP)
                    .multiply(BigDecimal.valueOf(100))
                    .setScale(2, RoundingMode.HALF_UP);
        }

        String descricao = formatStatusDescricao(bm, precoAtual, drawdown, fiboRetraction);

        return new BenchmarkStatus(
                bm,
                precoAtual,
                altaAno,
                drawdown,
                fiboUp,
                fiboDown,
                fiboRetraction,
                descricao
        );
    }

    private String formatStatusDescricao(String benchmark, BigDecimal preco, BigDecimal dd, BigDecimal fibo) {
        if (preco == null) return "Cotação do benchmark indisponível.";
        StringBuilder sb = new StringBuilder(benchmark).append(" em ").append(preco);
        if (dd != null) {
            sb.append(" | Drawdown: ").append(dd).append("%");
        }
        if (fibo != null) {
            sb.append(" | Fibo: ").append(fibo).append("% de retração");
        }
        return sb.toString();
    }

    private String buildBoletaTexto(List<AssetAllocationItem> itens, BigDecimal aporte, BigDecimal totalGasto, BigDecimal sobra) {
        StringBuilder sb = new StringBuilder();
        sb.append("=== BOLETA DE ORDENS - PLANO DE COMPRAS ===\n");
        sb.append("Data: ").append(LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))).append("\n");
        sb.append(String.format("Aporte da Rodada: R$ %,.2f\n", aporte != null ? aporte : BigDecimal.ZERO));
        sb.append("--------------------------------------------------\n");
        sb.append(String.format("%-8s | %-6s | %-12s | %-12s\n", "TICKER", "QTD", "PREÇO", "TOTAL"));
        sb.append("--------------------------------------------------\n");

        for (AssetAllocationItem item : itens) {
            if (item.qtdFinal() > 0) {
                sb.append(String.format("%-8s | %-6d | R$ %,9.2f | R$ %,9.2f\n",
                        item.ticker(),
                        item.qtdFinal(),
                        item.precoAtual() != null ? item.precoAtual() : BigDecimal.ZERO,
                        item.totalGasto()
                ));
            }
        }

        sb.append("--------------------------------------------------\n");
        sb.append(String.format("Total a Pagar: R$ %,.2f\n", totalGasto != null ? totalGasto : BigDecimal.ZERO));
        sb.append(String.format("Sobra de Caixa: R$ %,.2f\n", sobra != null ? sobra : BigDecimal.ZERO));
        sb.append("==================================================");
        return sb.toString();
    }
}
