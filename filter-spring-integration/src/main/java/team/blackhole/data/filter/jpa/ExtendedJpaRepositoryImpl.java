package team.blackhole.data.filter.jpa;

import team.blackhole.data.filter.Filter;
import team.blackhole.data.filter.support.Page;
import team.blackhole.data.filter.support.PageImpl;
import team.blackhole.data.filter.jpa.converter.JpaFilterConverter;
import jakarta.persistence.EntityManager;
import org.hibernate.query.sqm.tree.select.SqmSelectStatement;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

import java.io.Serializable;
import java.util.Collections;

/**
 * Реализация интерфейса {@link ExtendedJpaRepository}
 * @param <T> тип сущности оперируемой репозиторием
 * @param <I> тип идентификатора сущности оперируемой сущности
 */
public class ExtendedJpaRepositoryImpl<T, I extends Serializable> extends SimpleJpaRepository<T, I>
        implements ExtendedJpaRepository<T, I> {

    /** Заполнитель фильтра */
    private final JpaFilterConverter<T> converter;

    /** Контракт для вызова jpa */
    private final EntityManager entityManager;

    /**
     * Конструктор репозитория
     * @param converter         конвертер фильтра
     * @param entityInformation информация о сущности
     * @param entityManager     менеджер сущностей
     */
    public ExtendedJpaRepositoryImpl(final JpaFilterConverter<T> converter,
                                     final JpaEntityInformation<T, I> entityInformation, final EntityManager entityManager) {
        super(entityInformation, entityManager);
        this.converter = converter;
        this.entityManager = entityManager;
    }

    @Override
    public Page<T> findAll(Filter filter) {
        var query = (SqmSelectStatement<T>) converter.convert(filter);
        var count = entityManager.createQuery(query.createCountQuery()).getSingleResult();
        if (count == 0) {
            return new PageImpl<>(Collections.emptyList(), filter.getPage(), 0, filter);
        }
        return new PageImpl<>(entityManager.createQuery(query)
                .setMaxResults(filter.getPageSize())
                .setFirstResult(filter.getPage() * filter.getPageSize())
                .getResultList(), filter.getPage(), count, filter);
    }
}