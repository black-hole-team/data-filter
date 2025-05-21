package team.blackhole.data.filter.lexer;

import lombok.Getter;

import java.util.Set;

/**
 * Тип токена лексического анализатора
 */
@Getter
public enum LexerTokenType {

    /** Тип токена - контролирующий символ */
    CONTROL(true, '[', ']', '{', '}', '(', ')'),

    /** Тип токена - специальный символ */
    SPECIAL(true, '=', '&', '|', '>', '<', '^', '*', '/', '!'),

    /** Тип токена - цифра */
    DIGIT(true, '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '.'),

    /** Тип токена - слово */
    WORD(true, 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W',
            'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v',
            'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '.', '_'),

    /** Тип токена - строка */
    STRING(false, '"', '\'', '`'),

    /** Тип токена - разделитель */
    SEPARATOR(true, ',', ';', ' '),

    /** Другое */
    OTHER(true);

    /** Признак того, что в содержимое токена нужно включать первый символ */
    private final boolean includeFirstCharacter;

    /** Набор символов, которые говорят лексическому анализатору присвоить токену соответствующий тип */
    private final Set<Character> triggerChars;

    /**
     * Конструктор
     * @param includeFirstCharacter признак того, что в содержимое токена нужно включать первый символ
     * @param triggerChars          набор символов, которые говорят лексическому анализатору присвоить токену соответствующий тип
     */
    LexerTokenType(boolean includeFirstCharacter, Character... triggerChars) {
        this.includeFirstCharacter = includeFirstCharacter;
        this.triggerChars = Set.of(triggerChars);
    }
}
