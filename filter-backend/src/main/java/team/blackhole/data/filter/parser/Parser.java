package team.blackhole.data.filter.parser;

/**
 * Интерфейс разбора поступающих данных
 * @param <T> сущность для разбора
 */
public interface Parser<T> {

    /**
     * Выполнить разбор токенов поступающих из лексического анализатора
     * @return разобранная сущность
     */
    T parse();
}
