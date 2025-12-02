package DiffLens.back_end.domain.search.service.implement;

import DiffLens.back_end.domain.members.entity.Member;
import DiffLens.back_end.domain.members.service.auth.CurrentUserService;
import DiffLens.back_end.domain.panel.repository.PanelRepository;
import DiffLens.back_end.domain.panel.repository.projection.PanelWithRawDataDTO;
import DiffLens.back_end.domain.search.converter.SearchDtoConverter;
import DiffLens.back_end.domain.search.dto.SearchRequestDTO;
import DiffLens.back_end.domain.search.dto.SearchResponseDTO;
import DiffLens.back_end.domain.search.entity.Filter;
import DiffLens.back_end.domain.search.entity.SearchHistory;
import DiffLens.back_end.domain.search.enums.mode.QuestionMode;
import DiffLens.back_end.domain.search.repository.FilterRepository;
import DiffLens.back_end.domain.search.service.interfaces.SearchHistoryService;
import DiffLens.back_end.global.fastapi.FastApiService;
import DiffLens.back_end.global.fastapi.dto.request.MainSearchRequest;
import DiffLens.back_end.global.fastapi.dto.response.FastChartResponseDTO;
import DiffLens.back_end.global.fastapi.dto.response.MainSearchResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.lenient;

/**
 * Unit Test for NaturalSearchService
 *
 * This test verifies that the natural language search functionality
 * returns correct search results, summary, applied filters, and chart information.
 *
 * Dependencies (repositories, services, converters, APIs) are mocked to isolate
 * the service logic from database and external API calls.
 */
@ExtendWith(MockitoExtension.class)
class NaturalSearchServiceTest {

    private NaturalSearchService naturalSearchService;

    @Mock
    private CurrentUserService currentUserService;

    @Mock
    private PanelRepository panelRepository;

    @Mock
    private FilterRepository filterRepository;

    @Mock
    private SearchHistoryService searchHistoryService;

    @Mock
    private FastApiService fastApiService;

    @Mock
    private SearchDtoConverter<MainSearchResponse, SearchResponseDTO.SearchResult.Summary, List<PanelWithRawDataDTO>> summaryConverter;

    @Mock
    private SearchDtoConverter<Void, List<SearchResponseDTO.SearchResult.AppliedFilter>, SearchHistory> filterConverter;

    /**
     * Test case: Successful natural language search
     *
     * Steps:
     * 1. Prepare a request DTO with question, mode, and filter IDs.
     * 2. Prepare mock objects for member, panel, search history, summary, applied filters, and chart data.
     * 3. Define mock behavior for service dependencies.
     * 4. Call the naturalSearchService.search() method.
     * 5. Verify the response content and dependency interactions.
     */
    @Test
    @DisplayName("자연어 검색 성공 테스트")
    void search_success() {
        // Initialize service with mocked dependencies
        naturalSearchService = new NaturalSearchService(
                currentUserService,
                panelRepository,
                filterRepository,
                searchHistoryService,
                fastApiService,
                summaryConverter,
                filterConverter
        );

        // --- 1. Request DTO 준비 ---
        SearchRequestDTO.NaturalLanguage request = SearchRequestDTO.NaturalLanguage.builder()
                .question("테스트 질문")
                .mode(QuestionMode.FLEXIBLE)
                .filters(List.of(101L, 102L))
                .build();

        // --- 2. Mock 데이터 준비 ---
        Member member = Member.builder().id(1L).email("test@example.com").build();

        MainSearchResponse.PanelInfo panelInfo = new MainSearchResponse.PanelInfo();
        panelInfo.setPanelId("panel1");
        MainSearchResponse mainSearchResponse = MainSearchResponse.builder()
                .panels(List.of(panelInfo))
                .build();

        PanelWithRawDataDTO panelWithRawDataDTO = org.mockito.Mockito.mock(PanelWithRawDataDTO.class);
        lenient().when(panelWithRawDataDTO.getId()).thenReturn("panel1"); // lenient: unnecessary stubbing 방지

        SearchHistory searchHistory = SearchHistory.builder().id(100L).build();

        SearchResponseDTO.SearchResult.Summary summary = SearchResponseDTO.SearchResult.Summary.builder()
                .totalRespondents(100)
                .build();

        List<SearchResponseDTO.SearchResult.AppliedFilter> appliedFilters = List.of(
                SearchResponseDTO.SearchResult.AppliedFilter.builder()
                        .displayValue("Filter1")
                        .build()
        );

        FastChartResponseDTO.ChartData mainChartData = FastChartResponseDTO.ChartData.builder()
                .chartType("pie")
                .title("Main Chart")
                .data(Collections.emptyList())
                .build();

        FastChartResponseDTO.ChartRecommendationsResponse chartResponse = FastChartResponseDTO.ChartRecommendationsResponse.builder()
                .mainChart(mainChartData)
                .subCharts(Collections.emptyList())
                .build();

        Filter filter1 = Filter.builder().type("age").rawDataValue("20s").build();
        Filter filter2 = Filter.builder().type("gender").rawDataValue("male").build();

        // --- 3. Mocking 동작 정의 ---
        given(currentUserService.getCurrentUser()).willReturn(member);
        given(filterRepository.findByIds(anyList())).willReturn(List.of(filter1, filter2));
        given(fastApiService.getMainSearch(any(MainSearchRequest.class))).willReturn(mainSearchResponse);
        given(panelRepository.findPanelsWithRawDataByIds(anyList())).willReturn(List.of(panelWithRawDataDTO));
        given(searchHistoryService.makeSearchHistory(any(), any())).willReturn(searchHistory);
        given(summaryConverter.requestToDto(any(), anyList())).willReturn(summary);
        given(filterConverter.requestToDto(isNull(), any(SearchHistory.class))).willReturn(appliedFilters);
        given(fastApiService.getChartRecommendations(anyLong())).willReturn(chartResponse);

        // --- 4. 실제 서비스 호출 ---
        SearchResponseDTO.SearchResult result = naturalSearchService.search(request);
        System.out.println("Applied Filters: " + result.getAppliedFiltersSummary());

        // --- 5. 결과 검증 ---
        assertNotNull(result); // 결과가 null이 아닌지 확인
        assertEquals(100L, result.getSearchId()); // searchId 확인
        assertEquals(summary, result.getSummary()); // summary 확인
        assertEquals(appliedFilters, result.getAppliedFiltersSummary()); // 필터 적용 확인
        assertEquals("pie", result.getMainChart().getChartType()); // 차트 유형 확인
        assertEquals("Main Chart", result.getMainChart().getTitle()); // 차트 제목 확인

        // --- 6. 의존성 호출 검증 ---
        verify(currentUserService).getCurrentUser();
        verify(fastApiService).getMainSearch(any(MainSearchRequest.class));
        verify(panelRepository).findPanelsWithRawDataByIds(anyList());
        verify(searchHistoryService).makeSearchHistory(any(), any());
        verify(fastApiService).getChartRecommendations(100L);
    }
}
