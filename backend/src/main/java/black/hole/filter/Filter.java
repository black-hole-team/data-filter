package black.hole.filter;

import java.util.List;

/**
 * Интерфейс фильтра
 */
public interface Filter {

    /**
     * Возвращает страницу фильтрации
     * @return страница фильтрации
     */
    int getPage();

    /**
     * Возвращает размер страницы
     * @return размер страницы
     */
    int getPageSize();

    /**
     * Возвращает список критериев фильтрации
     * @return список критериев фильтрации
     */
    List<FilterCriteria> getCriteria();

    /**
     * Возвращает список сортировок фильтра
     * @return список сортировок фильтра
     */
    List<FilterSort> getSorts();

    /**
     * Возвращает пустой фильтр
     * @return пустой фильтр
     */
    static Filter empty() {
        return FilterImpl.EMPTY_FILTER;
    }
}
