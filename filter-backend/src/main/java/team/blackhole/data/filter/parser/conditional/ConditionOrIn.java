package team.blackhole.data.filter.parser.conditional;

import team.blackhole.data.filter.lexer.LexerToken;
import team.blackhole.data.filter.lexer.LexerTokenMask;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Проверка на соответствие одному из условий
 */
public class ConditionOrIn implements Conditional {

    /** Маски для сравнения */
    private final Set<String> masks;

    /**
     * Конструктор
     * @param masks Маски для сравнения
     */
    public ConditionOrIn(LexerTokenMask... masks) {
        this.masks = Arrays.stream(masks).map(e -> String.format("%s:%s", e.getType(), e.getContent())).collect(Collectors.toSet());
    }

    @Override
    public boolean valid(LexerToken token) {
        return masks.contains(String.format("%s:%s", token.getType().name(), token.getAsString())) || masks.contains("null:" + token.getAsString()) ||
                masks.contains(token.getType().name() + ":null");
    }
}
