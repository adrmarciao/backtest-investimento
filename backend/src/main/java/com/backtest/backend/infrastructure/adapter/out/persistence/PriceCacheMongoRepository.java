package com.backtest.backend.infrastructure.adapter.out.persistence;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PriceCacheMongoRepository extends MongoRepository<PriceCacheDocument, String> {
}
