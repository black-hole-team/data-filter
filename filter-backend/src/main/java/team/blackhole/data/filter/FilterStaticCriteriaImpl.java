package team.blackhole.data.filter;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Реализация интерфейса {@code FilterStaticCriteria}
 */
@Getter
@RequiredArgsConstructor
public class FilterStaticCriteriaImpl implements FilterStaticCriteria {

    /** Признак статического критерия */
    private final boolean allowFilter;
}
