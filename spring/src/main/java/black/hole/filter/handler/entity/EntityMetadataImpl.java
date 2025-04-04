package black.hole.filter.handler.entity;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

/**
 * Реализация интерфейса {@link EntityMetadata}
 */
@Log4j2
@RequiredArgsConstructor
public class EntityMetadataImpl<T> implements EntityMetadata<T> {

    /** Класс сущности */
    private final Class<T> entityClass;

    /** Тип управления сущности */
    private final EntityControllerType type;

    @Override
    public EntityControllerType getControllerType() {
        return type;
    }

    @Override
    public Class<T> getOwner() {
        return entityClass;
    }

    @Override
    public String getIdentifier() {
        return entityClass.getName();
    }
}
