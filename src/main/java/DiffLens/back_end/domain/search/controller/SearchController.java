package DiffLens.back_end.domain.search.controller;

import DiffLens.back_end.domain.search.dto.SearchRequestDTO;
import DiffLens.back_end.domain.search.dto.SearchResponseDTO;
import DiffLens.back_end.domain.search.service.interfaces.SearchHistoryService;
import DiffLens.back_end.domain.search.service.interfaces.SearchRecommendService;
import DiffLens.back_end.domain.search.service.interfaces.SearchService;
import DiffLens.back_end.global.responses.exception.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "검색 API")
@RestController
@RequestMapping("/search")
@RequiredArgsConstructor
public class SearchController {

        private final SearchService<SearchRequestDTO.NaturalLanguage> naturalSearchService;
        private final SearchService<Long> recommendationSearchService;
        private final SearchService<SearchRequestDTO.ExistingSearchResult> existingSearchService;
        private final SearchHistoryService searchHistoryService;
        private final SearchRecommendService searchRecommendService;

        @PostMapping
        @Operation(summary = "자연어 검색 ( 자연어 쿼리 직접 입력 ) ( ai 연동 완료, 차트 포함 )", description = """
                        ## 개요
                        자연어 검색 API 입니다. AI 서버를 통해 검색을 수행하고, 검색 결과에 대한 차트 추천을 받아 반환합니다.

                        ## request body
                        - 검색 모드와 필터 항목들은 노션에 정리하여 올리겠습니다.
                        - 필터에는 Filter Code 를 넣어주세요. ex) 101, 203, 305 ...

                        ## 응답 구조
                        - **summary**: 검색 결과 요약 정보 (총 응답자 수, 평균 연령, 신뢰도 등)
                        - **applied_filters_summary**: 적용된 필터 목록
                        - **main_chart**: 메인 차트 데이터 (amCharts 형식)
                          - `chart_type`: 차트 타입 (pie, donut, column, bar, map, stacked-bar, infographic 등)
                          - `metric`: 차트를 생성한 메트릭 (age_group, gender, residence 등)
                          - `title`: 차트 제목
                          - `reasoning`: 차트 선택 이유 (메인 차트에만 제공)
                          - `data`: 차트 데이터 포인트 배열
                        - **sub_charts**: 서브 차트 데이터 배열 (최대 2개, amCharts 형식)
                          - 메인 차트와 동일한 구조이지만 `reasoning`은 null

                        ## 차트 타입
                        - **pie**: 원형 차트 (2-5개 카테고리)
                        - **donut**: 도넛 차트 (4-8개 카테고리)
                        - **column**: 세로 막대 차트 (8개 이상 카테고리)
                        - **bar**: 가로 막대 차트 (레이블이 긴 경우)
                        - **map**: 지도 차트 (지역 데이터)
                        - **stacked-bar**: 누적 가로 막대 차트 (연령대별 성별 분포 등)
                        - **infographic**: 인포그래픽 차트 (직업별 성별 비율 등)

                        ## 참고사항
                        - 검색 결과에 개별응답은 포함하지 않았습니다. 개별 응답 데이터 API를 조회해야 합니다.
                        - 차트는 AI 서버에서 자동으로 추천되며, 검색 결과의 특성에 따라 최적의 차트 타입이 선택됩니다.
                        """)
        public ApiResponse<SearchResponseDTO.SearchResult> naturalLanguage(
                        @RequestBody @Valid SearchRequestDTO.NaturalLanguage request) {
                SearchResponseDTO.SearchResult result = naturalSearchService.search(request);
                return ApiResponse.onSuccess(result);
        }

        @PostMapping("/recommended/{recommendedId}")
        @Operation(summary = "추천 검색어로 검색 ( ai가 추천해준 검색 정보로 검색 ) ( ai 연동 완료, 차트 포함 )", description = """
                        ## 개요
                        AI가 추천해준 검색 정보로 검색합니다. 자연어 검색 API와 동일한 응답 구조를 반환하며, 차트도 포함됩니다.

                        ## 요청
                        - 맞춤 검색 추천 api 호출로 얻은 결과 중 recommendations에 포함된 검색 정보의 id를 recommendedId에 넣어 요청하면 됩니다.
                        - 검색 정보는 DB가 아닌 캐시에 저장되어 일정 시간이 지나면 올바른 recommendedId로 요청해도 오류가 발생합니다.
                        - 만료되었다는 응답이 발생하면 '맞춤 검색 추천' api를 다시 호출하거나,
                          추천 검색어 api에서 응답받은 title 혹은 query를 이용해서 자연어 검색 api를 호출하여 검색해주세요.

                        ## 응답
                        자연어 검색과 동일한 형태의 응답을 보냅니다.
                        - **main_chart**: 메인 차트 데이터 (amCharts 형식)
                        - **sub_charts**: 서브 차트 데이터 배열 (최대 2개, amCharts 형식)
                        - 자세한 차트 구조는 자연어 검색 API 설명을 참고하세요.
                        """)
        public ApiResponse<SearchResponseDTO.SearchResult> recommendedSearch(
                        @PathVariable("recommendedId") Long recommendedId) {
                SearchResponseDTO.SearchResult result = recommendationSearchService.search(recommendedId);
                return ApiResponse.onSuccess(result);
        }

        @GetMapping("/{searchId}/each-responses")
        @Operation(summary = "개별 응답 데이터 ( 완료 )", description = """

                        ## 개요
                        개별 응답 데이터 조회 API 입니다.
                        페이징 문제로 인해 검색 API와 분리하였습니다.

                        ## 요청값
                        - searchId : 검색결과 ID. 검색 API 에서 받은 식별자 값(searchId)를 넣으면 됩니다.
                        - page : 페이지 번호입니다. 1부터 시작입니다.
                        - size : 한 페이지 크기입니다.

                        ## 응답
                        현재 피그마에 나와있는대로 구현했습니다.

                        """)
        public ApiResponse<SearchResponseDTO.EachResponses> eachResponses(@PathVariable("searchId") Long searchId,
                        @RequestParam("page") Integer page, @RequestParam("size") Integer size) {
                SearchResponseDTO.EachResponses result = searchHistoryService.getEachResponses(searchId, page, size);
                return ApiResponse.onSuccess(result);
        }

        @GetMapping("/recommended")
        @Operation(summary = "맞춤 검색 추천 ( ai 연동 완료 )", description = "유저 온보딩 정보, 검색기록을 토대로 검색어를 추천합니다.")
        public ApiResponse<SearchResponseDTO.Recommends> recommendation() {
                SearchResponseDTO.Recommends recommendations = searchRecommendService.getRecommendations();
                return ApiResponse.onSuccess(recommendations);
        }

        /** ------ 👇 아래는 미제공 API 👇 ------ **/

        @PostMapping("/refine")
        @Operation(summary = "기존 검색 결과 기반 재검색 ( 미구현 )", description = "아직 구현 전이지만 아마 자연어 검색과 같은 형태로 반환될 듯 싶습니다.", hidden = true)
        public ApiResponse<SearchResponseDTO.SearchResult> refine(
                        @RequestBody @Valid SearchRequestDTO.ExistingSearchResult request) {
                SearchResponseDTO.SearchResult result = new SearchResponseDTO.SearchResult(); // 임시 result
                return ApiResponse.onSuccess(result);
        }

}
