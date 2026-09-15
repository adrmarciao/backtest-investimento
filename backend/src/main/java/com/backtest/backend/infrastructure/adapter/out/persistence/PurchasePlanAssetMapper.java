package com.backtest.backend.infrastructure.adapter.out.persistence;

import com.backtest.backend.domain.entity.PurchasePlanAsset;

public class PurchasePlanAssetMapper {

    public static PurchasePlanAssetDocument toDocument(PurchasePlanAsset domain) {
        if (domain == null) return null;
        return new PurchasePlanAssetDocument(
                domain.getId(),
                domain.getTicker(),
                domain.getSetor(),
                domain.getPeso(),
                domain.getHabilitado(),
                domain.getPrecoAtual(),
                domain.getDpaForecast(),
                domain.getTetoBazin(),
                domain.getLpa(),
                domain.getVpa(),
                domain.getPrecoGraham(),
                domain.getMargemBazin(),
                domain.getAjusteManualQtd(),
                domain.getOrdem()
        );
    }

    public static PurchasePlanAsset toDomain(PurchasePlanAssetDocument doc) {
        if (doc == null) return null;
        return new PurchasePlanAsset(
                doc.getId(),
                doc.getTicker(),
                doc.getSetor(),
                doc.getPeso(),
                doc.getHabilitado(),
                doc.getPrecoAtual(),
                doc.getDpaForecast(),
                doc.getTetoBazin(),
                doc.getLpa(),
                doc.getVpa(),
                doc.getPrecoGraham(),
                doc.getMargemBazin(),
                doc.getAjusteManualQtd(),
                doc.getOrdem()
        );
    }
}
