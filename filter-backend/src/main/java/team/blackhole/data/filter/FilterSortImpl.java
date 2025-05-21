package team.blackhole.data.filter;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * Реализация интерфейса {@link FilterSort}
 */
@Getter
@ToString
@AllArgsConstructor
public class FilterSortImpl implements FilterSort {

    /** Направление сортировки */
    private final SortDirection direction;

    /** Наименование поля для сортировки */
    private final String field;
}
