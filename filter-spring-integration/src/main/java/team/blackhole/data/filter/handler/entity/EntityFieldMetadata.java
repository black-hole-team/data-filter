package team.blackhole.data.filter.handler.entity;

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

    /**
     * Возвращает признак возможноти фильтрации по полю
     * @return {@code true}, если фильтрация возможна, {@code false}, если иначе
     */
    boolean isAllowFiltering();
}
