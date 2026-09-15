package com.backtest.backend.domain.port.in;

import com.backtest.backend.domain.entity.PurchasePlanConfig;

public interface ManagePurchasePlanConfigPort {
    PurchasePlanConfig getConfig();
    PurchasePlanConfig saveConfig(PurchasePlanConfig config);
}
