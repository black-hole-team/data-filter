package black.hole.filter.mongo.converter;

import black.hole.filter.*;
import black.hole.filter.handler.CustomCriteriaHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.util.ObjectUtils;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Конвертер фильтра для mongo
 */
@RequiredArgsConstructor
public class MongoFilterConverter implements FilterConverter<Query> {

    /** Карта с фабриками персонализированных обработчиков критериев */
    private final List<CustomCriteriaHandler> handlers;

    /** Кеш обработчиков для полей */
    private final Map<String, Set<CustomCriteriaHandler>> handlersByField = new ConcurrentHashMap<>();

    @Override
    public Query convert(Filter filter) {
        if (filter.getPageSize() <= 0) {
            throw new FilterConverterException("Размер страницы должен быть больше нуля");
        }
        var query = new Query();
        Stream.concat(filter.getCriteria().stream(), handlers.stream().map(CustomCriteriaHandler::base).filter(Objects::nonNull))
                .map(this::convertCriteria).forEach(query::addCriteria);
        return query.with(convertSort(filter.getSorts()));
    }

    /**
     * Конвертирует сортировку фильтра в сортировку mongo
     * @param sorts сортировка фильтра
     * @return cортировка mongo
     */
    private Sort convertSort(List<FilterSort> sorts) {
        var orders = new Sort.Order[sorts.size()];
        for (int i = 0; i < sorts.size(); i++) {
            var filtered = sorts.get(i);
            orders[i] = new Sort.Order(filtered.getDirection() == SortDirection.ASC ?
                    Sort.Direction.ASC : Sort.Direction.DESC, filtered.getField());
        }
        return Sort.by(orders);
    }

    /**
     * Конвертирует критерий фильтра в критерий mongo
     * @param criteria критерий
     * @return критерий mongo
     */
    private Criteria convertCriteria(FilterCriteria criteria) {
        if (criteria instanceof FilterFieldCriteria ffc && ffc.isCanBeConverted()) {
            criteria = convertFilterCriteria(ffc);
        }
        if (criteria instanceof FilterGroupCriteria fgc) {
            var root = new Criteria();
            var subCriteria = new ArrayList<Criteria>();
            for (var current : fgc.getCriteria()) {
                subCriteria.add(convertCriteria(current));
            }
            if (fgc.getOperator() == GroupOperator.AND) {
                root.andOperator(subCriteria);
            } else {
                root.orOperator(subCriteria);
            }
            return root;
        } else if (criteria instanceof FilterFieldCriteria ffc) {
            var root = Criteria.where(ffc.getField());
            switch (ffc.getOperator()) {
                case "=":
                    root.is(ffc.getValue());
                    break;
                case "!=":
                    root.not().is(ffc.getValue());
                    break;
                case ">":
                    root.gt(ffc.getValue());
                    break;
                case "<":
                    root.lt(ffc.getValue());
                    break;
                case ">=":
                    root.gte(ffc.getValue());
                    break;
                case "<=":
                    root.lte(ffc.getValue());
                    break;
                case "in":
                    root.in(getCollectionCondition(ffc.getValue()));
                    break;
                case "not in":
                    root.nin(getCollectionCondition(ffc.getValue()));
                    break;
                case "like":
                    root.regex((String) ffc.getValue());
                    break;
                case "not like":
                    root.not().regex((String) ffc.getValue());
                    break;
                default: throw new FilterConverterException(String.format("Неизвестный оператор '%s'", ffc.getOperator()));
            }
            return root;
        } else if (criteria instanceof FilterStaticCriteria fsc) {
            return new Criteria("$expr").is(fsc.isAllowFilter());
        } else {
            throw new FilterConverterException(String.format("Неизвестный тип критерия %s.", criteria.getClass().getSimpleName()));
        }
    }

    /**
     * Возвращает преобразованное к типу коллекции значение критерия
     * @param root значение критерия
     * @return преобразованное к типу коллекции значение критерия
     */
    private Collection<?> getCollectionCondition(Object root) {
        if (root instanceof Collection<?> collection) {
            return collection;
        } else if (root instanceof Iterable<?> iterable) {
            var result = new ArrayList<>();
            for (var current : iterable) {
                result.add(current);
            }
            return result;
        } else if (root == null) {
            return Collections.emptyList();
        } else if (ObjectUtils.isArray(root)) {
            return List.of(ObjectUtils.toObjectArray(root));
        }
        throw new FilterConverterException("Для операции in и not in в качестве значения должен быть передан массив");
    }

    /**
     * Возвращает модифицированное условие фильтра, с учетом выполнения всех обработчиков
     * @param criteria оригинальный критерий
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
