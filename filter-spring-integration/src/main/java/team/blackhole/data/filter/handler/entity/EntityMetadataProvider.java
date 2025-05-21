package team.blackhole.data.filter.handler.entity;

/**
 * Поставщик метаданных сущности
 */
public interface EntityMetadataProvider {

    /**
     * Возвращает метаданные сущности для типа `type`
     * @param type тип сущности метаданных
     * @return метаданные сущности
     */
    <T> EntityMetadata<T> get(Class<T> type);
}
