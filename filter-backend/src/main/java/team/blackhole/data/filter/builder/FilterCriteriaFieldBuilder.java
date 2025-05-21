package team.blackhole.data.filter.builder;

/**
 * Построитель критерия фильтра
 */
public interface FilterCriteriaFieldBuilder<T extends FilterCriteriaOwner> {

    /**
     * Устанавливает название поля для текущего критерия
     * @param field наименование поля
     * @return построитель критерия поля
     */
    FilterCriteriaFieldBuilder<T> field(String field);

    /**
     * Устанавливает оператор для текущего критерия
     * @param operator оператор
     * @return построитель критерия поля
     */
    FilterCriteriaFieldBuilder<T> operator(String operator);

    /**
     * Устанавливает название поля для текущего критерия
     * @param value значение
     * @return построитель критерия поля
     */
    FilterCriteriaFieldBuilder<T> value(Object value);

    /**
     * Собирает фильтр,
     * @return владелец текущего критерия
     */
    T build();
}
