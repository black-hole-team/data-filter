package team.blackhole.data.filter.parser;

import team.blackhole.data.filter.lexer.Lexer;
import team.blackhole.data.filter.lexer.LexerToken;
import team.blackhole.data.filter.lexer.LexerTokenType;
import team.blackhole.data.filter.parser.conditional.Conditional;

import java.util.LinkedList;

/**
 * Класс для разбора с буферизированным чтением токенов
 * @param <T> тип результата разбора
 */
public abstract class BufferedParser<T> extends LexerParser<T> {

    /** Буфер для прочитанных токенов */
    private final LinkedList<LexerToken> tokens = new LinkedList<>();

    /**
     * Конструктор
     * @param lexer лексический анализатор
     */
    protected BufferedParser(Lexer lexer) {
        super(lexer);
    }

    @Override
    protected LexerToken getNext() {
        if (!tokens.isEmpty()) {
            return tokens.removeLast();
        }
        return super.getNext();
    }

    /**
     * Возвращает первый токен из буфера
     * @return первый токен из буфера
     */
    protected LexerToken getFirst() {
        return tokens.getFirst();
    }

    /**
     * Выполняет проверку токена на соответствие условиям по типу и содержимому
     * @param token   токен
     * @param type    ожидаемый тип или {@code null}
     * @param content ожидаемое содержимое или {@code null}
     * @return токен
     */
    protected LexerToken validate(LexerToken token, LexerTokenType type, String content) {
        if (type != null) {
            validateTokenType(token, type);
        }
        if (content != null) {
            validateTokenContent(token, content);
        }
        return token;
    }

    /**
     * Проверяет условные операторы на
     * @param conditionals список условий для соблюдения
     * @return true если список условий попадает под маску
     */
    protected boolean compareConditional(Conditional... conditionals) {
        var iterator = tokens.descendingIterator();
        for (Conditional value : conditionals) {
            if (!iterator.hasNext() || !value.valid(iterator.next())) {
                return false;
            }
        }
        return true;
    }

    /**
     * Очищает буфер
     * @param flushCount количество токенов для очищения
     */
    protected void flushBuffer(int flushCount) {
        if (flushCount > tokens.size()) {
            flushCount = tokens.size();
        }
        for (var i = 0; i < flushCount; i++) {
            tokens.removeLast();
        }
    }

    /**
     * Читает следующие {@code count} токенов в буфер
     * @param count количество токенов для запроса
     */
    protected void readIntoBuffer(int count) {
        count = Math.max(count - tokens.size(), 0);
        for (var i = 0; i < count; i++) {
            var next = super.getNext();
            if (next == null) {
                break;
            }
            tokens.addFirst(next);
        }
    }
}
