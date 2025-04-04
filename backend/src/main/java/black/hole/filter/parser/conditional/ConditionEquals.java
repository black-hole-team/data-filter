package black.hole.filter.parser.conditional;

import black.hole.filter.lexer.LexerToken;
import black.hole.filter.lexer.LexerTokenMask;

/**
 * Условие проверки токена на равенство
 */
public class ConditionEquals implements Conditional {

    /** Маска для сравнения */
    private final LexerTokenMask mask;

    /**
     * Конструктор
     * @param mask маска для сравнения
     */
    public ConditionEquals(LexerTokenMask mask) {
        this.mask = mask;
    }

    @Override
    public boolean valid(LexerToken token) {
        var valid = true;
        if (mask.getContent() != null) {
            valid = mask.getContent().equals(token.getAsString());
        }
        if (valid && mask.getType() != null) {
            valid = mask.getType() == token.getType();
        }
        return valid;
    }
}
