package team.blackhole.data.filter.builder;

import team.blackhole.data.filter.Filter;
import team.blackhole.data.filter.FilterCriteria;
import team.blackhole.data.filter.FilterSort;
import team.blackhole.data.filter.GroupOperator;

import java.util.List;

/**
 * Интерфейс построителя фильтра
 */
public interface FilterBuilder extends FilterCriteriaOwner {

    /**
     * Устанавливает страницу фильтра
     * @param page страница фильтра
     * @return построитель фильтра
     */
    FilterBuilder page(int page);

    /**
     * Увеличивает страницу фильтра на значение {@code delta}
     * @param delta разница между текущей и новой страницей фильтра
     * @return построитель фильтра
     */
    FilterBuilder incrementPage(int delta);

    /**
     * Устанавливает количество элементов на 1 странице
     * @param pageSize количество элементов на 1 странице
     * @return построитель фильтра
     */
    FilterBuilder pageSize(int pageSize);

    /**
     * Устанавливает список критериев фильтра
     * @param criteria список критериев
     * @return построитель фильтра
     */
    FilterBuilder criteria(List<FilterCriteria> criteria);

    /**
     * Устанавливает список сортировок фильтра
     * @param sorts список сортировок фильтра
     * @return построитель фильтра
     */
    FilterBuilder sorts(List<FilterSort> sorts);

    /**
     * Возвращает построитель критерия для текущего списка критериев и поля field
     * @param field наименование поля, для которого выполняется фильтрация
     * @return построитель критерия для текущего списка критериев и поля field
     */
    default FilterCriteriaFieldBuilder<FilterBuilder> where(String field) {
        return new FilterCriteriaFieldBuilderImpl<>(this).field(field);
    }

    /**
     * Возвращает построитель подгруппы критериев с оператором and
     * @param operator оператор подгруппы фильтра
     * @return построитель подгруппы критериев с оператором and
     */
    default FilterCriteriaGroupBuilder<FilterBuilder> where(GroupOperator operator) {
        return new FilterCriteriaGroupBuilderImpl<>(this).operator(operator);
    }

    /**
     * Выполнить сборку фильтра
     * @return собранный фильтр
     */
    Filter build();
}
