package black.hole.filter.parser;

import black.hole.filter.exception.ParserException;
import black.hole.filter.lexer.Lexer;
import black.hole.filter.lexer.LexerToken;
import black.hole.filter.lexer.LexerTokenType;
import lombok.RequiredArgsConstructor;

/**
 * Класс разбора, разбирающий токены принятые от лексического анализатора
 * @param <T> тип результата разбора
 */
@RequiredArgsConstructor
public abstract class LexerParser<T> implements Parser<T> {

    /** Лексический анализатор */
    private final Lexer lexer;

    /**
     * Возвращает следующий токен.
     * @return токен или {@code null} если следующий токен не доступен
     */
    protected LexerToken getNext() {
        if (!lexer.hasNext()) {
            return null;
        }
        return lexer.next();
    }

    /**
     * Получает следующий токен, если он доступен, то выбрасывает исключение.
     * @return токен
     */
    protected LexerToken requireNext() {
        var token = getNext();
        validateExists(token);
        return token;
    }

    /**
     * Проверяет получение токена
     * @param token токен
     */
    protected static void validateExists(LexerToken token) {
        if (token == null) {
            throw new ParserException("Неожиданный конец ввода, проверьте завершенность выражения");
        }
    }

    /**
     * Проверяет тип токена
     * @param token токен
     * @param type  варианты возможных типов токена
     */
    protected static void validateTokenType(LexerToken token, LexerTokenType type) {
        if (type != token.getType()) {
            throw new ParserException(String.format("Неожиданный тип токена '%s', ожидалось '%s' в позиции %s",
                    token.getType().name(), type.name(), token.getPosition()));
        }
    }

    /**
     * Проверяет содержимое токена
     * @param token           токен
     * @param expectedContent варианты возможного содержимого токена
     */
    protected static void validateTokenContent(LexerToken token, String expectedContent) {
        if (!token.getAsString().equals(expectedContent)) {
            throw new ParserException(String.format("Неожиданное содержимое '%s', ожидалось '%s' в позиции %s", token.getAsString(),
                    expectedContent, token.getPosition()));
        }
    }
}
