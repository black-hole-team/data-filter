package team.blackhole.data.filter.exception;

/**
 * Исключение разбора
 */
public class ParserException extends FilterException {

    /**
     * Конструктор исключения разбора
     */
    public ParserException() {
        super();
    }

    /**
     * Конструктор исключения разбора
     *
     * @param message сообщение об ошибке
     */
    public ParserException(String message) {
        super(message);
    }

    /**
     * Конструктор исключения разбора
     *
     * @param message сообщение об ошибке
     * @param cause   причина (которая сохраняется для последующего извлечения методом getCause())
     */
    public ParserException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Конструктор исключения разбора
     *
     * @param cause причина (которая сохраняется для последующего извлечения методом getCause())
     */
    public ParserException(Throwable cause) {
        super(cause);
    }
}
