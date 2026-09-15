package com.backtest.backend.infrastructure.adapter.in.rest;

import com.backtest.backend.domain.entity.BacktestResult;
import com.backtest.backend.domain.port.in.ExecuteBacktestPort;
import com.backtest.backend.domain.port.in.GetBacktestResultPort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/backtest")
public class BacktestController {

    private final ExecuteBacktestPort executeBacktestPort;
    private final GetBacktestResultPort getBacktestResultPort;

    public BacktestController(ExecuteBacktestPort executeBacktestPort, GetBacktestResultPort getBacktestResultPort) {
        this.executeBacktestPort = executeBacktestPort;
        this.getBacktestResultPort = getBacktestResultPort;
    }

    public static class BacktestRequest {
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        private LocalDate inicio;

        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        private LocalDate fim;

        private List<String> tickers;

        public LocalDate getInicio() {
            return inicio;
        }

        public void setInicio(LocalDate inicio) {
            this.inicio = inicio;
        }

        public LocalDate getFim() {
            return fim;
        }

        public void setFim(LocalDate fim) {
            this.fim = fim;
        }

        public List<String> getTickers() {
            return tickers;
        }

        public void setTickers(List<String> tickers) {
            this.tickers = tickers;
        }
    }

    @PostMapping
    public ResponseEntity<?> executeBacktest(@RequestBody BacktestRequest request) {
        try {
            LocalDate start = request.getInicio() != null ? request.getInicio() : LocalDate.now().minusYears(5);
            LocalDate end = request.getFim() != null ? request.getFim() : LocalDate.now();
            BacktestResult result = executeBacktestPort.executeBacktest(start, end, request.getTickers());
            return ResponseEntity.ok(result);
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Erro ao executar backtest: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<BacktestResult> getResultById(@PathVariable String id) {
        return getBacktestResultPort.getResultById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<BacktestResult>> getAllResults() {
        return ResponseEntity.ok(getBacktestResultPort.getAllResults());
    }
}
