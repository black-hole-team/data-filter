package black.hole.filter.mongo;

import black.hole.filter.Filter;
import black.hole.filter.FilterConverter;
import black.hole.filter.support.Page;
import black.hole.filter.support.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.repository.query.MongoEntityInformation;
import org.springframework.data.mongodb.repository.support.SimpleMongoRepository;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.function.Consumer;

/**
 * Реализация интерфейса {@link ExtendedMongoRepository}
 * @param <T> тип сущности оперируемой репозиторием
 * @param <I> тип идентификатора сущности оперируемой сущности
 */
public class ExtendedMongoRepositoryImpl<T, I extends Serializable> extends SimpleMongoRepository<T, I>
        implements ExtendedMongoRepository<T, I> {

    /** Заполнитель фильтра */
    private final FilterConverter<Query> converter;

    /** Контракт для вызова mongodb */
    private final MongoOperations mongoOperations;

    /** Информация о сущности */
    private final MongoEntityInformation<T, I> entityInformation;

    /**
     * Конструктор репозитория
     * @param converter         конвертер фильтра
     * @param entityInformation информация о сущности
     * @param mongoOperations   контракт для вызова mongodb
     */
    public ExtendedMongoRepositoryImpl(FilterConverter<Query> converter, final MongoEntityInformation<T, I> entityInformation,
                                       final MongoOperations mongoOperations) {
        super(entityInformation, mongoOperations);
        this.converter = converter;
        this.entityInformation = entityInformation;
        this.mongoOperations = mongoOperations;
    }

    @Override
    public Page<T> findAll(Filter filter) {
        var query = converter.convert(filter);
        var count = mongoOperations.count(query, entityInformation.getJavaType(), entityInformation.getCollectionName());
        var content = Collections.<T>emptyList();
        if (count != 0) {
            content = mongoOperations.find(query.with(PageRequest.of(filter.getPage(), filter.getPageSize())),
                    entityInformation.getJavaType(), entityInformation.getCollectionName());
        }
        return new PageImpl<>(content, filter.getPage(), count, filter);
    }

    @Override
    public Page<T> findAll(Aggregation aggregation, Filter filter) {
        var pipeline = aggregation.getPipeline();
        var query = converter.convert(filter);
        // Добавляем в конец условие
        pipeline.add(Aggregation.match(context -> query.getQueryObject()));
        var countPipeline = new ArrayList<>(pipeline.getOperations());
        countPipeline.add(Aggregation.count().as("total"));
        var count = mongoOperations.aggregate(Aggregation.newAggregation(countPipeline), entityInformation.getCollectionName(), Object.class)
                .getRawResults().getInteger("total");
        var content = Collections.<T>emptyList();
        if (count != 0) {
            // Добавляем в конец условие
            pipeline
                    .add(Aggregation.skip(query.getSkip()))
                    .add(Aggregation.limit(query.getLimit()));
            // Получаем результат
            content = mongoOperations.aggregate(aggregation, entityInformation.getCollectionName(), entityInformation.getJavaType())
                    .getMappedResults();
        }
        return new PageImpl<>(content, filter.getPage(), count, filter);
    }

    @Override
    public void custom(Consumer<MongoOperations> mongoOperationsConsumer) {
        mongoOperationsConsumer.accept(mongoOperations);
    }
}