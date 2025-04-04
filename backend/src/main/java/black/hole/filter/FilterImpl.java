package black.hole.filter;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Collections;
import java.util.List;

/**
 * Реализация интерфейса {@link Filter}
 */
@Getter
@RequiredArgsConstructor
public class FilterImpl implements Filter {

    /** Пустой фильтр */
    protected static final Filter EMPTY_FILTER = new FilterImpl(0, 500, Collections.emptyList(), Collections.emptyList());

    /** Страница фильтра */
    private final int page;

    /** Количество элементов на одной странице */
    private final int pageSize;

    /** Список критериев фильтра */
    private final List<FilterCriteria> criteria;

    /** Список сортировок фильтра */
    private final List<FilterSort> sorts;
}
