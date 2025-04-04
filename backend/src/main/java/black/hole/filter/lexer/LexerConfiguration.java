package black.hole.filter.lexer;

/**
 * Конфигурация лексического анализатора
 */
public interface LexerConfiguration {

    /**
     * Возвращает признак того, что лексический анализатор может выдавать
     * @param token токен для проверки
     * @return признак того, что лексический анализатор может выдавать
     */
    boolean isTokenAcceptable(LexerToken token);

    /**
     * Возвращает признак необходимости завершить обработку токена
     * @param type        тип токена
     * @param triggerChar триггерный символ для токена
     * @param currentChar текущий символ
     * @param prevChar    предыдущий символ
     * @return {@code true} если необходимо завершить обработку токена
     */
    boolean isNeedCompleteToken(LexerTokenType type, char triggerChar, char currentChar, char prevChar);
}
