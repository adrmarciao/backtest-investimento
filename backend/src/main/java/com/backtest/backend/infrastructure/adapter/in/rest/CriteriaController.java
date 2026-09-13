package com.backtest.backend.infrastructure.adapter.in.rest;

import com.backtest.backend.domain.entity.FixedCriteria;
import com.backtest.backend.domain.port.in.SaveFixedCriteriaPort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/criteria")
public class CriteriaController {

    private final SaveFixedCriteriaPort saveFixedCriteriaPort;

    public CriteriaController(SaveFixedCriteriaPort saveFixedCriteriaPort) {
        this.saveFixedCriteriaPort = saveFixedCriteriaPort;
    }

    @PutMapping
    public ResponseEntity<?> saveCriteria(@RequestBody FixedCriteria criteria) {
        try {
            FixedCriteria saved = saveFixedCriteriaPort.saveCriteria(criteria);
            return ResponseEntity.ok(saved);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<FixedCriteria> getCriteria() {
        return saveFixedCriteriaPort.getCriteria()
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.ok(new FixedCriteria())); // Retorna objeto vazio se não configurado
    }
}
