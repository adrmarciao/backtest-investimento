package com.backtest.backend.infrastructure.adapter.in.rest;

import com.backtest.backend.domain.entity.Asset;
import com.backtest.backend.domain.port.in.RegisterAssetPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/assets")
public class AssetController {

    private final RegisterAssetPort registerAssetPort;

    public AssetController(RegisterAssetPort registerAssetPort) {
        this.registerAssetPort = registerAssetPort;
    }

    @PostMapping
    public ResponseEntity<?> registerAsset(@RequestBody Asset asset) {
        try {
            Asset created = registerAssetPort.registerAsset(asset);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<Asset>> getAllAssets() {
        return ResponseEntity.ok(registerAssetPort.getAllAssets());
    }

    @GetMapping("/{ticker}")
    public ResponseEntity<Asset> getAssetByTicker(@PathVariable String ticker) {
        return registerAssetPort.getAssetByTicker(ticker)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{ticker}")
    public ResponseEntity<Void> deleteAsset(@PathVariable String ticker) {
        registerAssetPort.deleteAsset(ticker);
        return ResponseEntity.noContent().build();
    }
}
