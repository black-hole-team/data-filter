package black.hole.filter.config;

import black.hole.filter.FilterFacade;
import black.hole.filter.FilterFacadeImpl;
import black.hole.filter.builder.FilterBuilderFactory;
import black.hole.filter.builder.FilterBuilderFactoryImpl;
import black.hole.filter.handler.entity.EntityFieldMetadataProvider;
import black.hole.filter.handler.entity.EntityFieldMetadataProviderImpl;
import black.hole.filter.handler.entity.EntityMetadataProvider;
import black.hole.filter.handler.entity.EntityMetadataProviderImpl;
import black.hole.filter.writer.FilterWriter;
import black.hole.filter.writer.FilterWriterImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Автоматическая конфигурация разрешителя аргументов фильтра в запросе
 */
@Configuration
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class FilterAutoConfiguration {

    /**
     * Возвращает фабрику построителей фильтра
     * @return фабрика построителей фильтра
     */
    @Bean
    public FilterBuilderFactory filterBuilderFactory() {
        return new FilterBuilderFactoryImpl();
    }

    /**
     * Возвращает фасад фильтрации
     * @param filterWriter         писатель фильтра
     * @param filterBuilderFactory фабрика построителей фильтра
     * @return фасад фильтрации
     */
    @Bean
    public FilterFacade filterFacade(FilterWriter filterWriter, FilterBuilderFactory filterBuilderFactory) {
        return new FilterFacadeImpl(filterWriter, filterBuilderFactory);
    }

    /**
     * Возвращает писатель фильтра
     * @return писатель фильтра
     */
    @Bean
    public FilterWriter filterWriter() {
        return new FilterWriterImpl();
    }

    /**
     * Возвращает поставщика информации по типам сущностей
     * @return поставщик информации по типам сущностей
     */
    @Bean
    public EntityMetadataProvider entityMetadataProvider() {
        return new EntityMetadataProviderImpl();
    }

    /**
     * Возвращает поставщика информации по полям сущностей
     * @param entityMetadataProvider поставщик информации по типам сущностей
     * @return поставщик информации по полям сущностей
     */
    @Bean
    public EntityFieldMetadataProvider entityFieldMetadataProvider(EntityMetadataProvider entityMetadataProvider) {
        return new EntityFieldMetadataProviderImpl(entityMetadataProvider);
    }
}
