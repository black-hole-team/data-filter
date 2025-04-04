package black.hole.filter.handler;

import black.hole.filter.FilterCriteria;
import black.hole.filter.FilterFieldCriteria;

/**
 * Персонализированный обработчик критериев
 */
public interface CustomCriteriaHandler {

    /**
     * Возвращает базовый критерий
     * @return базовый критерий
     */
    default FilterCriteria base() {
        return null;
    }

    /**
     * Возвращает кастомные значение критерия
     * @param criteria критерий для фильтрации
     * @return кастомные значение критерия
     */
    default FilterCriteria handle(FilterFieldCriteria criteria) {
        return criteria;
    }

    /**
     * Возвращает признак возможности обработки поля
     * @param field поле
     * @return признак возможности обработки поля
     */
    default boolean canCandle(String field) {
        return false;
    }

    /**
     * Возвращает идентификатор обработчика
     * @return идентификатор обработчика
     */
    String getIdentifier();
}
