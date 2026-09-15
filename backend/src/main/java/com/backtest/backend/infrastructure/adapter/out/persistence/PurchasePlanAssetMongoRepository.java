package com.backtest.backend.infrastructure.adapter.out.persistence;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PurchasePlanAssetMongoRepository extends MongoRepository<PurchasePlanAssetDocument, String> {
    Optional<PurchasePlanAssetDocument> findByTickerIgnoreCase(String ticker);
    void deleteByTickerIgnoreCase(String ticker);
    List<PurchasePlanAssetDocument> findAllByOrderByOrdemAsc();
}
