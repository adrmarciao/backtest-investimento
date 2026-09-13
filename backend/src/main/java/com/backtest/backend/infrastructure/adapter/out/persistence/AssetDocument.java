package com.backtest.backend.infrastructure.adapter.out.persistence;

import com.backtest.backend.domain.entity.Periodicity;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

@Document(collection = "assets")
public class AssetDocument {

    @Id
    private String ticker;
    private BigDecimal valorAporte;
    private Periodicity periodicidade;

    public AssetDocument() {
    }

    public AssetDocument(String ticker, BigDecimal valorAporte, Periodicity periodicidade) {
        this.ticker = ticker;
        this.valorAporte = valorAporte;
        this.periodicidade = periodicidade;
    }

    public String getTicker() {
        return ticker;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    public BigDecimal getValorAporte() {
        return valorAporte;
    }

    public void setValorAporte(BigDecimal valorAporte) {
        this.valorAporte = valorAporte;
    }

    public Periodicity getPeriodicidade() {
        return periodicidade;
    }

    public void setPeriodicidade(Periodicity periodicidade) {
        this.periodicidade = periodicidade;
    }
}
