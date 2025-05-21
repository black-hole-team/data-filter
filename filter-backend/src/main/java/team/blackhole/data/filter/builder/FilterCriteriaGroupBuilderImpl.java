package team.blackhole.data.filter.builder;

import team.blackhole.data.filter.FilterCriteria;
import team.blackhole.data.filter.FilterGroupCriteriaImpl;
import team.blackhole.data.filter.GroupOperator;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * Реализация построителя группы критериев
 */
@RequiredArgsConstructor
public class FilterCriteriaGroupBuilderImpl<T extends FilterCriteriaOwner> implements FilterCriteriaGroupBuilder<T> {

    /** Владелец текущего построителя */
    private final T owner;

    /** Оператор группы */
    private GroupOperator groupOperator;

    /** Список критериев фильтра */
    private List<FilterCriteria> criteria;

    @Override
    public FilterCriteriaOwner addCriteria(FilterCriteria criteria) {
        if (this.criteria == null) {
            this.criteria = new ArrayList<>();
        }
        this.criteria.add(criteria);
        return this;
    }

    @Override
    public FilterCriteriaGroupBuilder<T> operator(GroupOperator operator) {
        this.groupOperator = operator;
        return this;
    }

    @Override
    public T build() {
        owner.addCriteria(new FilterGroupCriteriaImpl(groupOperator, criteria));
        return owner;
    }
}
