package black.hole.filter;

/**
 * Исключение при конвертации фильтра
 */
public class FilterConverterException extends RuntimeException {

    /**
     * Конструктор исключения конвертации фильтра
     */
    public FilterConverterException() {
        super();
    }

    /**
     * Конструктор исключения конвертации фильтра
     * @param message сообщение об ошибке
     */
    public FilterConverterException(String message) {
        super(message);
    }

    /**
     * Конструктор исключения конвертации фильтра
     * @param message сообщение об ошибке
     * @param cause   причина (которая сохраняется для последующего извлечения методом getCause())
     */
    public FilterConverterException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Конструктор исключения конвертации фильтра
     * @param cause причина (которая сохраняется для последующего извлечения методом getCause())
     */
    public FilterConverterException(Throwable cause) {
        super(cause);
    }
}
