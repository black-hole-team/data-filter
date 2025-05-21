package team.blackhole.data.filter.config;

import team.blackhole.data.filter.FilterFacade;
import team.blackhole.data.filter.FilterFacadeImpl;
import team.blackhole.data.filter.builder.FilterBuilderFactory;
import team.blackhole.data.filter.builder.FilterBuilderFactoryImpl;
import team.blackhole.data.filter.handler.entity.EntityFieldMetadataProvider;
import team.blackhole.data.filter.handler.entity.EntityFieldMetadataProviderImpl;
import team.blackhole.data.filter.handler.entity.EntityMetadataProvider;
import team.blackhole.data.filter.handler.entity.EntityMetadataProviderImpl;
import team.blackhole.data.filter.writer.FilterWriter;
import team.blackhole.data.filter.writer.FilterWriterImpl;
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
