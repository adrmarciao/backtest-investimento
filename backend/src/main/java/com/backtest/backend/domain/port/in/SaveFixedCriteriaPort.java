package com.backtest.backend.domain.port.in;

import com.backtest.backend.domain.entity.FixedCriteria;
import java.util.Optional;

public interface SaveFixedCriteriaPort {
    FixedCriteria saveCriteria(FixedCriteria criteria);
    Optional<FixedCriteria> getCriteria();
}
