package black.hole.filter.builder;

import black.hole.filter.FilterCriteria;

/**
 * Интерфейс владельца критерия
 */
public interface FilterCriteriaOwner {

    /**
     * Добавляет критерий в набор критериев текущего владельца
     * @param criteria критерий
     * @return владельца критерия
     */
    FilterCriteriaOwner addCriteria(FilterCriteria criteria);
}
