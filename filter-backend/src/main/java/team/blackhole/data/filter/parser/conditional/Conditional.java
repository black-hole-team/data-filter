package team.blackhole.data.filter.parser.conditional;

import team.blackhole.data.filter.lexer.LexerToken;

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
