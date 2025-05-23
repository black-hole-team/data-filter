package team.blackhole.data.filter.jpa;

import team.blackhole.data.filter.Filter;
import team.blackhole.data.filter.support.Page;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Интерфейс расширенного jpa репозитория
 * @param <T> тип сущности
 * @param <I> тип идентификатора сущности
 */
public interface ExtendedJpaRepository<T, I> extends JpaRepository<T, I> {

    /**
     * Найти все сущности по фильтру
     * @param filter фильтр
     * @return найденная страница
     */
    Page<T> findAll(Filter filter);
}
