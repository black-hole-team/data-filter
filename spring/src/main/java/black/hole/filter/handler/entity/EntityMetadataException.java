package black.hole.filter.handler.entity;

/**
 * Исключение при работе с метаданными сущности
 */
public class EntityMetadataException extends RuntimeException {

    /**
     * Конструктор исключения обработки сущности
     */
    public EntityMetadataException() {
        super();
    }

    /**
     * Конструктор исключения обработки сущности
     * @param message сообщение об ошибке
     */
    public EntityMetadataException(String message) {
        super(message);
    }

    /**
     * Конструктор исключения обработки сущности
     * @param message сообщение об ошибке
     * @param cause   причина (которая сохраняется для последующего извлечения методом getCause())
     */
    public EntityMetadataException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Конструктор исключения обработки сущности
     * @param cause причина (которая сохраняется для последующего извлечения методом getCause())
     */
    public EntityMetadataException(Throwable cause) {
        super(cause);
    }
}
