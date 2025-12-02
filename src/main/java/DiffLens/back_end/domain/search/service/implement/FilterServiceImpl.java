package DiffLens.back_end.domain.search.service.implement;

import DiffLens.back_end.domain.search.entity.Filter;
import DiffLens.back_end.domain.search.entity.SearchFilter;
import DiffLens.back_end.domain.search.entity.SearchHistory;
import DiffLens.back_end.domain.search.repository.FilterRepository;
import DiffLens.back_end.domain.search.repository.SearchFilterRepository;
import DiffLens.back_end.domain.search.repository.SearchHistoryRepository;
import DiffLens.back_end.domain.search.service.interfaces.FilterService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FilterServiceImpl implements FilterService {

    private final SearchFilterRepository searchFilterRepository;
    private final FilterRepository filterRepository;
    private final SearchHistoryRepository searchHistoryRepository;

    @Transactional
    public SearchFilter makeFilter(List<Long> filters, SearchHistory searchHistory) {
        if (searchHistory.getId() == null) {
            searchHistoryRepository.save(searchHistory); // 반드시 먼저 저장
        }

        SearchFilter searchFilter = SearchFilter.builder()
                .searchHistory(searchHistory)
                .filters(filters)
                .build();

        return searchFilterRepository.save(searchFilter);
    }

    @Override
    public List<Filter> findFilters(List<Long> filterIdList) {
        return filterRepository.findAllById(filterIdList);
    }
}
