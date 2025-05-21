package team.blackhole.data.filter.handler.entity;

/**
 * Метаданные сущности
 */
public interface EntityMetadata<T> {

    /**
     * Возвращает тип управления сущности
     * @return тип управления сущности
     */
    EntityControllerType getControllerType();

    /**
     * Возвращает класс, которому принадлежат метаданные
     * @return класс, которому принадлежат метаданные
     */
    Class<T> getOwner();

    /**
     * Возвращает идентификатор метаданных
     * @return идентификатор метаданных
     */
    String getIdentifier();

    /**
     * Возвращает признак принадлежности метаданных классу переданному в параметре
     * @param clazz класс
     * @return признак принадлежности метаданных классу переданному в параметре
     */
    default boolean isOwner(Class<?> clazz) {
        return getOwner().equals(clazz);
    }
}
