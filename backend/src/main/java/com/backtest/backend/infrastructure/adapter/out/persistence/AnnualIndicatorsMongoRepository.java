package com.backtest.backend.infrastructure.adapter.out.persistence;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AnnualIndicatorsMongoRepository extends MongoRepository<AnnualIndicatorsDocument, String> {
    List<AnnualIndicatorsDocument> findByTicker(String ticker);
    Optional<AnnualIndicatorsDocument> findByTickerAndAno(String ticker, int ano);
    void deleteByTickerAndAno(String ticker, int ano);
    void deleteByTicker(String ticker);
}
