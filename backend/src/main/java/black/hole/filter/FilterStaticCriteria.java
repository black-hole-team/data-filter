package black.hole.filter;

/**
 * Статический критерий фильтрации
 */
public non-sealed interface FilterStaticCriteria extends FilterCriteria {

    /**
     * Возвращает значение критерия
     * @return значение критерия
     */
    boolean isAllowFilter();

    /**
     * Возвращает статический критерий эквивалентный переданному параметру
     * @return статический критерий эквивалентный переданному параметру
     */
    static FilterCriteria of(boolean allow) {
        return new FilterStaticCriteriaImpl(allow);
    }

    /**
     * Возвращает статический критерий эквивалентный true выражению
     * @return статический критерий эквивалентный true выражению
     */
    static FilterCriteria allow() {
        return of(true);
    }

    /**
     * Возвращает статический критерий эквивалентный false выражению
     * @return статический критерий эквивалентный false выражению
     */
    static FilterCriteria deny() {
        return of(false);
    }
}
