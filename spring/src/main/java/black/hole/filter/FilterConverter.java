package black.hole.filter;

/**
 * Интерфейс преобразования фильтра
 */
public interface FilterConverter<R> {

    /**
     * Конвертирует фильтр в целевой класс
     * @param filter фильтр
     * @return экземпляр целевого класса
     */
    R convert(Filter filter);
}
