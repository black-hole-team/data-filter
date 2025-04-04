package black.hole.filter.config;

import black.hole.filter.FilterFacade;
import black.hole.filter.resolvers.FilterArgumentResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * Автоматическая mvc конфигурация разрешителя аргументов фильтра в запросе
 */
@Configuration
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class FilterMvcAutoConfiguration implements WebMvcConfigurer {

    /** Сервис фильтрации */
    private final FilterFacade filterFacade;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new FilterArgumentResolver(filterFacade));
    }
}
