package team.blackhole.data.filter;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Реализация интерфейса {@link FilterFieldCriteria}
 */
@Getter
@RequiredArgsConstructor
public class FilterFieldCriteriaImpl implements FilterFieldCriteria {

    /** Наинов ание поля фильтрации */
    private final String field;

    /** Оператор фильтрации */
    private final String operator;

    /** Значение фильтрации */
    private final Object value;

    /** Признак конвертированного критерия */
    private final boolean canBeConverted;
}
