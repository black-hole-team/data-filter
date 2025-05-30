package team.blackhole.data.filter.handler.entity;

import java.lang.annotation.*;

/**
 * Аннотация фильтруемого поля
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
public @interface FilterableField {

    /**
     * Возвращает наименование фильтруемого поля
     * @return наименование фильтруемого поля
     */
    String value();

    /**
     * Возвращает признак доступности фильтрации по полю
     * @return признак доступности фильтрации по полю
     */
    boolean allow() default false;
}
