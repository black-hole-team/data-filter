package black.hole.filter.parser.conditional;

import black.hole.filter.lexer.LexerToken;

/**
 * Условие проверки токена
 */
public interface Conditional {

    /**
     * Проверяет токен на соответствие внутренним условиям
     * @param token токен
     * @return {@code true} если токен соответствует внутренним условиям
     */
    boolean valid(LexerToken token);
}
