package team.blackhole.data.filter.builder;

/**
 * Фабрика просителей фильтра
 */
public interface FilterBuilderFactory {

    /**
     * Создает и первично настраивает построитель фильтра
     * @return построитель фильтра
     */
    FilterBuilder create();
}
