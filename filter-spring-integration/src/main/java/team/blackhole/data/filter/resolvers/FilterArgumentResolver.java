package team.blackhole.data.filter.resolvers;

import team.blackhole.data.filter.Filter;
import team.blackhole.data.filter.FilterFacade;
import team.blackhole.data.filter.exception.FilterException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.io.InputStreamReader;

/**
 * Разрешитель аргументов
 */
@RequiredArgsConstructor
public class FilterArgumentResolver implements HandlerMethodArgumentResolver {

    /** Сервис фильтрации */
    private final FilterFacade filterFacade;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().isAssignableFrom(Filter.class);
    }

    @Override
    public Object resolveArgument(@NonNull MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        var request = webRequest.getNativeRequest(HttpServletRequest.class);
        if (request == null) {
            throw new FilterException("Невозможно получить поток на чтение");
        }
        return filterFacade.read(new InputStreamReader(request.getInputStream()));
    }
}
