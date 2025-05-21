package team.blackhole.data.filter.writer;

import org.apache.commons.text.StringEscapeUtils;
import team.blackhole.data.filter.*;

import java.util.List;

/**
 * Реализация интерфейса {@link FilterWriter}
 */
public class FilterWriterImpl implements FilterWriter {

    @Override
    public String write(Filter filter) {
        var builder = new StringBuilder(String.format("[%s, %s]", filter.getPage(), filter.getPageSize()));
        if (filter.getCriteria() != null && !filter.getCriteria().isEmpty()) {
            writeRootCriteria(builder, filter.getCriteria());
        }
        if (filter.getSorts() != null && !filter.getSorts().isEmpty()) {
            writeFilterSort(builder, filter.getSorts());
        }
        return builder.toString();
    }

    /**
     * Выводит корневой список критериев в формате фильтра
     * @param builder  построитель строки
     * @param criteria список критериев фильтра
     */
    private void writeRootCriteria(StringBuilder builder, List<FilterCriteria> criteria) {
        builder.append("[");
        writeCriteria(builder, criteria, GroupOperator.OR);
        builder.append("]");
    }

    /**
     * Выводит список критериев в формате фильтра
     * @param builder  построитель строки
     * @param criteria список критериев фильтра
     * @param operator оператор группы критериев
     */
    private void writeCriteria(StringBuilder builder, List<FilterCriteria> criteria, GroupOperator operator) {
        for (var i = 0; i < criteria.size(); i++) {
            if (i > 0) {
                builder.append(operator == GroupOperator.OR ? " | " : " & ");
            }
            writeCriteria(builder, criteria.get(i));
        }
    }

    /**
     * Выводит значение критерия в формате фильтра
     * @param builder  построитель строки
     * @param criteria критерий для вывода
     */
    private void writeCriteria(StringBuilder builder, FilterCriteria criteria) {
        if (criteria instanceof FilterFieldCriteria ffc) {
            writeValue(builder.append(ffc.getField()).append(" ").append(ffc.getOperator()).append(" "), ffc.getValue());
        } else if (criteria instanceof FilterGroupCriteria fgc) {
            builder.append("(");
            writeCriteria(builder, fgc.getCriteria(), fgc.getOperator());
            builder.append(")");
        }
    }

    /**
     * Выводит значение фильтра в формате фильтра
     * @param builder построитель строки
     * @param value   значение для вывода
     */
    private void writeValue(StringBuilder builder, Object value) {
        if (value instanceof String str) {
            builder.append('"').append(StringEscapeUtils.escapeJson(str)).append('"');
        } else if (value instanceof Iterable<?> iter) {
            var iterator = iter.iterator();
            builder.append("{");
            while (iterator.hasNext()) {
                var current = iterator.next();
                writeValue(builder, current);
                if (iterator.hasNext()) {
                    builder.append(", ");
                }
            }
            builder.append("}");
        } else {
            builder.append(value);
        }
    }

    /**
     * Выводит сортировку фильтра в текстовом формате
     * @param builder построитель строки
     * @param sorts   список сортировок
     */
    private void writeFilterSort(StringBuilder builder, List<FilterSort> sorts) {
        builder.append("[");
        for (var i = 0; i < sorts.size(); i++) {
            if (i > 0) {
                builder.append(", ");
            }
            var current = sorts.get(i);
            if (current.getDirection() == SortDirection.ASC) {
                builder.append("^");
            }
            builder.append(current.getField());
        }
        builder.append("]");
    }
}
