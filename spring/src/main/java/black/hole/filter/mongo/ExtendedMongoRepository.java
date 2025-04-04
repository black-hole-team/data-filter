package black.hole.filter.mongo;

import black.hole.filter.Filter;
import black.hole.filter.support.Page;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.function.Consumer;

/**
 * Интерфейс расширенного mongodb репозитория
 * @param <T> тип сущности
 * @param <I> тип идентификатора сущности
 */
public interface ExtendedMongoRepository<T, I> extends MongoRepository<T, I> {

    /**
     * Найти все сущности по фильтру
     * @param filter фильтр
     * @return найденная страница
     */
    Page<T> findAll(Filter filter);

    /**
     * Найти все сущности по фильтру
     * @param aggregation выражение агрегации
     * @param filter      фильтр
     * @return найденная страница
     */
    Page<T> findAll(Aggregation aggregation, Filter filter);

    /**
     * Выполняет кастомизированное действие над данными используя шаблон взаимодействия с mongodb
     * @param mongoOperationsConsumer получатель шаблона взаимодействия с mongodb
     */
    void custom(Consumer<MongoOperations> mongoOperationsConsumer);
}
