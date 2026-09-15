package com.backtest.backend.infrastructure.adapter.in.rest;

import com.backtest.backend.domain.entity.BenchmarkStatus;
import com.backtest.backend.domain.entity.PurchasePlanAsset;
import com.backtest.backend.domain.entity.PurchasePlanConfig;
import com.backtest.backend.domain.entity.RoundAllocationResult;
import com.backtest.backend.domain.port.in.ManagePurchasePlanAssetsPort;
import com.backtest.backend.domain.port.in.ManagePurchasePlanConfigPort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/purchase-plan")
public class PurchasePlanController {

    private final ManagePurchasePlanConfigPort configPort;
    private final ManagePurchasePlanAssetsPort assetsPort;

    public PurchasePlanController(ManagePurchasePlanConfigPort configPort, ManagePurchasePlanAssetsPort assetsPort) {
        this.configPort = configPort;
        this.assetsPort = assetsPort;
    }

    @GetMapping("/config")
    public ResponseEntity<PurchasePlanConfig> getConfig() {
        return ResponseEntity.ok(configPort.getConfig());
    }

    @PutMapping("/config")
    public ResponseEntity<PurchasePlanConfig> saveConfig(@RequestBody PurchasePlanConfig config) {
        return ResponseEntity.ok(configPort.saveConfig(config));
    }

    @GetMapping("/assets")
    public ResponseEntity<List<PurchasePlanAsset>> getAssets() {
        return ResponseEntity.ok(assetsPort.getAllAssets());
    }

    @PutMapping("/assets")
    public ResponseEntity<List<PurchasePlanAsset>> saveAssets(@RequestBody List<PurchasePlanAsset> assets) {
        return ResponseEntity.ok(assetsPort.saveAllAssets(assets));
    }

    @PostMapping("/assets")
    public ResponseEntity<?> saveSingleAsset(@RequestBody PurchasePlanAsset asset) {
        try {
            return ResponseEntity.ok(assetsPort.saveAsset(asset));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/assets/{ticker}")
    public ResponseEntity<Void> deleteAsset(@PathVariable String ticker) {
        assetsPort.deleteAsset(ticker);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/sync-quotes")
    public ResponseEntity<List<PurchasePlanAsset>> syncQuotes() {
        return ResponseEntity.ok(assetsPort.syncQuotes());
    }

    @PostMapping("/sync-fundamentals")
    public ResponseEntity<?> syncFundamentals(@RequestBody(required = false) SyncFundamentalsRequest request) {
        try {
            String token = (request != null) ? request.token() : null;
            return ResponseEntity.ok(assetsPort.syncFundamentals(token));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/benchmark-status")
    public ResponseEntity<BenchmarkStatus> getBenchmarkStatus(@RequestParam(defaultValue = "IBOV") String benchmark) {
        return ResponseEntity.ok(assetsPort.getBenchmarkStatus(benchmark));
    }

    @GetMapping("/allocation")
    public ResponseEntity<RoundAllocationResult> getAllocation() {
        return ResponseEntity.ok(assetsPort.calculateAllocation());
    }
}
