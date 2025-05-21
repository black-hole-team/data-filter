package team.blackhole.data.filter;

/**
 * Интерфейс критерия фильтрации фильтра
 */
public sealed interface FilterCriteria permits FilterFieldCriteria, FilterGroupCriteria, FilterStaticCriteria {

}
