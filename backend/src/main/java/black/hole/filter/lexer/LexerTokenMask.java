package black.hole.filter.lexer;

/**
 * Маска токена лексического анализатора
 */
public interface LexerTokenMask {

    /**
     * Возвращает тип маски токена
     * @return тип маски токена
     */
    LexerTokenType getType();

    /**
     * Возвращает содержимое маски токена
     * @return содержимое маски токена
     */
    String getContent();
}
