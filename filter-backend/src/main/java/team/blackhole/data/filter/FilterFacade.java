package team.blackhole.data.filter;

import java.io.Reader;

/**
 * Интерфейс фасада модуля фильтрации
 */
public interface FilterFacade {

    /**
     * Возвращает объект общего фильтра
     * @param input входящая строка фильтрации
     * @return объект общего фильтра
     */
    Filter read(Reader input);

    /**
     * Возвращает строку фильтра, соответствующую переданному объекту фильтра
     * @param filter фильтр
     * @return строка фильтра, соответствующую переданному объекту фильтра
     */
    String write(Filter filter);
}
