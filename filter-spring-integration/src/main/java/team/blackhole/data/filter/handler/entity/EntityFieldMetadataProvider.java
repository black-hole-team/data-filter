package team.blackhole.data.filter.handler.entity;

/**
 * Поставщик метаданных поля сущности
 */
public interface EntityFieldMetadataProvider {

    /**
     * Возвращает метаданные поля сущности по пути до поля
     * @param entityMetadata метаданные сущности
     * @param fieldPath      путь до поля сущности
     * @return метаданные поля сущности
     */
    EntityFieldMetadata get(EntityMetadata<?> entityMetadata, String fieldPath);
}
