package black.hole.filter.jpa;

import black.hole.filter.handler.CustomCriteriaHandler;
import black.hole.filter.handler.CustomCriteriaHandlerFactory;
import black.hole.filter.handler.entity.EntityMetadata;
import black.hole.filter.handler.entity.EntityMetadataProvider;
import black.hole.filter.jpa.converter.JpaFilterConverter;
import jakarta.persistence.EntityManager;
import lombok.NonNull;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactoryBean;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.data.repository.core.RepositoryInformation;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;

import java.io.Serializable;
import java.util.List;

/**
 * Бин фабрики репозиториев
 * Используются ленивые ссылки, чтобы контекст успел проинициализировать элементы конфигурации и не показывал предупреждения
 * @param <R> тип репозитория
 * @param <T> тип сущности репозитория
 * @param <I> тип идентификатора сущности репозитория
 */
public class ExtendedJpaRepositoryFactoryBean<R extends JpaRepository<T, I>, T, I extends Serializable>
        extends JpaRepositoryFactoryBean<R, T, I> {

    /** Ленивая ссылка на фабрику метаданных сущности */
    private final ObjectFactory<EntityMetadataProvider> entityMetadataFactory;

    /** Фабрики персонализированных обработчиков критериев */
    private final ObjectFactory<List<CustomCriteriaHandlerFactory>> handlerFactories;

    /**
     * Конструктор
     * @param repositoryInterface   интерфейс
     * @param entityMetadataFactory фабрика метаданных сущности
     * @param handlerFactories      фабрики персонализированных обработчиков критериев
     */
    public ExtendedJpaRepositoryFactoryBean(Class<? extends R> repositoryInterface,
                                            ObjectFactory<EntityMetadataProvider> entityMetadataFactory,
                                            @Autowired(required = false) ObjectFactory<List<CustomCriteriaHandlerFactory>> handlerFactories) {
        super(repositoryInterface);
        this.entityMetadataFactory = entityMetadataFactory;
        this.handlerFactories = handlerFactories;
    }

    @Override
    @NonNull
    protected RepositoryFactorySupport createRepositoryFactory(@NonNull EntityManager entityManager) {
        return new ExtendedJpaRepositoryFactory(entityManager);
    }

    /**
     * Фабрика jpa репозиториев
     */
    private class ExtendedJpaRepositoryFactory extends JpaRepositoryFactory {

        /**
         * Конструктор
         * @param entityManager менеджер сущностей
         */
        public ExtendedJpaRepositoryFactory(EntityManager entityManager) {
            super(entityManager);
        }

        @Override
        @NonNull
        protected Class<?> getRepositoryBaseClass(@NonNull RepositoryMetadata metadata) {
            return ExtendedJpaRepository.class;
        }

        @NonNull
        @Override
        protected JpaRepositoryImplementation<?, ?> getTargetRepository(RepositoryInformation information, @NonNull EntityManager entityManager) {
            return getScopedRepository(information.getDomainType(), entityManager);
        }

        /**
         * Возвращает список обработчиков для метаданных
         * @param metadata метаданные сущности
         * @return список кастомных обработчиков
         */
        private List<CustomCriteriaHandler> getHandlers(EntityMetadata<?> metadata) {
            return handlerFactories.getObject().stream()
                    .filter(e -> e.acceptable(metadata))
                    .map(e -> e.create(metadata)).toList();
        }

        /**
         * Возвращает реализацию репозитория для указанной сущности
         * @param type          тип сущности
         * @param entityManager менеджер сущностей
         * @return реализация репозитория
         * @param <Type> тип сущности
         * @param <Id>   тип идентификатора сущности
         */
        private <Type, Id extends Serializable> JpaRepositoryImplementation<?, ?> getScopedRepository(Class<Type> type, EntityManager entityManager) {
            var entityInformation = this.<Type, Id>getEntityInformation(type);
            var filterConverter = new JpaFilterConverter<>(getHandlers(entityMetadataFactory.getObject().get(type)), entityManager, type);
            return new ExtendedJpaRepositoryImpl<>(filterConverter, entityInformation, entityManager);
        }
    }
}