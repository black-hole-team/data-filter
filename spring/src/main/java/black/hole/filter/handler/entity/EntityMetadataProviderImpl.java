package black.hole.filter.handler.entity;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Реализация интерфейса {@link EntityMetadataProvider}
 */
public class EntityMetadataProviderImpl implements EntityMetadataProvider {

    /** Карта, где ключом является класс сущности, а значением метаданные сущности */
    private final Map<Class<?>, EntityMetadata<?>> metadata = new ConcurrentHashMap<>();

    @Override
    @SuppressWarnings("unchecked")
    public <T> EntityMetadata<T> get(Class<T> type) {
        return (EntityMetadata<T>) metadata.computeIfAbsent(type, entityType -> {
            var filterableEntity = entityType.getAnnotation(FilterableEntity.class);
            if (filterableEntity != null) {
                return new EntityMetadataImpl<>(entityType, filterableEntity.value());
            } else {
                if (Arrays.stream(entityType.getAnnotations()).anyMatch(e -> e.annotationType().getName().contains("mongo"))) {
                    return new EntityMetadataImpl<>(entityType, EntityControllerType.MONGO);
                } else if (Arrays.stream(entityType.getAnnotations()).anyMatch(e -> e.annotationType().getName().contains("jakarta"))) {
                    return new EntityMetadataImpl<>(entityType, EntityControllerType.JPA);
                } else {
                    throw new EntityMetadataException(String.format("Неизвестный тип управляемой сущности '%s'", entityType.getName()));
                }
            }
        });
    }
}
