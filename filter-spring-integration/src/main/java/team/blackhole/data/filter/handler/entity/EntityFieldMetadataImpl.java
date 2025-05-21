package team.blackhole.data.filter.handler.entity;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.lang.reflect.Field;

/**
 * Реализация интерфейса {@link EntityFieldMetadata}
 */
@Log4j2
@RequiredArgsConstructor
public class EntityFieldMetadataImpl implements EntityFieldMetadata {

    /** Поле сущности */
    private final Field field;

    /** Наименование поля в базе данных */
    private volatile String inDatabaseName;

    /** Признак возможности фильтрации по полю */
    private volatile Boolean allowFiltering;

    @Override
    public Field getOwner() {
        return field;
    }

    /**
     * Возвращает наименования поля в базе данных
     * @return наименования поля в базе данных
     */
    @Override
    public String getInDatabaseName() {
        if (inDatabaseName == null) {
            synchronized (this) {
                if (inDatabaseName == null) {
                    var filterable = field.getAnnotation(FilterableField.class);
                    inDatabaseName = filterable == null ? field.getName() : filterable.value();
                }
            }
        }
        return inDatabaseName;
    }

    @Override
    public boolean isAllowFiltering() {
        if (allowFiltering == null) {
            synchronized (this) {
                if (allowFiltering == null) {
                    var filterable = field.getAnnotation(FilterableField.class);
                    allowFiltering = filterable != null && filterable.allow();
                }
            }
        }
        return allowFiltering;
    }
}
