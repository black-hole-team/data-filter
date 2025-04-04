package black.hole.filter.handler.entity;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Реализация интерфейса {@link EntityFieldMetadata}
 */
@Log4j2
@RequiredArgsConstructor
public class EntityFieldMetadataImpl implements EntityFieldMetadata {

    /** Аннотации, которые являются источниками наименования полей класса */
    private static final Set<Class<Annotation>> ANNOTATION_NAME_SOURCES = Stream
            .of("org.springframework.data.mongodb.core.mapping.Field")
            .map(EntityFieldMetadataImpl::getAnnotationClass)
            .filter(Optional::isPresent)
            .map(Optional::get)
            .collect(Collectors.toSet());

    /** Поле сущности */
    private final Field field;

    /** Наименование поля в базе данных */
    private volatile String inDatabaseName;

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
                    inDatabaseName = extractNameByAnnotation(field);
                }
            }
        }
        return inDatabaseName;
    }

    /**
     * Извлекает наименование поля в бд из аннотации поля
     * @param field поле
     * @return название поля в бд
     */
    public static String extractNameByAnnotation(Field field) {
        var foundAnnotation = ANNOTATION_NAME_SOURCES.stream()
            .map(field::getAnnotation)
            .filter(Objects::nonNull)
            .findFirst()
            .orElseGet(() -> field.getAnnotation(FilterableField.class));
        return foundAnnotation != null ? callMethodIfPresent(foundAnnotation, "value")
                : field.getName();
    }

    /**
     * Возвращает класс аннотации
     * @param name наименование класса аннотации
     * @return опциональное значение класса аннотации
     */
    @SuppressWarnings("unchecked")
    private static Optional<Class<Annotation>> getAnnotationClass(String name) {
        try {
            return Optional.of((Class<Annotation>) Class.forName(name));
        } catch (ClassNotFoundException ex) {
            // Класс не найден
        }
        log.warn("Не удалось загрузить класс аннотаций {}, необходимо проверить пути к классам", name);
        return Optional.empty();
    }

    /**
     * Вызывает метод аннотации, если он присутствует
     * @param foundAnnotation аннотация
     * @param name            наименование метода
     * @return значение метода, если он присутствует, иначе null
     */
    private static String callMethodIfPresent(Annotation foundAnnotation, String name) {
        try {
            return (String) foundAnnotation.getClass().getMethod(name).invoke(foundAnnotation);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            return null;
        }
    }
}
