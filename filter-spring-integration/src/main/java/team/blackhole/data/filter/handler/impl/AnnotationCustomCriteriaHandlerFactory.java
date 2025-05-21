package team.blackhole.data.filter.handler.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import team.blackhole.data.filter.handler.CustomCriteriaHandler;
import team.blackhole.data.filter.handler.CustomCriteriaHandlerFactory;
import team.blackhole.data.filter.handler.entity.EntityFieldMetadataProvider;
import team.blackhole.data.filter.handler.entity.EntityMetadata;
import team.blackhole.data.filter.handler.entity.FilterableField;

import java.util.*;

/**
 * Фабрика обработчика кастомных критерив фильтрации на основе аннотаций
 */
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AnnotationCustomCriteriaHandlerFactory implements CustomCriteriaHandlerFactory {

    /** Поставщик метаданных полей сущности */
    private final EntityFieldMetadataProvider entityFieldMetadataProvider;

    @Override
    public CustomCriteriaHandler create(EntityMetadata<?> metadata) {
        return new AnnotationCustomCriteriaHandler(metadata);
    }

    @Override
    public boolean acceptable(EntityMetadata<?> metadata) {
        // Если сущность не содержит аннотации типа {@link FilterableField}, значит фильтрация разрешена по всем полям сущности
        return Arrays.stream(metadata.getOwner().getDeclaredFields()).anyMatch(e -> e.isAnnotationPresent(FilterableField.class));
    }

    /**
     * Обработчик кастомных критерив фильтрации на основе аннотаций
     */
    @RequiredArgsConstructor
    protected class AnnotationCustomCriteriaHandler implements CustomCriteriaHandler {

        /** Идентификатор обработчика */
        private final UUID id = UUID.randomUUID();

        /** Карта, где ключ это название поля, а значение это контракт доступа к значению поля */
        private final EntityMetadata<?> metadata;

        @Override
        public boolean canCandle(String field) {
            return entityFieldMetadataProvider.get(metadata, field).isAllowFiltering();
        }

        @Override
        public String getIdentifier() {
            return id + ":" + metadata.getIdentifier();
        }
    }
}
