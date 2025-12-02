package DiffLens.back_end.domain.search.service.implement;

import DiffLens.back_end.domain.panel.repository.PanelRepository;
import DiffLens.back_end.domain.search.dto.SearchRequestDTO;
import DiffLens.back_end.domain.search.dto.SearchResponseDTO;
import DiffLens.back_end.domain.search.service.interfaces.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 재검색 Service --> SearchService 구현
 */
@Service("existingSearchService")
@RequiredArgsConstructor
public class ExistingSearchService implements SearchService<SearchRequestDTO.ExistingSearchResult> {

    private final PanelRepository panelRepository;

    @Override
    public SearchResponseDTO.SearchResult search(SearchRequestDTO.ExistingSearchResult request) {
        return null;
    }

}