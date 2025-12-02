package DiffLens.back_end.global.fastapi;

import DiffLens.back_end.global.fastapi.dto.request.FastLibraryChartRequestDTO;
import DiffLens.back_end.global.fastapi.dto.request.*;
import DiffLens.back_end.global.fastapi.dto.response.FastLibraryChartResponseDTO;
import DiffLens.back_end.global.fastapi.dto.response.*;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum FastApiRequestType {

        // 검색
        NATURAL_SEARCH("/api/search/", MainSearchRequest.class,
                        MainSearchResponse.class, "자연어 검색"),
        RE_SEARCH("/api/re-search", FastReSearchRequestDTO.ReSearch.class, FastReSearchResponseDTO.ReSearch.class, "재검색"),

        // 추천
        RECOMMENDATIONS("/api/quick-search/recommendations", FastHomeRequestDTO.HomeRecommendRequest.class,
                        FastHomeResponseDTO.HomeRecommend.class, "검색어 일반 추천"), // 일반 추천
        RECOMMENDATIONS_BY_MEMBER("/api/quick-search/recommendations/by-member",
                        FastHomeRequestDTO.HomeRecommendByMemberRequest.class,
                        FastHomeResponseDTO.HomeRecommend.class, "검색어 유저기반 추천"),

        // 라이브러리 비교
        COMPARE("/api/cohort-comparison/compare", Void.class,
                        FastLibraryCompareResponseDTO.CompareResult.class, "라이브러리 비교"),

        // 차트
        CHART_RECOMMENDATIONS("/api/chart/search-result/{searchId}/recommendations", Void.class,
                        FastChartResponseDTO.ChartRecommendationsResponse.class, "차트 추천"),
        CHART_FROM_LIBRARY("/api/chart/from-library", FastLibraryChartRequestDTO.class,
                        FastLibraryChartResponseDTO.LibraryChartResponse.class, "라이브러리 차트 추천"),
        // REFINE_SEARCH("/search/refine",
        // FastNaturalSearchResponseDTO.SearchResult.class),
        ;

        private final String uri;
        private final Class<?> requestBody; // request body
        private final Class<?> responseType; // response body
        private final String name;

}
