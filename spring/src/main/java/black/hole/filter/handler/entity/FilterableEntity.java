package black.hole.filter.handler.entity;

import java.lang.annotation.*;

/**
 * Аннотация фильтруемой сущности
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
public @interface FilterableEntity {

    /**
     * Возвращает тип управления сущностью
     * @return тип управления сущностью
     */
    EntityControllerType value();
}
