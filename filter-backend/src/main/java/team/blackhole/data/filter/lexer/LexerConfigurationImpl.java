package team.blackhole.data.filter.lexer;

import java.util.Arrays;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Конфигурация лексического анализатора
 */
public class LexerConfigurationImpl implements LexerConfiguration {

    /** Символ экранирования */
    private static final char ESCAPE_CHAR = '\\';

    /** Список принимаемых типов токенов и содержимого токена */
    private final Set<String> accepted;

    /** Признак того, что переданные токены должны исключаться */
    private final boolean exclude;

    /**
     * Конструктор
     * @param exclude признак того, что переданные токены должны исключаться
     * @param tokens  список токенов, которые принимает лексический анализатор
     */
    public LexerConfigurationImpl(boolean exclude, LexerTokenMask...tokens) {
        this.accepted = Arrays.stream(tokens).map(token -> token.getType().name() + "_" + token.getContent())
                .collect(Collectors.toSet());
        this.exclude = exclude;
    }

    @Override
    public boolean isTokenAcceptable(LexerToken token) {
        return exclude != accepted.contains(token.getType().name() + "_" + token.getAsString());
    }

    @Override
    public boolean isNeedCompleteToken(LexerTokenType type, char triggerChar, char currentChar, char prevChar) {
        return switch (type) {
            case DIGIT, WORD -> !type.getTriggerChars().contains(currentChar);
            case STRING -> currentChar == triggerChar && !Objects.equals(prevChar, ESCAPE_CHAR);
            case CONTROL, SPECIAL, SEPARATOR, OTHER -> true;
        };
    }
}
