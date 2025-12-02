package DiffLens.back_end.domain.search.converter;

import DiffLens.back_end.domain.search.dto.SearchResponseDTO.SearchResult.AppliedFilter;
import DiffLens.back_end.domain.search.entity.Filter;
import DiffLens.back_end.domain.search.entity.SearchHistory;
import DiffLens.back_end.domain.search.service.interfaces.FilterService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class FilterDtoConverter implements SearchDtoConverter<Void, List<AppliedFilter>, SearchHistory>{

    private final FilterService filterService;

    @Override
    public List<AppliedFilter> requestToDto(Void v, SearchHistory searchHistory) {

        List<Long> filterIdList = searchHistory.getSearchFilter().getFilters();
        List<Filter> filters = filterService.findFilters(filterIdList);
        return filters.stream()
                .map(this::filterToDto)
                .toList();

    }

    private AppliedFilter filterToDto(Filter filter) {
        return AppliedFilter.builder()
                .key(filter.getType())
                .displayValue(filter.getDisplayValue())
                .build();
    }

}
