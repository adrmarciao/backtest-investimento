package com.backtest.backend.domain.port.out;

import com.backtest.backend.domain.entity.PurchasePlanConfig;
import java.util.Optional;

public interface PurchasePlanConfigRepositoryPort {
    PurchasePlanConfig save(PurchasePlanConfig config);
    Optional<PurchasePlanConfig> findDefault();
}
