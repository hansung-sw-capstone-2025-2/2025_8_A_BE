package DiffLens.back_end.domain.search.service.implement;

import DiffLens.back_end.domain.members.entity.Member;
import DiffLens.back_end.domain.members.service.auth.CurrentUserService;
import DiffLens.back_end.domain.search.dto.SearchRequestDTO;
import DiffLens.back_end.domain.search.dto.SearchResponseDTO;
import DiffLens.back_end.domain.search.enums.mode.QuestionMode;
import DiffLens.back_end.domain.search.service.interfaces.RecommendSearchCacheService;
import DiffLens.back_end.domain.search.service.interfaces.SearchService;
import DiffLens.back_end.global.fastapi.dto.response.FastHomeResponseDTO;
import DiffLens.back_end.global.responses.code.status.error.SearchStatus;
import DiffLens.back_end.global.responses.exception.handler.ErrorHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

/**
 * ai가 추천해준 검색어로 검색합니다.
 */
@Service
@RequiredArgsConstructor
public class RecommendedSearchService implements SearchService<Long> {

    private final CurrentUserService currentUserService;
    private final SearchService<SearchRequestDTO.NaturalLanguage> naturalSearchService;
    private final RecommendSearchCacheService<FastHomeResponseDTO.HomeRecommend> recommendCacheService;

    @Override
    public SearchResponseDTO.SearchResult search(Long recommendedId) {

        // 0. 현재 유저 조회
        Member member = currentUserService.getCurrentUser();

        // 1. 캐시에 있으면 그거로 SearchRequestDTO.NaturalLanguage 객체 만듦
        FastHomeResponseDTO.HomeRecommend cacheInfo = recommendCacheService.getCacheInfo(member);
        if (cacheInfo == null) { //
            throw new ErrorHandler(SearchStatus.RECOMMENDED_EXPIRED); // 멤버에 해당하는 추천 검색어 캐시가 만료된 경우
        }

        // 1-1. 캐시에서 조회한 데이터 중 recommendedId에 해당하는 Recommendation
        FastHomeResponseDTO.Recommendation recommendation = cacheInfo.getRecommendations().stream()
                .filter(r -> r.getId().equals(recommendedId))
                .findFirst()
                .orElseThrow(() -> new ErrorHandler(SearchStatus.RECOMMENDED_SEARCH_NOT_FOUND)); // 멤버에 해당하는 캐시 정보는 있지만 recommendedId가 잘못된 경우

        // 2-1. Recommendation -> SearchRequestDTO.NaturalLanguage 변환
        SearchRequestDTO.NaturalLanguage naturalRequestDTO = SearchRequestDTO.NaturalLanguage.builder()
                .question(recommendation.getQuery())
                .mode(QuestionMode.STRICT)
                .filters(new ArrayList<Long>()) // 일단 필터 없이...
                .build();

        // . SearchRequestDTO.NaturalLanguage로 기존 자연어 검색 서비스 메서드 호출 -> 반환
        return naturalSearchService.search(naturalRequestDTO);
    }

}
