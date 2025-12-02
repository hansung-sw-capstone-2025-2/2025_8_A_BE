package DiffLens.back_end.domain.search.service.interfaces;

import DiffLens.back_end.domain.search.dto.SearchResponseDTO;

public interface SearchRecommendService {
    SearchResponseDTO.Recommends getRecommendations();
}
