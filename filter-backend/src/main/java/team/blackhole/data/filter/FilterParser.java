package team.blackhole.data.filter;

import team.blackhole.data.filter.builder.FilterBuilder;
import team.blackhole.data.filter.builder.FilterBuilderFactory;
import team.blackhole.data.filter.exception.ParserException;
import team.blackhole.data.filter.lexer.Lexer;
import team.blackhole.data.filter.lexer.LexerTokenMaskImpl;
import team.blackhole.data.filter.lexer.LexerTokenType;
import team.blackhole.data.filter.parser.BufferedParser;
import team.blackhole.data.filter.parser.conditional.ConditionEquals;
import team.blackhole.data.filter.parser.conditional.ConditionOrIn;
import team.blackhole.data.filter.parser.conditional.Conditional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Класс для разбора фильтра для фильтра
 */
public class FilterParser extends BufferedParser<Filter> {

    /** Маска следующей группы */
    private static final Conditional NEXT_GROUP_CONDITION = new ConditionEquals(new LexerTokenMaskImpl(LexerTokenType.CONTROL, "["));

    /** Маска логической подгруппы */
    private static final Conditional NEXT_SUB_GROUP_CONDITION = new ConditionEquals(new LexerTokenMaskImpl(LexerTokenType.CONTROL, "("));

    /** Маска разделителя запятой */
    private static final Conditional COMMA_SEPARATOR_CONDITION = new ConditionEquals(new LexerTokenMaskImpl(LexerTokenType.SEPARATOR, ","));

    /** Маска сортировки по возрастанию */
    private static final Conditional IS_ASC_SORT_CONDITION = new ConditionEquals(new LexerTokenMaskImpl(LexerTokenType.SPECIAL, "^"));

    /** Маска следующей группы */
    private static final Conditional NEXT_LOGICAL_CONDITION = new ConditionOrIn(new LexerTokenMaskImpl(LexerTokenType.SPECIAL, "|"),
            new LexerTokenMaskImpl(LexerTokenType.SPECIAL, "&"));

    /** Маска конфигурации пагинации [n, n] */
    private static final Conditional[] PAGE_CONFIG_MASK = new Conditional[] {new ConditionEquals(new LexerTokenMaskImpl(LexerTokenType.DIGIT, null)),
            new ConditionEquals(new LexerTokenMaskImpl(LexerTokenType.SEPARATOR, ",")), new ConditionEquals(new LexerTokenMaskImpl(LexerTokenType.DIGIT, null))};

    /** Маска конфигурации фильтра [(?word? >,<,!,=,in,not]*/
    private static final Conditional[] FILTER_MASK_1 = new Conditional[] {
            new ConditionEquals(new LexerTokenMaskImpl(LexerTokenType.WORD, null)),
            new ConditionOrIn(new LexerTokenMaskImpl(LexerTokenType.SPECIAL, "!"), new LexerTokenMaskImpl(LexerTokenType.SPECIAL, ">"),
                    new LexerTokenMaskImpl(LexerTokenType.SPECIAL, "<"), new LexerTokenMaskImpl(LexerTokenType.SPECIAL, "="),
                    new LexerTokenMaskImpl(LexerTokenType.WORD, "in"), new LexerTokenMaskImpl(LexerTokenType.WORD, "not"),
                    new LexerTokenMaskImpl(LexerTokenType.CONTROL, "("), new LexerTokenMaskImpl(LexerTokenType.WORD, "like"))};

    /** Маска конфигурации фильтра [(?word?)?]*/
    private static final Conditional[] FILTER_MASK_2 = new Conditional[] {
            new ConditionEquals(new LexerTokenMaskImpl(LexerTokenType.CONTROL, "(")),
            new ConditionOrIn(new LexerTokenMaskImpl(LexerTokenType.WORD, null), new LexerTokenMaskImpl(LexerTokenType.CONTROL, "("))};

    /** Маска конфигурации сортировки [^?word]*/
    private static final Conditional[] SORTS_MASK = new Conditional[] {new ConditionOrIn(new LexerTokenMaskImpl(LexerTokenType.WORD, null),
            new LexerTokenMaskImpl(LexerTokenType.SPECIAL, "^")), new ConditionOrIn(new LexerTokenMaskImpl(LexerTokenType.WORD, null),
            new LexerTokenMaskImpl(LexerTokenType.SEPARATOR, ","), new LexerTokenMaskImpl(LexerTokenType.CONTROL, "]"))};

    /** Размер буфера для чтения токенов */
    private static final int READ_BUFFER_SIZE = 3;

    /** Содержимое оператора */
    private static final Set<String> OPERATOR_CONTENTS = Set.of("!=", "=", "<", ">", "<=", ">=", "not in", "in", "like", "not like");

    /** Содержимое оператора, которое говорит о том, что дальше может следовать вторая часть оператора */
    private static final Set<String> OPERATOR_CONTINUE_CONTENTS = Set.of("!", "<", ">", "not");

    /** Шаблоны форматирования дуэтов операторов */
    private static final String[] DUET_OPERATORS_PATTERNS = new String[] { "%s%s", "%s %s" };

    /** Фабрика построителей фильтра */
    private final FilterBuilderFactory factory;

    /**
     * Конструктор
     * @param lexer лексический анализатор
     */
    public FilterParser(Lexer lexer, FilterBuilderFactory factory) {
        super(lexer);
        this.factory = factory;
    }

    @Override
    public Filter parse() {
        var builder = factory.create();
        parseBlockList(builder);
        return builder.build();
    }

    /**
     * Выполняет разбор блоков по маскам
     * @param builder построитель фильтра
     */
    private void parseBlockList(FilterBuilder builder) {
        while (true) {
            readIntoBuffer(1);
            if (!compareConditional(NEXT_GROUP_CONDITION)) {
                return;
            }
            flushBuffer(1);
            parse(builder);
            validate(requireNext(), LexerTokenType.CONTROL, "]");
        }
    }

    /**
     * Разбирает следующий блок
     * @param builder построитель фильтра
     */
    private void parse(FilterBuilder builder) {
        readIntoBuffer(READ_BUFFER_SIZE);
        if (compareConditional(PAGE_CONFIG_MASK)) {
            parsePageCount(builder);
        } else if (compareConditional(FILTER_MASK_1) || compareConditional(FILTER_MASK_2)) {
            parseCriteria(builder);
        } else if (compareConditional(SORTS_MASK)) {
            parseSorts(builder);
        } else {
            throw new ParserException(String.format("Неизвестный тип блока на позиции %s: '%s'", getFirst().getPosition(),
                    getFirst().getAsString()));
        }
    }

    /**
     * Разбирает конфигурацию пагинации фильтра
     * @param builder построитель фильтра
     */
    private void parsePageCount(FilterBuilder builder) {
        builder.page(validate(requireNext(), LexerTokenType.DIGIT, null).getAsInt());
        validate(requireNext(), LexerTokenType.SEPARATOR, ",");
        builder.pageSize(validate(requireNext(), LexerTokenType.DIGIT, null).getAsInt());
    }

    /**
     * Разбирает список критериев фильтра
     * @param builder построитель фильтра
     */
    private void parseCriteria(FilterBuilder builder) {
        var criteria = new ArrayList<FilterCriteria>();
        builder.criteria(criteria);
        parseCriteriaGroup(criteria, GroupOperator.OR);
    }

    /**
     * Разбирает группу критериев фильтра
     * @param criteria      список критериев
     * @param groupOperator оператор группы
     */
    private void parseCriteriaGroup(List<FilterCriteria> criteria, GroupOperator groupOperator) {
        while (true) {
            criteria.add(parseFieldCriteria());
            readIntoBuffer(1);
            if (!compareConditional(NEXT_LOGICAL_CONDITION)) {
                return;
            }
            var token = requireNext();
            if ("&".equals(token.getAsString())) {
                parseAndCriteriaGroup(criteria);
                readIntoBuffer(1);
                if (!compareConditional(NEXT_LOGICAL_CONDITION)) {
                    return;
                }
            } else if (groupOperator == GroupOperator.AND) {
                return;
            }
        }
    }

    /**
     * Разбирает группу критериев фильтра с оператором и
     * @param criteria список критериев
     */
    private void parseAndCriteriaGroup(List<FilterCriteria> criteria) {
        var lastIndex = criteria.size() - 1;
        var andCriteriaList = new ArrayList<FilterCriteria>();
        var andCriteria = new FilterGroupCriteriaImpl(GroupOperator.AND, andCriteriaList);
        var last = criteria.get(lastIndex);
        andCriteriaList.add(last);
        criteria.set(lastIndex, andCriteria);
        parseCriteriaGroup(andCriteriaList, andCriteria.getOperator());
    }

    /**
     * Разбирает критерий фильтра
     * @return критерий фильтра
     */
    private FilterCriteria parseFieldCriteria() {
        readIntoBuffer(1);
        if (compareConditional(NEXT_SUB_GROUP_CONDITION)) {
            flushBuffer(1);
            var criteria = parseSubCriteria();
            validate(requireNext(), LexerTokenType.CONTROL, ")");
            return criteria;
        } else {
            return FilterFieldCriteria.convertible(parseFieldName(), parseOperator(), parseValue());
        }
    }

    /**
     * Разбирает группу критериев фильтра с оператором или
     * @return подгруппа критериев
     */
    private FilterCriteria parseSubCriteria() {
        var orCriteriaList = new ArrayList<FilterCriteria>();
        var orCriteria = new FilterGroupCriteriaImpl(GroupOperator.OR, orCriteriaList);
        parseCriteriaGroup(orCriteriaList, orCriteria.getOperator());
        // Небольшая оптимизация (если например в подгруппе только операторы and)
        if (orCriteriaList.size() == 1 && orCriteriaList.get(0) instanceof FilterGroupCriteria fgc) {
            return fgc;
        }
        return orCriteria;
    }

    /**
     * Разбирает наименование поля критерия фильтра
     * @return наименование критерия фильтра
     */
    private String parseFieldName() {
        return requireNext().getAsString();
    }

    /**
     * Разбирает оператор критерия фильтра
     * @return оператор критерия фильтра
     */
    private String parseOperator() {
        var part1 = requireNext();
        if (OPERATOR_CONTINUE_CONTENTS.contains(part1.getAsString())) {
            readIntoBuffer(1);
            var part2 = getFirst();
            if (part2 != null) {
                for (var pattern: DUET_OPERATORS_PATTERNS) {
                    var duet = String.format(pattern, part1.getAsString(), part2.getAsString());
                    if (OPERATOR_CONTENTS.contains(duet)) {
                        flushBuffer(1);
                        return duet;
                    }
                }
            }
        }
        return part1.getAsString();
    }

    /**
     * Разбирает значение критерия фильтра
     * @return значение критерия фильтра
     */
    private Object parseValue() {
        var next = requireNext();
        var type = next.getType();
        if (type == LexerTokenType.WORD) {
            switch (next.getAsString()) {
                case "null": return null;
                case "true": return true;
                case "false": return false;
                default: break;
            }
        }
        if (type == LexerTokenType.CONTROL && "{".equals(next.getAsString())) {
            var arr = parseArrayValue();
            validate(requireNext(), LexerTokenType.CONTROL, "}");
            return arr;
        }
        return switch (type) {
            case CONTROL, SEPARATOR, SPECIAL, OTHER, WORD ->
                    throw new ParserException(String.format("Неожиданный токен %s '%s' в позиции %s. Ожидалось 'null', 'DIGIT', 'STRING', 'CONTROL'",
                            type.name(), next.getAsString(), next.getPosition()));
            case DIGIT -> next.getAsNumber();
            case STRING -> next.getAsString();
        };
    }

    /**
     * Выполняет разбор значение списка
     * @return разобранное значение списка
     */
    private List<Object> parseArrayValue() {
        var result = new ArrayList<>();
        while (true) {
            result.add(parseValue());
            readIntoBuffer(1);
            if (compareConditional(COMMA_SEPARATOR_CONDITION)) {
                flushBuffer(1);
            } else {
                break;
            }
        }
        return result;
    }

    /**
     * Разбирает параметры сортировки фильтра
     * @param builder построитель фильтра
     */
    private void parseSorts(FilterBuilder builder) {
        var sorts = new ArrayList<FilterSort>();
        while (true) {
            sorts.add(parseFilterSort());
            readIntoBuffer(1);
            if (compareConditional(COMMA_SEPARATOR_CONDITION)) {
                flushBuffer(1);
            } else {
                break;
            }
        }
        builder.sorts(sorts);
    }

    /**
     * Выполняет разбор условия сортировки
     * @return условие сортировки фильтра
     */
    private FilterSort parseFilterSort() {
        var direction = SortDirection.DESC;
        readIntoBuffer(1);
        if (compareConditional(IS_ASC_SORT_CONDITION)) {
            direction = SortDirection.ASC;
            flushBuffer(1);
        }
        return new FilterSortImpl(direction, requireNext().getAsString());
    }
}