package black.hole.filter.handler;

import black.hole.filter.handler.entity.EntityMetadata;

/**
 * Фабрика персонализированных обработчиков
 */
public interface CustomCriteriaHandlerFactory {

    /**
     * Создает персонализированный обработчик критериев для метаданных `metadata`
     * @param metadata метаданные сущности
     * @return персонализированный обработчик
     */
    CustomCriteriaHandler create(EntityMetadata<?> metadata);

    /**
     * Может ли фабрика принимать переданные метаданные
     * @param metadata метаданные
     * @return {@code true} если может, {@code false} если иначе.
     */
    boolean acceptable(EntityMetadata<?> metadata);
}
