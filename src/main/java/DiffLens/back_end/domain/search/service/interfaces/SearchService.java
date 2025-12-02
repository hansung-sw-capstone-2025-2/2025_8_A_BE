package DiffLens.back_end.domain.search.service.interfaces;

import DiffLens.back_end.domain.search.dto.SearchResponseDTO;

import java.util.List;

public interface SearchService<T> {
    SearchResponseDTO.SearchResult search(T request);
}