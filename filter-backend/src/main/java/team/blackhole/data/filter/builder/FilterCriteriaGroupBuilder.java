package team.blackhole.data.filter.builder;

import team.blackhole.data.filter.GroupOperator;

/**
 * Построитель группы критериев
 */
public interface FilterCriteriaGroupBuilder<T extends FilterCriteriaOwner> extends FilterCriteriaOwner {

    /**
     * Устанавливает оператор для группы критериев
     * @param operator оператор группы критериев
     * @return построитель группы критериев
     */
    FilterCriteriaGroupBuilder<T> operator(GroupOperator operator);

    /**
     * Возвращает построитель критерия для текущего списка критериев и поля field
     * @param field наименование поля, для которого выполняется фильтрация
     * @return построитель критерия для текущего списка критериев и поля field
     */
    default FilterCriteriaFieldBuilder<FilterCriteriaGroupBuilder<T>> where(String field) {
        return new FilterCriteriaFieldBuilderImpl<>(this).field(field);
    }

    /**
     * Возвращает построитель подгруппы критериев с оператором and
     * @param operator оператор подгруппы фильтра
     * @return построитель подгруппы критериев с оператором and
     */
    default FilterCriteriaGroupBuilder<FilterCriteriaGroupBuilder<T>> where(GroupOperator operator) {
        return new FilterCriteriaGroupBuilderImpl<>(this).operator(operator);
    }

    /**
     * Собирает фильтр,
     * @return владелец текущего критерия
     */
    T build();
}
