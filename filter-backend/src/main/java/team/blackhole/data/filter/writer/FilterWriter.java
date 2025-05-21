package team.blackhole.data.filter.writer;

import team.blackhole.data.filter.Filter;

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
