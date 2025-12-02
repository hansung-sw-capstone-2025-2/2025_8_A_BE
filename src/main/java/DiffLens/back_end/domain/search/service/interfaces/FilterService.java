package DiffLens.back_end.domain.search.service.interfaces;

import DiffLens.back_end.domain.search.entity.Filter;
import DiffLens.back_end.domain.search.entity.SearchFilter;
import DiffLens.back_end.domain.search.entity.SearchHistory;

import java.util.List;

public interface FilterService {
    SearchFilter makeFilter(List<Long> filterIdList, SearchHistory searchHistory);
    List<Filter> findFilters(List<Long> filterIdList);
}
