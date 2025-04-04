package black.hole.filter.handler.entity;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;

/**
 * Реализация интерфейса {@link EntityFieldMetadataProvider}
 */
@Log4j2
@RequiredArgsConstructor
public class EntityFieldMetadataProviderImpl implements EntityFieldMetadataProvider {

    /** Карта, где ключ это название, а значение это метаданные поля */
    private final Map<String, Map<String, EntityFieldMetadata>> fields = new ConcurrentHashMap<>();

    /** Поставщик метаданных сущности */
    private final EntityMetadataProvider entityMetadataProvider;

    @Override
    public EntityFieldMetadata get(EntityMetadata<?> entityMetadata, String fieldPath) {
        var entityFieldMetadata = getFieldMetadata(entityMetadata, fieldPath);
        if (entityFieldMetadata == null) {
            throw new EntityMetadataException(String.format("Невозможно разрешить путь до свойства '%s' в типе сущности '%s'", fieldPath,
                    entityMetadata.getIdentifier()));
        }
        return entityFieldMetadata;
    }

    /**
     * Возвращает метаданные поля для сущности
     * @param entityMetadata метаданные базовой сущности
     * @param fieldPath      путь до свойства
     * @return метаданные поля сущности
     */
    private EntityFieldMetadata getFieldMetadata(EntityMetadata<?> entityMetadata, String fieldPath) {
        var fieldMetadata = (EntityFieldMetadata) null;
        for (var part : fieldPath.split("\\.")) {
            fieldMetadata = getFieldPartMetadata(fieldMetadata == null ? entityMetadata :
                    entityMetadataProvider.get(getRootType(fieldMetadata)), part);
            if (fieldMetadata == null) {
                return null;
            }
        }
        return fieldMetadata;
    }

    /**
     * Возвращает тип поля без оберток в виде коллекций карт и т.д.
     * @param fieldMetadata метаданные поля
     * @return тип поля без оберток
     */
    private static Class<?> getRootType(EntityFieldMetadata fieldMetadata) {
        var root = fieldMetadata.getOwner().getType();
        if (Collection.class.isAssignableFrom(root)) {
            var type = fieldMetadata.getOwner().getGenericType();
            if (type instanceof ParameterizedType pt) {
                return (Class<?>) pt.getActualTypeArguments()[0];
            } else {
                throw new EntityMetadataException("Невозможно определить тип элемента коллекции поля %s".formatted(fieldMetadata.getOwner().getName()));
            }
        }
        return root;
    }

    /**
     * Возвращает метаданные от части поля
     * @param entityMetadata метаданные сущности
     * @param part           полное наименование поля сунщности
     * @return метаданные поля сущности
     */
    private EntityFieldMetadata getFieldPartMetadata(EntityMetadata<?> entityMetadata, String part) {
        return getEntityFields(entityMetadata).computeIfAbsent(part, key -> {
            var field = findField(entityMetadata, key);
            if (field == null) {
                return null;
            } else {
                var metadata = new EntityFieldMetadataImpl(field);
                log.debug("Метаданные поля {} загружены, определено наименование поля в бд '{}'", key,
                        metadata.getInDatabaseName());
                return metadata;
            }
        });
    }

    /**
     * Возвращает карту, где ключом является наименование поля, а значением метаданные поля
     * @param entityMetadata метаданные сущности
     * @return карта, где ключом является наименование поля, а значением метаданные поля
     */
    private Map<String, EntityFieldMetadata> getEntityFields(EntityMetadata<?> entityMetadata) {
        return fields.computeIfAbsent(entityMetadata.getIdentifier(), identifier -> new ConcurrentHashMap<>());
    }

    /**
     * Выполняет поиск поля по названию среди полей сущности
     * @param entityMetadata метаданные сущности
     * @param name           наименование поля
     * @return найденное поле или {@code null}
     */
    private Field findField(EntityMetadata<?> entityMetadata, String name) {
        return Arrays.stream(entityMetadata.getOwner().getDeclaredFields())
                .filter(new MetadataFieldFilter(name))
                .findFirst()
                .orElseThrow();
    }

    /**
     * Фильтр для поиска поля в классе по названию
     * @param name Наименование поля, поиск которого выполняется
     */
    private record MetadataFieldFilter(String name) implements Predicate<Field> {

        @Override
        public boolean test(Field field) {
            if (field.getName().equals(name)) {
                return true;
            }
            var annotation = field.getAnnotation(FilterableField.class);
            return annotation != null && name.equals(annotation.value());
        }
    }
}
