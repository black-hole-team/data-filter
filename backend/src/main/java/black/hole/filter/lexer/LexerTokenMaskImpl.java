package black.hole.filter.lexer;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class LexerTokenMaskImpl implements LexerTokenMask {

    /** Тип токена */
    private final LexerTokenType type;

    /** Строковое содержимое токена */
    private final String content;
}
