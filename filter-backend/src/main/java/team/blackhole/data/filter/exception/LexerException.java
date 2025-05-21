package team.blackhole.data.filter.exception;

/**
 * Исключение контракта лексического анализа
 */
public class LexerException extends FilterException {

    /**
     * Конструктор исключения контракта лексического анализа
     */
    public LexerException() {
        super();
    }

    /**
     * Конструктор исключения контракта лексического анализа
     *
     * @param message сообщение об ошибке
     */
    public LexerException(String message) {
        super(message);
    }

    /**
     * Конструктор исключения контракта лексического анализа
     *
     * @param message сообщение об ошибке
     * @param cause   причина (которая сохраняется для последующего извлечения методом getCause())
     */
    public LexerException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Конструктор исключения контракта лексического анализа
     *
     * @param cause причина (которая сохраняется для последующего извлечения методом getCause())
     */
    public LexerException(Throwable cause) {
        super(cause);
    }
}
