package black.hole.filter;

import black.hole.filter.builder.FilterBuilderFactory;
import black.hole.filter.lexer.*;
import black.hole.filter.writer.FilterWriter;
import lombok.RequiredArgsConstructor;

import java.io.Reader;

/**
 * Реализация интерфейса {@link FilterFacade}
 */
@RequiredArgsConstructor
public class FilterFacadeImpl implements FilterFacade {

    /** Конфигурация лексического анализатора */
    private static final LexerConfiguration FILTER_LEXER_CONFIGURATION = new LexerConfigurationImpl(
            true, new LexerTokenMaskImpl(LexerTokenType.SEPARATOR, " "));

    /** Экземпляр писателя фильтра */
    private final FilterWriter filterWriter;

    /** Фабрика построителей фильтра */
    private final FilterBuilderFactory filterBuilderFactory;

    @Override
    public Filter read(Reader input) {
        return new FilterParser(new LexerImpl(input, FILTER_LEXER_CONFIGURATION), filterBuilderFactory).parse();
    }

    @Override
    public String write(Filter filter) {
        return filterWriter.write(filter);
    }
}
