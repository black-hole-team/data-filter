package team.blackhole.data.filter.builder;

import team.blackhole.data.filter.FilterFieldCriteria;
import lombok.RequiredArgsConstructor;

import java.util.Objects;

/**
 * Реализация построителя критерия поля
 */
@RequiredArgsConstructor
public class FilterCriteriaFieldBuilderImpl<T extends FilterCriteriaOwner>implements FilterCriteriaFieldBuilder<T> {

    /** Владелец текущего построителя */
    private final T owner;

    /** Наинов ание поля фильтрации */
    private String field;

    /** Оператор фильтрации */
    private String operator;

    /** Значение фильтрации */
    private Object value;

    @Override
    public FilterCriteriaFieldBuilder<T> field(String field) {
        this.field = field;
        return this;
    }

    @Override
    public FilterCriteriaFieldBuilder<T> operator(String operator) {
        this.operator = operator;
        return this;
    }

    @Override
    public FilterCriteriaFieldBuilder<T> value(Object value) {
        this.value = value;
        return this;
    }

    @Override
    public T build() {
        owner.addCriteria(FilterFieldCriteria.convertible(field, Objects.requireNonNullElse(operator, "="), value));
        return owner;
    }
}
