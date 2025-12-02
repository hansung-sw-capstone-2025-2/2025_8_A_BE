package DiffLens.back_end.domain.search.service.implement;

import DiffLens.back_end.domain.members.entity.Member;
import DiffLens.back_end.domain.members.service.auth.CurrentUserService;
import DiffLens.back_end.domain.panel.repository.PanelRepository;
import DiffLens.back_end.domain.panel.repository.projection.PanelWithRawDataDTO;
import DiffLens.back_end.domain.search.entity.Filter;
import DiffLens.back_end.domain.search.repository.FilterRepository;
import DiffLens.back_end.global.fastapi.FastApiService;
import DiffLens.back_end.global.fastapi.dto.request.MainSearchRequest;
import DiffLens.back_end.global.fastapi.dto.response.FastChartResponseDTO;
import DiffLens.back_end.domain.search.converter.SearchDtoConverter;
import DiffLens.back_end.domain.search.dto.SearchRequestDTO;
import DiffLens.back_end.domain.search.dto.SearchResponseDTO;
import DiffLens.back_end.domain.search.entity.SearchHistory;
import DiffLens.back_end.domain.search.service.interfaces.SearchHistoryService;
import DiffLens.back_end.domain.search.service.interfaces.SearchService;
import DiffLens.back_end.global.fastapi.dto.response.MainSearchResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 자연어 Service --> SearchService 구현
 */
@Slf4j
@Service("naturalSearchService")
@RequiredArgsConstructor
public class NaturalSearchService implements SearchService<SearchRequestDTO.NaturalLanguage> {

    // service & repository
    private final CurrentUserService currentUserService;
    private final PanelRepository panelRepository;
    private final FilterRepository filterRepository;
    private final SearchHistoryService searchHistoryService;
    private final FastApiService fastApiService;

    // converter들
    private final SearchDtoConverter<MainSearchResponse, SearchResponseDTO.SearchResult.Summary, List<PanelWithRawDataDTO>> summaryConverter;
    private final SearchDtoConverter<Void, List<SearchResponseDTO.SearchResult.AppliedFilter>, SearchHistory> filterConverter;


    @Override
    @Transactional(readOnly = false)
    public SearchResponseDTO.SearchResult search(SearchRequestDTO.NaturalLanguage request) {

        // 자연어 쿼리 로그 출력
        log.info("[자연어 검색 호출 중] 쿼리 : {}", request.getQuestion());

        // 유저 추출
        Member currentUser = currentUserService.getCurrentUser();

        // fast api에 요청보낼 requestDTO 생성
        MainSearchResponse response = fastApiService.getMainSearch(makeRequestDTO(request));

        // panelId List 생성
        List<String> panelIdList = response.getPanels().stream()
                .map(MainSearchResponse.PanelInfo::getPanelId)
                .toList();

        // DB 에서 Panel 목록 가져옴
        List<PanelWithRawDataDTO> foundPanels = panelRepository.findPanelsWithRawDataByIds(panelIdList);

        // SearchResult.Summary 생성
        SearchResponseDTO.SearchResult.Summary summary = summaryConverter.requestToDto(response, foundPanels);

        // SearchHistory 데이터 생성 및 저장
        SearchHistory searchHistory = searchHistoryService.makeSearchHistory(request, response);
        searchHistory.setMember(currentUser);

        // SearchResult.AppliedFilter 생성
        List<SearchResponseDTO.SearchResult.AppliedFilter> appliedFiltersSummary = filterConverter.requestToDto(null, searchHistory);

        // Chart 생성
        // 차트 추천 서브서버 API 호출
        FastChartResponseDTO.ChartRecommendationsResponse chartResponse = fastApiService.getChartRecommendations(searchHistory.getId());

        // ChartData 변환
        SearchResponseDTO.SearchResult.ChartData mainChart = convertToChartData(chartResponse.getMainChart());
        List<SearchResponseDTO.SearchResult.ChartData> subCharts = chartResponse.getSubCharts().stream()
                .map(this::convertToChartData)
                .toList();

        return SearchResponseDTO.SearchResult.builder()
                .searchId(searchHistory.getId())
                .summary(summary)
                .appliedFiltersSummary(appliedFiltersSummary)
                .mainChart(mainChart)
                .subCharts(subCharts)
                .build();
    }

    /**
     * FastAPI ChartData를 SearchResponseDTO ChartData로 변환
     */
    private SearchResponseDTO.SearchResult.ChartData convertToChartData(
            FastChartResponseDTO.ChartData fastChartData) {
        if (fastChartData == null) {
            return null;
        }

        List<SearchResponseDTO.SearchResult.ChartDataPoint> dataPoints = fastChartData.getData().stream()
                .map(this::convertToChartDataPoint)
                .toList();

        return SearchResponseDTO.SearchResult.ChartData.builder()
                .chartType(fastChartData.getChartType())
                .metric(fastChartData.getMetric())
                .title(fastChartData.getTitle())
                .reasoning(fastChartData.getReasoning())
                .data(dataPoints)
                .build();
    }

    /**
     * FastAPI ChartDataPoint를 SearchResponseDTO ChartDataPoint로 변환
     */
    private SearchResponseDTO.SearchResult.ChartDataPoint convertToChartDataPoint(
            FastChartResponseDTO.ChartDataPoint fastDataPoint) {
        if (fastDataPoint == null) {
            return null;
        }

        return SearchResponseDTO.SearchResult.ChartDataPoint.builder()
                .category(fastDataPoint.getCategory())
                .value(fastDataPoint.getValue())
                .male(fastDataPoint.getMale())
                .maleMax(fastDataPoint.getMaleMax())
                .female(fastDataPoint.getFemale())
                .femaleMax(fastDataPoint.getFemaleMax())
                .id(fastDataPoint.getId())
                .name(fastDataPoint.getName())
                .build();
    }

    // fast api에 보낼 요청 dto 생성
    private MainSearchRequest makeRequestDTO(SearchRequestDTO.NaturalLanguage request){

        // 추천 쿼리 선택 시 제공되는 구조화된 검색 파라미터
        Map<String, Object> recommendedFilters = new HashMap<>();

        // ui에서 필터를 직접 선택한 경우
        Map<String, Object> structuredFilters = new HashMap<>();
        List<Filter> filters = filterRepository.findByIds(request.getFilters());
        for (Filter filter : filters) {
            String key = filter.getType(); // type을 키로 설정
            String value = filter.getRawDataValue(); // rawDataValue를 value로 설정

            structuredFilters.merge(key, value, (oldVal, newVal) -> oldVal + "," + newVal);
        }

        return MainSearchRequest.builder()
                .query(request.getQuestion())
                .searchParams(recommendedFilters)
                .structuredFilters(structuredFilters)
                .searchMode(request.getMode().name().toLowerCase())
                .limit(100)
                .build();

    }

}
