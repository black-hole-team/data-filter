package black.hole.filter.annotations;

import black.hole.filter.config.FilterAutoConfiguration;
import black.hole.filter.config.FilterMvcAutoConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * Аннотация, которая позволяет включить механизм фильтрации в приложении spring
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Import({FilterAutoConfiguration.class, FilterMvcAutoConfiguration.class})
public @interface EnableFiltration {
}
