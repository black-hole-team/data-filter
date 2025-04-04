package black.hole.filter.lexer;

import black.hole.filter.exception.LexerException;
import lombok.Getter;

import java.io.IOException;
import java.io.Reader;
import java.util.NoSuchElementException;

/**
 * Реализация интерфейса {@link Lexer}
 */
@Getter
public class LexerImpl implements Lexer {

    /** Конфигурация по умолчанию */
    private static final LexerConfiguration DEFAULT_CONFIGURATION = new LexerConfigurationImpl(true);

    /** Входящая строка для разбора */
    private final Reader reader;

    /** Конфигурация лексического анализатора */
    private final LexerConfiguration configuration;

    /** Следующий токен */
    private LexerToken nextToken;

    /** Последний обработанный символ */
    private Character lastHandledCharacter;

    /** Индекс каретки */
    private int caret;

    /**
     * Конструктор без конфигурации
     * @param reader        читатель
     * @param configuration конфигурация лексического анализатора
     */
    public LexerImpl(Reader reader, LexerConfiguration configuration) {
        this.reader = reader;
        this.configuration = configuration;
    }

    /**
     * Конструктор без конфигурации
     * @param reader читатель
     */
    public LexerImpl(Reader reader) {
        this(reader, DEFAULT_CONFIGURATION);
    }

    @Override
    public boolean hasNext() {
        if (nextToken != null) {
            return true;
        }
        do {
            var nextChar = nextChar();
            if (nextChar == null) {
                return false;
            }
            receiveNextToken(nextChar);
        } while (nextToken != null && !configuration.isTokenAcceptable(nextToken));
        return nextToken != null;
    }

    @Override
    public LexerToken next() {
        if (nextToken == null) {
            throw new NoSuchElementException();
        }
        var token = nextToken;
        nextToken = null;
        return token;
    }

    @Override
    public void reset() {
        try {
            lastHandledCharacter = null;
            nextToken = null;
            reader.reset();
        } catch (IOException e) {
            throw new LexerException("Ошибка при сбросе читателя", e);
        }
    }

    /**
     * Присваивает следующему токену значение, полученное из входящего потока
     * @param nextChar начальный символ токена
     */
    private void receiveNextToken(Character nextChar) {
        var type = getTokenTypeForChar(nextChar);
        if (type == LexerTokenType.OTHER) {
            nextToken = null;
        } else {
            nextToken = getTokenByTriggerChars(type, nextChar);
        }
    }

    /**
     * Возвращает токен по триггерным символам типа токена
     * @param type        тип токена
     * @param triggerChar триггерный символ
     * @return токен
     */
    private LexerTokenImpl getTokenByTriggerChars(LexerTokenType type, char triggerChar) {
        var content = new StringBuilder();
        if (type.isIncludeFirstCharacter()) {
            content.append(triggerChar);
        }
        for (Character character = nextChar(), prev = character; character != null; prev = character,
                character = nextChar()) {
            if (configuration.isNeedCompleteToken(type, triggerChar, character, prev)) {
                if (type.isIncludeFirstCharacter()) {
                    markCharacterAsUnhandled(character);
                }
                break;
            } else {
                content.append(character);
            }
        }
        if (content.isEmpty()) {
            return null;
        } else {
            return new LexerTokenImpl(type, caret - 1 - content.length(), content.toString());
        }
    }

    /**
     * Помечает символ как необработанный (при следующей итерации он будет возвращен)
     * @param character символ
     */
    private void markCharacterAsUnhandled(char character) {
        this.lastHandledCharacter = character;
    }

    /**
     * Возвращает следующий символ принимающего читателя
     * @return следующий символ принимающего читателя
     */
    private Character nextChar() {
        if (this.lastHandledCharacter != null) {
            var character = this.lastHandledCharacter;
            this.lastHandledCharacter = null;
            return character;
        }
        try {
            int read = reader.read();
            if (read == -1) {
                return null;
            }
            caret++;
            return (char) read;
        } catch (IOException e) {
            throw new LexerException("Ошибка при чтении данных", e);
        }
    }

    /**
     * Получить тип токена для символа
     * @param currentChar символ
     * @return тип токена
     */
    private static LexerTokenType getTokenTypeForChar(char currentChar) {
        for (var type : LexerTokenType.values()) {
            if (type.getTriggerChars().contains(currentChar)) {
                return type;
            }
        }
        return LexerTokenType.OTHER;
    }
}
