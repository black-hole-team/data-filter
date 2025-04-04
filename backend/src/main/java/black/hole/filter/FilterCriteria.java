package black.hole.filter;

/**
 * Интерфейс критерия фильтрации фильтра
 */
public sealed interface FilterCriteria permits FilterFieldCriteria, FilterGroupCriteria, FilterStaticCriteria {

}
