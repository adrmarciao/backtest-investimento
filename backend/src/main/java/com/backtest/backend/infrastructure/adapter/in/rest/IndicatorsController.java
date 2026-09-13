package com.backtest.backend.infrastructure.adapter.in.rest;

import com.backtest.backend.domain.entity.AnnualIndicators;
import com.backtest.backend.domain.port.in.SaveAnnualIndicatorsPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/assets/{ticker}/indicators")
public class IndicatorsController {

    private final SaveAnnualIndicatorsPort saveAnnualIndicatorsPort;

    public IndicatorsController(SaveAnnualIndicatorsPort saveAnnualIndicatorsPort) {
        this.saveAnnualIndicatorsPort = saveAnnualIndicatorsPort;
    }

    @PostMapping
    public ResponseEntity<?> saveIndicators(@PathVariable String ticker, @RequestBody AnnualIndicators indicators) {
        try {
            indicators.setTicker(ticker);
            AnnualIndicators saved = saveAnnualIndicatorsPort.saveIndicators(indicators);
            return ResponseEntity.status(HttpStatus.CREATED).body(saved);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<AnnualIndicators>> getIndicatorsByTicker(@PathVariable String ticker) {
        return ResponseEntity.ok(saveAnnualIndicatorsPort.getIndicatorsByTicker(ticker));
    }

    @GetMapping("/{year}")
    public ResponseEntity<AnnualIndicators> getIndicatorsByYear(@PathVariable String ticker, @PathVariable int year) {
        return saveAnnualIndicatorsPort.getIndicatorsByTickerAndYear(ticker, year)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{year}")
    public ResponseEntity<?> updateIndicatorsByYear(@PathVariable String ticker, @PathVariable int year, @RequestBody AnnualIndicators indicators) {
        try {
            indicators.setTicker(ticker);
            indicators.setAno(year);
            AnnualIndicators saved = saveAnnualIndicatorsPort.saveIndicators(indicators);
            return ResponseEntity.ok(saved);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{year}")
    public ResponseEntity<Void> deleteIndicatorsByYear(@PathVariable String ticker, @PathVariable int year) {
        saveAnnualIndicatorsPort.deleteIndicatorsByTickerAndYear(ticker, year);
        return ResponseEntity.noContent().build();
    }
}
