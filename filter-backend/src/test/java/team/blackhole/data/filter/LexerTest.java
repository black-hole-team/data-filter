package team.blackhole.data.filter;

import org.junit.jupiter.api.Test;
import team.blackhole.data.filter.lexer.*;

import java.io.StringReader;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Тест контракта лексического анализа
 */
class LexerTest {

    /**
     * Тест разбора выражения сортировки
     */
    @Test
    void FilterLexerTestSortExpressionsTest() {
        assertLexerOutput(new LexerImpl(new StringReader("[^field1, field2]")),
                new LexerTokenMaskImpl(LexerTokenType.CONTROL, "["),
                new LexerTokenMaskImpl(LexerTokenType.SPECIAL, "^"),
                new LexerTokenMaskImpl(LexerTokenType.WORD, "field1"),
                new LexerTokenMaskImpl(LexerTokenType.SEPARATOR, ","),
                new LexerTokenMaskImpl(LexerTokenType.SEPARATOR, " "),
                new LexerTokenMaskImpl(LexerTokenType.WORD, "field2"),
                new LexerTokenMaskImpl(LexerTokenType.CONTROL, "]"));
    }

    /**
     * Тест разбора выражения пагинации
     */
    @Test
    void FilterLexerTestPageExpressionsTest() {
        assertLexerOutput(new LexerImpl(new StringReader("[0,500]")),
                new LexerTokenMaskImpl(LexerTokenType.CONTROL, "["),
                new LexerTokenMaskImpl(LexerTokenType.DIGIT, "0"),
                new LexerTokenMaskImpl(LexerTokenType.SEPARATOR, ","),
                new LexerTokenMaskImpl(LexerTokenType.DIGIT, "500"),
                new LexerTokenMaskImpl(LexerTokenType.CONTROL, "]"));
    }

    /**
     * Тест разбора выражения фильтра
     */
    @Test
    void FilterLexerTestLogicalExpressionsTest() {
        assertLexerOutput(new LexerImpl(new StringReader("[field1 > 55.01 & field2 < 15.05 | field3 = 'keyboardcat']")),
                new LexerTokenMaskImpl(LexerTokenType.CONTROL, "["),
                new LexerTokenMaskImpl(LexerTokenType.WORD, "field1"),
                new LexerTokenMaskImpl(LexerTokenType.SEPARATOR, " "),
                new LexerTokenMaskImpl(LexerTokenType.SPECIAL, ">"),
                new LexerTokenMaskImpl(LexerTokenType.SEPARATOR, " "),
                new LexerTokenMaskImpl(LexerTokenType.DIGIT, "55.01"),
                new LexerTokenMaskImpl(LexerTokenType.SEPARATOR, " "),
                new LexerTokenMaskImpl(LexerTokenType.SPECIAL, "&"),
                new LexerTokenMaskImpl(LexerTokenType.SEPARATOR, " "),
                new LexerTokenMaskImpl(LexerTokenType.WORD, "field2"),
                new LexerTokenMaskImpl(LexerTokenType.SEPARATOR, " "),
                new LexerTokenMaskImpl(LexerTokenType.SPECIAL, "<"),
                new LexerTokenMaskImpl(LexerTokenType.SEPARATOR, " "),
                new LexerTokenMaskImpl(LexerTokenType.DIGIT, "15.05"),
                new LexerTokenMaskImpl(LexerTokenType.SEPARATOR, " "),
                new LexerTokenMaskImpl(LexerTokenType.SPECIAL, "|"),
                new LexerTokenMaskImpl(LexerTokenType.SEPARATOR, " "),
                new LexerTokenMaskImpl(LexerTokenType.WORD, "field3"),
                new LexerTokenMaskImpl(LexerTokenType.SEPARATOR, " "),
                new LexerTokenMaskImpl(LexerTokenType.SPECIAL, "="),
                new LexerTokenMaskImpl(LexerTokenType.SEPARATOR, " "),
                new LexerTokenMaskImpl(LexerTokenType.STRING, "keyboardcat"),
                new LexerTokenMaskImpl(LexerTokenType.CONTROL, "]"));
    }

    /**
     * Сопоставить вывод лексического анализа с эталоном
     * @param lexer  контракт лексического анализа
     * @param tokens эталон токенов
     */
    void assertLexerOutput(Lexer lexer, LexerTokenMask... tokens) {
        int i = 0;
        while (lexer.hasNext()) {
            var next = lexer.next();
            var current = tokens[i++];
            assertEquals(current.getType(), next.getType(),
                    String.format("Не совпадает тип токена для токена %s. Ожидалось %s, но получено %s", i,
                            current.getType(), next.getType()));
            assertEquals(current.getContent(), next.getAsString(),
                    String.format("Не совпадает содержимое токена для токена %s. Ожидалось %s, но получено %s", i,
                            current.getContent(), next.getAsString()));
        }
        assertEquals(tokens.length, i, "Не совпадает длинна выходящих токенов лексического анализатора");
    }
}