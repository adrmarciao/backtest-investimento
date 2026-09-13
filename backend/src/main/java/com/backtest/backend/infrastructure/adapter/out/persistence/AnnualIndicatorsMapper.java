package com.backtest.backend.infrastructure.adapter.out.persistence;

import com.backtest.backend.domain.entity.AnnualIndicators;

public class AnnualIndicatorsMapper {

    public static AnnualIndicatorsDocument toDocument(AnnualIndicators ind) {
        if (ind == null) return null;
        return new AnnualIndicatorsDocument(
                ind.getTicker(), ind.getAno(), ind.getPl(), ind.getPvp(),
                ind.getDividaEbitda(), ind.getRoe(), ind.getDpa(), ind.getLpa(), ind.getVpa()
        );
    }

    public static AnnualIndicators toDomain(AnnualIndicatorsDocument doc) {
        if (doc == null) return null;
        return new AnnualIndicators(
                doc.getTicker(), doc.getAno(), doc.getPl(), doc.getPvp(),
                doc.getDividaEbitda(), doc.getRoe(), doc.getDpa(), doc.getLpa(), doc.getVpa()
        );
    }
}
