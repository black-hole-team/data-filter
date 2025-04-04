package black.hole.filter.support;

import black.hole.filter.Filter;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

/**
 * Реализация интерфейса {@link Page}
 * @param <T> тип содержимого страницы
 */
@Getter
@RequiredArgsConstructor
public class PageImpl<T> implements Page<T> {

    /** Содержимое страницы */
    private final List<T> content;

    /** Параметры пагинации */
    private final int pageNumber;

    /** Общее количество элементов */
    private final long totalElements;

    /** Фильтр, по которому страница была получена */
    private final Filter filter;
}
