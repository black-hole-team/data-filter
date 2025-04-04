package black.hole.filter.handler.entity;

import java.lang.reflect.Field;

/**
 * Метаданные поля сущности
 */
public interface EntityFieldMetadata {

    /**
     * Возвращает поле которому принадлежат метаданные
     * @return поле которому принадлежат метаданные
     */
    Field getOwner();

    /**
     * Возвращает наименование поля в базе данных
     * @return наименование поля в базе данных
     */
    String getInDatabaseName();
}
