package black.hole.filter.writer;

import black.hole.filter.Filter;

/**
 * Писатель фильтра
 */
public interface FilterWriter {

    /**
     * Возвращает строку фильтра, соответствующую переданному объекту фильтра
     * @param filter фильтр
     * @return строка фильтра, соответствующую переданному объекту фильтра
     */
    String write(Filter filter);
}
