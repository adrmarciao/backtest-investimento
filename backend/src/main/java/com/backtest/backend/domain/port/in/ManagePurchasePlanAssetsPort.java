package com.backtest.backend.domain.port.in;

import com.backtest.backend.domain.entity.BenchmarkStatus;
import com.backtest.backend.domain.entity.PurchasePlanAsset;
import com.backtest.backend.domain.entity.RoundAllocationResult;

import java.util.List;

public interface ManagePurchasePlanAssetsPort {
    List<PurchasePlanAsset> getAllAssets();
    List<PurchasePlanAsset> saveAllAssets(List<PurchasePlanAsset> assets);
    PurchasePlanAsset saveAsset(PurchasePlanAsset asset);
    void deleteAsset(String ticker);
    List<PurchasePlanAsset> syncQuotes();
    List<PurchasePlanAsset> syncFundamentals(String token);
    RoundAllocationResult calculateAllocation();
    BenchmarkStatus getBenchmarkStatus(String benchmark);
}
