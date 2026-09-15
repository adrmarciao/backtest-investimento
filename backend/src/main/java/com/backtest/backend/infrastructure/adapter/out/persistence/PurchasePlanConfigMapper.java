package com.backtest.backend.infrastructure.adapter.out.persistence;

import com.backtest.backend.domain.entity.PurchasePlanConfig;

public class PurchasePlanConfigMapper {

    public static PurchasePlanConfigDocument toDocument(PurchasePlanConfig domain) {
        if (domain == null) return null;
        return new PurchasePlanConfigDocument(
                domain.getId(),
                domain.getSaldoTotal(),
                domain.getParcelasMeses(),
                domain.getPeriodicidade(),
                domain.getAporteRodadaManual(),
                domain.getBenchmark(),
                domain.getFiboUp(),
                domain.getFiboDown(),
                domain.getAltaAno(),
                domain.getTokenBrapi()
        );
    }

    public static PurchasePlanConfig toDomain(PurchasePlanConfigDocument doc) {
        if (doc == null) return null;
        return new PurchasePlanConfig(
                doc.getId(),
                doc.getSaldoTotal(),
                doc.getParcelasMeses(),
                doc.getPeriodicidade(),
                doc.getAporteRodadaManual(),
                doc.getBenchmark(),
                doc.getFiboUp(),
                doc.getFiboDown(),
                doc.getAltaAno(),
                doc.getTokenBrapi()
        );
    }
}
