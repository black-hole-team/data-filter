package black.hole.filter.lexer;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Реализация интерфейса {@link LexerToken}
 */
@AllArgsConstructor
public class LexerTokenImpl implements LexerToken {

    /** Тип токена */
    @Getter
    private final LexerTokenType type;

    /** Позиция токена */
    @Getter
    private final int position;

    /** Строковое содержимое токена */
    private final String content;

    @Override
    public String getAsString() {
        return content;
    }

    @Override
    public String toString() {
        return String.format("[%s] %s", type.name(), content);
    }
}
