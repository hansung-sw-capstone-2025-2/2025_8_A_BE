package DiffLens.back_end.domain.search.service.interfaces;

import DiffLens.back_end.global.fastapi.dto.response.FastNaturalLanguageResponseDTO;
import DiffLens.back_end.domain.search.dto.SearchRequestDTO;
import DiffLens.back_end.domain.search.dto.SearchResponseDTO;
import DiffLens.back_end.domain.search.entity.SearchHistory;
import DiffLens.back_end.global.fastapi.dto.response.MainSearchResponse;

public interface SearchHistoryService {
    SearchHistory makeSearchHistory(SearchRequestDTO.NaturalLanguage request, MainSearchResponse fastApiResponse);
    SearchResponseDTO.EachResponses getEachResponses(Long searchHistoryId, Integer pageNum, Integer size);
}
