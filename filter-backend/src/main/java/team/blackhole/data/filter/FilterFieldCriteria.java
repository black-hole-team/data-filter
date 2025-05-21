package team.blackhole.data.filter;

/**
 * Критерий фильтрации по полю
 */
public non-sealed interface FilterFieldCriteria extends FilterCriteria {

    /**
     * Возвращает поле критерия фильтрации
     * @return поле критерия фильтрации
     */
    String getField();

    /**
     * Возвращает оператор критерия фильтрации
     * @return оператор критерия фильтрации
     */
    String getOperator();

    /**
     * Возвращает значение критерия фильтрации
     * @return значение критерия фильтрации
     */
    Object getValue();

    /**
     * Возвращает признак возможности конвертации критерия
     * @return признак возможности конвертации критерия
     */
    boolean isCanBeConverted();

    /**
     * Возвращает критерий фильтрации с возможностью для конвертации
     * @param field    поле критерия
     * @param operator оператор критерия
     * @param value    значение критерия
     * @return критерий фильтрации
     */
    static FilterFieldCriteria convertible(String field, String operator, Object value) {
        return new FilterFieldCriteriaImpl(field, operator, value, true);
    }

    /**
     * Возвращает критерий фильтрации без возможности конвертации
     * @param field    поле критерия
     * @param operator оператор критерия
     * @param value    значение критерия
     * @return критерий фильтрации
     */
    static FilterFieldCriteria notConvertible(String field, String operator, Object value) {
        return new FilterFieldCriteriaImpl(field, operator, value, false);
    }
}
