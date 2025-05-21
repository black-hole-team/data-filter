package team.blackhole.data.filter.lexer;

import java.util.Iterator;

/**
 * Интерфейс лексического анализа фильтра
 */
public interface Lexer extends Iterator<LexerToken> {

    /**
     * Сбросить состояние лексического анализатора
     */
    void reset();

}
