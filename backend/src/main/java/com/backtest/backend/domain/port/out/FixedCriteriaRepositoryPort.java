package com.backtest.backend.domain.port.out;

import com.backtest.backend.domain.entity.FixedCriteria;
import java.util.Optional;

public interface FixedCriteriaRepositoryPort {
    FixedCriteria save(FixedCriteria fixedCriteria);
    Optional<FixedCriteria> find();
}
