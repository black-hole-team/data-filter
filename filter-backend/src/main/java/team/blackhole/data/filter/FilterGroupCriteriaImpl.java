package team.blackhole.data.filter;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

/**
 * Реализация интерфейса {@link FilterGroupCriteria}
 */
@Getter
@AllArgsConstructor
public class FilterGroupCriteriaImpl implements FilterGroupCriteria {

    /** Оператор группы */
    private final GroupOperator operator;

    /** Список критериев фильтрации группы */
    private final List<FilterCriteria> criteria;
}
