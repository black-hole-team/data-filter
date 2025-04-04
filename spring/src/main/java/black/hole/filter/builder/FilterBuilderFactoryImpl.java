package black.hole.filter.builder;

/**
 * Реализация интерфейса {@link FilterBuilderFactory} по умолчанию
 */
public class FilterBuilderFactoryImpl implements FilterBuilderFactory {

    @Override
    public FilterBuilder create() {
        var builder = new FilterBuilderImpl();
        builder.setPage(0);
        builder.setPageSize(500);
        return builder;
    }
}
