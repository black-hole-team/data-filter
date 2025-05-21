package team.blackhole.data.filter.exception;

/**
 * Исключение модуля фильтрации
 */
public class FilterException extends RuntimeException {

    /**
     * Конструктор исключения фильтрации
     */
    public FilterException() {
        super();
    }

    /**
     * Конструктор исключения фильтрации
     * @param message сообщение об ошибке
     */
    public FilterException(String message) {
        super(message);
    }

    /**
     * Конструктор исключения фильтрации
     * @param message сообщение об ошибке
     * @param cause   причина (которая сохраняется для последующего извлечения методом getCause())
     */
    public FilterException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Конструктор исключения фильтрации
     * @param cause причина (которая сохраняется для последующего извлечения методом getCause())
     */
    public FilterException(Throwable cause) {
        super(cause);
    }
}