package team.blackhole.data.filter.mongo;

import team.blackhole.data.filter.handler.CustomCriteriaHandler;
import team.blackhole.data.filter.handler.CustomCriteriaHandlerFactory;
import team.blackhole.data.filter.handler.entity.EntityMetadata;
import team.blackhole.data.filter.handler.entity.EntityMetadataProvider;
import team.blackhole.data.filter.mongo.converter.MongoFilterConverter;
import lombok.NonNull;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mapping.context.MappingContext;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.mapping.MongoPersistentEntity;
import org.springframework.data.mongodb.core.mapping.MongoPersistentProperty;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.support.MappingMongoEntityInformation;
import org.springframework.data.mongodb.repository.support.MongoRepositoryFactory;
import org.springframework.data.mongodb.repository.support.MongoRepositoryFactoryBean;
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
public class ExtendedMongoRepositoryFactoryBean<R extends MongoRepository<T, I>, T, I extends Serializable>
        extends MongoRepositoryFactoryBean<R, T, I> {

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
    public ExtendedMongoRepositoryFactoryBean(Class<? extends R> repositoryInterface,
                                           ObjectFactory<EntityMetadataProvider> entityMetadataFactory,
                                           @Autowired(required = false) ObjectFactory<List<CustomCriteriaHandlerFactory>> handlerFactories) {
        super(repositoryInterface);
        this.entityMetadataFactory = entityMetadataFactory;
        this.handlerFactories = handlerFactories;
    }

    @Override
    @NonNull
    protected RepositoryFactorySupport getFactoryInstance(@NonNull MongoOperations operations) {
        return new ExtendedMongoRepositoryFactory(operations);
    }

    /**
     * Фабрика mongo репозиториев
     */
    private class ExtendedMongoRepositoryFactory extends MongoRepositoryFactory {

        /** Контракт на работу с mongo */
        private final MongoOperations mongo;

        /** Контекст */
        private final MappingContext<? extends MongoPersistentEntity<?>, MongoPersistentProperty> mappingContext;

        /**
         * Конструктор
         * @param mongoOperations контракт на работу с mongo
         */
        public ExtendedMongoRepositoryFactory(MongoOperations mongoOperations) {
            super(mongoOperations);
            this.mongo = mongoOperations;
            this.mappingContext = mongoOperations.getConverter().getMappingContext();
        }

        @NonNull
        @Override
        @SuppressWarnings("unchecked")
        protected Object getTargetRepository(RepositoryInformation information) {
            var entity = mappingContext.getRequiredPersistentEntity(information.getDomainType());
            var entityInformation = new MappingMongoEntityInformation<>(entity, (Class<Serializable>) information.getIdType());
            var metadata = entityMetadataFactory.getObject().get(entityInformation.getJavaType());
            var filterConverter = new MongoFilterConverter(getHandlers(metadata));
            return new ExtendedMongoRepositoryImpl<>(filterConverter, entityInformation, mongo);
        }

        @Override
        @NonNull
        protected Class<?> getRepositoryBaseClass(@NonNull RepositoryMetadata metadata) {
            return ExtendedMongoRepository.class;
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
    }
}