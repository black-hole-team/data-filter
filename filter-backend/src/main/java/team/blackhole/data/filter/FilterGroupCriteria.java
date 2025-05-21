package team.blackhole.data.filter;

import java.util.List;

/**
 * Группа критериев
 */
public non-sealed interface FilterGroupCriteria extends FilterCriteria {

    /**
     * Возвращает список критериев группы
     * @return список критериев группы
     */
    List<FilterCriteria> getCriteria();

    /**
     * Возвращает оператор группы
     * @return оператор группы
     */
    GroupOperator getOperator();

    /**
     * Возвращает новую
     * @param criteria список критериев фильтрации
     * @return критерий групповой фильтрации
     */
    static FilterCriteria and(FilterCriteria ...criteria) {
        return new FilterGroupCriteriaImpl(GroupOperator.AND, List.of(criteria));
    }

    /**
     * Возвращает новую
     * @param criteria список критериев фильтрации
     * @return критерий групповой фильтрации
     */
    static FilterCriteria or(FilterCriteria ...criteria) {
        return new FilterGroupCriteriaImpl(GroupOperator.OR, List.of(criteria));
    }
}
