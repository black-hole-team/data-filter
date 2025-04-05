package black.hole.filter.jpa.converter;

import black.hole.filter.*;
import black.hole.filter.handler.CustomCriteriaHandler;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.*;
import lombok.RequiredArgsConstructor;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Конвертер фильтра для jpa
 */
@RequiredArgsConstructor
public class JpaFilterConverter<T> implements FilterConverter<CriteriaQuery<T>> {

    /** Карта с фабриками персонализированных обработчиков критериев */
    private final List<CustomCriteriaHandler> handlers;

    /** Кеш обработчиков для полей */
    private final Map<String, Set<CustomCriteriaHandler>> handlersByField = new ConcurrentHashMap<>();

    /** Менеджер сущностей */
    private final EntityManager entityManager;

    /** Класс сущности */
    private final Class<T> entityClass;

    @Override
    public CriteriaQuery<T> convert(Filter filter) {
        if (filter.getPageSize() <= 0) {
            throw new FilterConverterException("Размер страницы должен быть больше нуля");
        }
        var cb = entityManager.getCriteriaBuilder();
        var cr = cb.createQuery(entityClass);
        var root = cr.from(entityClass);
        cr.where(Stream.concat(filter.getCriteria().stream(), handlers.stream().map(CustomCriteriaHandler::base).filter(Objects::nonNull))
                    .map(criteria -> convertCriteria(criteria, cb, root)).toArray(Predicate[]::new));
        cr.orderBy(convertSort(filter.getSorts(), cb, root));
        return cr.select(root);
    }

    /**
     * Конвертирует сортировку фильтра в сортировку jpa
     * @param sorts сортировка фильтра
     * @return cортировка jpa
     */
    private Order[] convertSort(List<FilterSort> sorts, CriteriaBuilder cb, Root<T> root) {
        var orders = new Order[sorts.size()];
        for (int i = 0; i < sorts.size(); i++) {
            var filtered = sorts.get(i);
            orders[i] = filtered.getDirection() == SortDirection.ASC ?
                cb.asc(path(root, filtered.getField())) :
                cb.desc(path(root, filtered.getField()));
        }
        return orders;
    }

    /**
     * Конвертирует критерий фильтра в критерий jpa
     * @param criteria критерий
     * @return критерий jpa
     */
    private Predicate convertCriteria(FilterCriteria criteria, CriteriaBuilder cb, Root<T> root) {
        if (criteria instanceof FilterFieldCriteria ffc && ffc.isCanBeConverted()) {
            criteria = convertFilterCriteria(ffc);
        }
        if (criteria instanceof FilterGroupCriteria fgc) {
            var subCriteria = fgc.getCriteria()
                    .stream()
                    .map(currentCriteria -> convertCriteria(currentCriteria, cb, root))
                    .toArray(Predicate[]::new);
            if (fgc.getOperator() == GroupOperator.AND) {
                return cb.and(subCriteria);
            } else {
                return cb.or(subCriteria);
            }
        } else if (criteria instanceof FilterFieldCriteria ffc) {
            return switch (ffc.getOperator()) {
                case "=" -> cb.equal(path(root, ffc.getField()), ffc.getValue());
                case "!=" -> cb.notEqual(path(root, ffc.getField()), ffc.getValue());
                case ">" -> cb.gt(path(root, ffc.getField()), (Number) ffc.getValue());
                case "<" -> cb.lt(path(root, ffc.getField()), (Number) ffc.getValue());
                case ">=" -> cb.ge(path(root, ffc.getField()), (Number) ffc.getValue());
                case "<=" -> cb.le(path(root, ffc.getField()), (Number) ffc.getValue());
                case "in" -> path(root, ffc.getField()).in(ffc.getValue());
                case "not in" -> cb.not(path(root, ffc.getField()).in(ffc.getValue()));
                case "like" -> cb.like(path(root, ffc.getField()), (String) ffc.getValue());
                case "not like" -> cb.notLike(path(root, ffc.getField()), (String) ffc.getValue());
                default -> throw new FilterConverterException(String.format("Неизвестный оператор '%s'", ffc.getOperator()));
            };
        } else if (criteria instanceof FilterStaticCriteria fsc) {
            return cb.equal(cb.literal(1), cb.literal(fsc.isAllowFilter() ? 1 : 0));
        } else {
            throw new FilterConverterException(String.format("Неизвестный тип критерия %s.", criteria.getClass().getSimpleName()));
        }
    }

    /**
     * Возвращает путь к полю сущности
     * @param root данные сущности
     * @param name путь до поля
     * @return путь
     * @param <T> тип сущности
     * @param <R> тип поля
     */
    private static <T, R> Path<R> path(Root<T> root, String name) {
        var separated = name.split("\\.");
        var path = root.<R>get(separated[0]);
        for (var i = 1; i < separated.length; i++) {
            path = path.get(separated[i]);
        }
        return path;
    }

    /**
     * Возвращает модифицированное условие фильтра, с учетом выполнения всех обработчиков
     * @param criteria     оригинальный критерий
     * @return модифицированный критерий
     */
    private FilterCriteria convertFilterCriteria(FilterFieldCriteria criteria) {
        var handlers = handlersByField.computeIfAbsent(criteria.getField(), key -> {
            var found = this.handlers.stream().filter(e -> e.canCandle(criteria.getField())).collect(Collectors.toSet());
            if (found.isEmpty()) {
                return Collections.emptySet();
            } else {
                return found;
            }
        });
        if (handlers.isEmpty()) {
            return criteria;
        }
        if (handlers.size() == 1) {
            return handlers.iterator().next().handle(criteria);
        }
        var criteriaList = new ArrayList<FilterCriteria>();
        for (var current : handlers) {
            criteriaList.add(current.handle(criteria));
        }
        return new FilterGroupCriteriaImpl(GroupOperator.AND, criteriaList);
    }
}
