package team.blackhole.data.filter.annotations;

import team.blackhole.data.filter.config.FilterAutoConfiguration;
import team.blackhole.data.filter.config.FilterMvcAutoConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * Встраивает фильтрацию в кофигурацию spring boot
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Import({FilterAutoConfiguration.class, FilterMvcAutoConfiguration.class})
public @interface EnableFiltration {
}
