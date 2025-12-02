package DiffLens.back_end.global.fastapi;

import DiffLens.back_end.global.fastapi.dto.request.FastHomeRequestDTO;
import DiffLens.back_end.global.fastapi.dto.request.FastLibraryChartRequestDTO;
import DiffLens.back_end.global.fastapi.dto.request.MainSearchRequest;
import DiffLens.back_end.global.fastapi.dto.response.FastHomeResponseDTO;
import DiffLens.back_end.global.fastapi.dto.response.FastLibraryChartResponseDTO;
import DiffLens.back_end.global.fastapi.dto.response.FastLibraryCompareResponseDTO;
import DiffLens.back_end.global.fastapi.dto.response.MainSearchResponse;
import DiffLens.back_end.global.fastapi.dto.response.FastChartResponseDTO;
import DiffLens.back_end.global.fastapi.fastApiClients.PathVariableFastApiClient;
import DiffLens.back_end.global.fastapi.fastApiClients.PostFastApiClient;
import DiffLens.back_end.global.fastapi.fastApiClients.QueryParamFastApiClient;
import DiffLens.back_end.global.responses.code.status.error.SearchStatus;
import DiffLens.back_end.global.responses.exception.handler.ErrorHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Fast API 호출 관리
 */
@Service
@RequiredArgsConstructor
public class FastApiService {

    private final PostFastApiClient postClient;
    private final PathVariableFastApiClient pathClient;
    private final QueryParamFastApiClient queryClient;

    // 자연어 검색
    public MainSearchResponse getMainSearch(MainSearchRequest request) {
        return postClient.sendRequest(FastApiRequestType.NATURAL_SEARCH, request);
    }

    // 라이브러리 비교
    public FastLibraryCompareResponseDTO.CompareResult compareLibraries(Long cohort1Id, Long cohort2Id) {
        java.util.Map<String, Object> queryParams = new java.util.HashMap<>();
        queryParams.put("cohort_1_id", cohort1Id);
        queryParams.put("cohort_2_id", cohort2Id);
        return queryClient.sendRequest(FastApiRequestType.COMPARE, null, queryParams);
    }

    // 추천검색
    public FastHomeResponseDTO.HomeRecommend recommend(FastHomeRequestDTO.HomeRecommendRequest request) {
        return postClient.sendRequest(FastApiRequestType.RECOMMENDATIONS, request);
    }

    // 차트 추천
    public FastChartResponseDTO.ChartRecommendationsResponse getChartRecommendations(Long searchId) {

        FastChartResponseDTO.ChartRecommendationsResponse response = pathClient.sendRequest(
                FastApiRequestType.CHART_RECOMMENDATIONS,
                null,
                searchId);

        // 호출 예외 발생하면 null을 반환함 -> null일 경우 NO_RESULT 반환
        if(response == null)
            throw new ErrorHandler(SearchStatus.NO_RESULT);

        return response;
    }

    // 라이브러리로부터 차트 생성
    public FastLibraryChartResponseDTO.LibraryChartResponse getChartsFromLibrary(
            FastLibraryChartRequestDTO request) {
        return postClient.sendRequest(FastApiRequestType.CHART_FROM_LIBRARY, request);
    }

}
