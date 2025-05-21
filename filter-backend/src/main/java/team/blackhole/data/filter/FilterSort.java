package team.blackhole.data.filter;

/**
 * Сортировка фильтра
 */
public interface FilterSort {

    /**
     * Возвращает направление сортировки
     * @return направление сортировки
     */
    SortDirection getDirection();

    /**
     * Возвращает поле для сортировки
     * @return поле для сортировки
     */
    String getField();
}
