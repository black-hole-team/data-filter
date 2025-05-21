package team.blackhole.data.filter;

import team.blackhole.data.filter.builder.FilterBuilderFactory;
import team.blackhole.data.filter.builder.FilterBuilderImpl;
import team.blackhole.data.filter.exception.ParserException;
import team.blackhole.data.filter.writer.FilterWriterImpl;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.StringReader;

/**
 * Тест сервиса фильтров
 */
@Log4j2
class FilterFacadeTest {

    /** Стандартная фабрика построителя фильтра */
    private static final FilterBuilderFactory FILTER_BUILDER_FACTORY = () -> {
        var builder = new FilterBuilderImpl();
        builder.setPage(0);
        builder.setPageSize(500);
        return builder;
    };

    /**
     * Выполнить проверку сервиса фильтрации дл разбора фильтра
     * @param filterSource   текстовый вид фильтра
     * @param expectedResult ожидаемый текстовый эталон фильтра
     */
    private static void test(String filterSource, String expectedResult) {
        var service = new FilterFacadeImpl(new FilterWriterImpl(), FILTER_BUILDER_FACTORY);
        var start = System.currentTimeMillis();
        Assertions.assertEquals(expectedResult, service.write(service.read(new StringReader(filterSource))));
        log.info("Выполнено {}: за {} мс", filterSource, System.currentTimeMillis() - start);
    }

    /**
     * Выполнить проверку сервиса фильтрации дл разбора фильтра на полученную ошибку
     * @param filterSource    текстовый вид фильтра
     * @param expectedMessage ожидаемое сообщение исключения
     */
    private static void testFailed(String filterSource, String expectedMessage) {
        var service = new FilterFacadeImpl(new FilterWriterImpl(), FILTER_BUILDER_FACTORY);
        var start = System.currentTimeMillis();
        Assertions.assertThrows((Class<? extends Exception>) ParserException.class, () -> service.read(new StringReader(filterSource)), expectedMessage);
        log.info("Выполнено {}: за {} мс. Исключение {} с текстом '{}' успешно получено", filterSource,
                System.currentTimeMillis() - start, ParserException.class.getSimpleName(), expectedMessage);
    }

    @Test
    void FilterServiceTestFailNotCompletedInputPageCountAndPageSize() {
        testFailed("[10, 500", "Неожиданный конец ввода, проверьте завершенность выражения");
    }

    @Test
    void FilterServiceTestFailNotMatchAnyBlockPattern() {
        testFailed("[10, 'some test']", "Неизвестный тип блока на позиции 6: 'some test'");
    }

    @Test
    void FilterServiceTestPageCountAndPageSize() {
        test("[10, 500]", "[10, 500]");
    }

    @Test
    void FilterServiceTestSimpleCriteria() {
        test("[field1 = 'value of field1' & field2 = 'value of field2']", "[0, 500][(field1 = \"value of field1\" & field2 = \"value of field2\")]");
    }

    @Test
    void FilterServiceTestSimpleCriteriaWithSubGroup() {
        test("[field1 < 5 | (field1 > 5 & field2 = 10)]", "[0, 500][field1 < 5 | (field1 > 5 & field2 = 10)]");
    }

    @Test
    void FilterServiceTestSimpleCriteriaWithListValueAndInOperator() {
        test("[field1 < 5 | field1 in {1, 2, 3, 4, 5, 'example value of field1'}]", "[0, 500][field1 < 5 | field1 in {1, 2, 3, 4, 5, \"example value of field1\"}]");
    }

    @Test
    void FilterServiceTestSimpleCriteriaWithListValueAndNotInOperator() {
        test("[field1 <= 5 | field1 not in {1, 2, 3, 4, 5, 'example value of field1'}]", "[0, 500][field1 <= 5 | field1 not in {1, 2, 3, 4, 5, \"example value of field1\"}]");
    }

    @Test
    void FilterServiceTestSimpleCriteriaWithNotEqualOperator() {
        test("[field1 != 5]", "[0, 500][field1 != 5]");
    }

    @Test
    void FilterServiceTestSimpleCriteriaWithListValueAndNotInOperatorWithNestedList() {
        test("[field1 > 10 & field2 not in {1, 2, 3, {1, 2, 3}}]", "[0, 500][(field1 > 10 & field2 not in {1, 2, 3, {1, 2, 3}})]");
    }

    @Test
    void FilterServiceTestComplexCriteria() {
        test("[field1 < 5 & (field2 >= 10 | field3 <= 20) & field4 not in {'a', 'b', 'c'}]", "[0, 500][(field1 < 5 & ((field2 >= 10 | field3 <= 20) & field4 not in {\"a\", \"b\", \"c\"}))]");
    }

    @Test
    void FilterServiceTestNestedCriteria() {
        test("[field1 = 'value1' & (field2 = 'value2' | field3 = 'value3' & (field4 = 'value4' | field5 = 'value5'))]", "[0, 500][(field1 = \"value1\" & (field2 = \"value2\" | (field3 = \"value3\" & (field4 = \"value4\" | field5 = \"value5\"))))]");
    }

    @Test
    void FilterServiceTestComplexNestedCriteria() {
        test("[field1 = 'value1' & ((field2 = 'value2' | field3 = 'value3') & (field4 = 'value4' | field5 = 'value5'))]", "[0, 500][(field1 = \"value1\" & ((field2 = \"value2\" | field3 = \"value3\") & (field4 = \"value4\" | field5 = \"value5\")))]");
    }

    @Test
    void FilterServiceTestComplexNestedCriteriaWithInOperator() {
        test("[field1 in {1, 2, 3} & (field2 in {4, 5} | field3 not in {'a', 'b'})]", "[0, 500][(field1 in {1, 2, 3} & (field2 in {4, 5} | field3 not in {\"a\", \"b\"}))]");
    }
}