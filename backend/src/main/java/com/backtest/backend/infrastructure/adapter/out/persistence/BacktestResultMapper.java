package com.backtest.backend.infrastructure.adapter.out.persistence;

import com.backtest.backend.domain.entity.BacktestResult;

public class BacktestResultMapper {

    public static BacktestResultDocument toDocument(BacktestResult domain) {
        if (domain == null) return null;
        BacktestResultDocument doc = new BacktestResultDocument();
        doc.setId(domain.getId());
        doc.setDataExecucao(domain.getDataExecucao());
        doc.setCompras(domain.getCompras());
        doc.setSerieTemporal(domain.getSerieTemporal());
        doc.setResumoAtivos(domain.getResumoAtivos());
        doc.setAnosIgnorados(domain.getAnosIgnorados());
        doc.setTotalAportado(domain.getTotalAportado());
        doc.setValorFinal(domain.getValorFinal());
        doc.setRetornoTotal(domain.getRetornoTotal());
        doc.setCagr(domain.getCagr());
        doc.setMaxDrawdown(domain.getMaxDrawdown());
        doc.setSharpeRatio(domain.getSharpeRatio());
        doc.setAlfaIbov(domain.getAlfaIbov());
        doc.setTotalDividendosRecebidos(domain.getTotalDividendosRecebidos());
        doc.setTotalDividendosReinvestidos(domain.getTotalDividendosReinvestidos());
        doc.setSaldoCaixaDividendos(domain.getSaldoCaixaDividendos());
        return doc;
    }

    public static BacktestResult toDomain(BacktestResultDocument doc) {
        if (doc == null) return null;
        BacktestResult domain = new BacktestResult();
        domain.setId(doc.getId());
        domain.setDataExecucao(doc.getDataExecucao());
        domain.setCompras(doc.getCompras());
        domain.setSerieTemporal(doc.getSerieTemporal());
        domain.setResumoAtivos(doc.getResumoAtivos());
        domain.setAnosIgnorados(doc.getAnosIgnorados());
        domain.setTotalAportado(doc.getTotalAportado());
        domain.setValorFinal(doc.getValorFinal());
        domain.setRetornoTotal(doc.getRetornoTotal());
        domain.setCagr(doc.getCagr());
        domain.setMaxDrawdown(doc.getMaxDrawdown());
        domain.setSharpeRatio(doc.getSharpeRatio());
        domain.setAlfaIbov(doc.getAlfaIbov());
        domain.setTotalDividendosRecebidos(doc.getTotalDividendosRecebidos());
        domain.setTotalDividendosReinvestidos(doc.getTotalDividendosReinvestidos());
        domain.setSaldoCaixaDividendos(doc.getSaldoCaixaDividendos());
        return domain;
    }
}

