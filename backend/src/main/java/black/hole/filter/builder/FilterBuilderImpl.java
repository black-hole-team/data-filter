package black.hole.filter.builder;

import black.hole.filter.Filter;
import black.hole.filter.FilterCriteria;
import black.hole.filter.FilterImpl;
import black.hole.filter.FilterSort;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Построитель фильтра
 */
@Setter
public class FilterBuilderImpl implements FilterBuilder {

    /** Страница фильтра */
    private int page;

    /** Количество элементов на одной странице */
    private int pageSize;

    /** Список критериев фильтра */
    private List<FilterCriteria> criteria;

    /** Список сортировок фильтра */
    private List<FilterSort> sorts;

    @Override
    public FilterBuilder page(int page) {
        this.page = page;
        return this;
    }

    @Override
    public FilterBuilder pageSize(int pageSize) {
        this.pageSize = pageSize;
        return this;
    }

    @Override
    public FilterBuilder criteria(List<FilterCriteria> criteria) {
        this.criteria = criteria;
        return this;
    }

    @Override
    public FilterBuilder sorts(List<FilterSort> sorts) {
        this.sorts = sorts;
        return this;
    }

    @Override
    public FilterCriteriaOwner addCriteria(FilterCriteria criteria) {
        if (this.criteria == null) {
            this.criteria = new ArrayList<>();
        }
        this.criteria.add(criteria);
        return this;
    }

    @Override
    public Filter build() {
        return new FilterImpl(page, pageSize, criteria == null ? Collections.emptyList() : criteria,
                sorts == null ? Collections.emptyList() : sorts);
    }
}
