package com.backtest.backend.infrastructure.adapter.out.persistence;

import com.backtest.backend.domain.entity.FixedCriteria;

public class FixedCriteriaMapper {

    public static FixedCriteriaDocument toDocument(FixedCriteria criteria) {
        if (criteria == null) return null;
        return new FixedCriteriaDocument(criteria.getPlMax(), criteria.getPvpMax(), criteria.getDividaEbitdaMax(), criteria.getRoeMin());
    }

    public static FixedCriteria toDomain(FixedCriteriaDocument doc) {
        if (doc == null) return null;
        return new FixedCriteria(doc.getPlMax(), doc.getPvpMax(), doc.getDividaEbitdaMax(), doc.getRoeMin());
    }
}
