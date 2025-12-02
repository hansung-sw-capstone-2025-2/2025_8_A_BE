package DiffLens.back_end.domain.search.service.implement;

import DiffLens.back_end.domain.panel.repository.projection.PanelWithRawDataDTO;
import DiffLens.back_end.global.fastapi.dto.response.FastNaturalLanguageResponseDTO;
import DiffLens.back_end.domain.rawData.service.RawDataService;
import DiffLens.back_end.domain.search.dto.SearchRequestDTO;
import DiffLens.back_end.domain.search.dto.SearchResponseDTO;
import DiffLens.back_end.domain.search.entity.SearchFilter;
import DiffLens.back_end.domain.search.entity.SearchHistory;
import DiffLens.back_end.domain.search.repository.SearchHistoryRepository;
import DiffLens.back_end.domain.search.service.interfaces.FilterService;
import DiffLens.back_end.domain.search.service.interfaces.SearchHistoryService;
import DiffLens.back_end.domain.search.service.interfaces.SearchPanelService;
import DiffLens.back_end.global.dto.ResponsePageDTO;
import DiffLens.back_end.global.fastapi.dto.response.MainSearchResponse;
import DiffLens.back_end.global.responses.code.status.error.ErrorStatus;
import DiffLens.back_end.global.responses.code.status.error.SearchStatus;
import DiffLens.back_end.global.responses.exception.handler.ErrorHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchHistoryServiceImpl implements SearchHistoryService {

    private final SearchHistoryRepository historyRepository;
    private final SearchPanelService searchPanelService;
    private final RawDataService rawDataService;
    private final FilterService filterService;

    private final List<String> keys = List.of("응답자ID-respondent_id", "성별-gender", "나이-age", "거주지-residence", "월소득-personal_income", "일치율-concordance_rate");

    @Override
    public SearchHistory makeSearchHistory(SearchRequestDTO.NaturalLanguage request, MainSearchResponse fastApiResponse) {

        // SearchHistory 객체 생성
        SearchHistory searchHistory = historyRepository.findById(Long.valueOf(fastApiResponse.getSearchId()))
                .orElseThrow(() -> new ErrorHandler(SearchStatus.SEARCH_HISTORY_NOT_FOUND));

        // SearchFilter 생성
        SearchFilter searchFilter = filterService.makeFilter(request.getFilters(), searchHistory);

        // SearchHistory의 Filter를 지정
        searchHistory.setFilter(searchFilter);

        return searchHistory;

    }

    /**
     *
     * 개별 응답 데이터 조회입니다.
     * 검색 결과에서 패널 정보와 일치율 정보를 가져와 페이징 처리하여 ( offset, limit 기반 ) 반환합니다.
     * 현재 패널 정보 항목은 keys 변수와 같습니다.
     * 항목이 바뀐다면 keys와 SearchResponseDTO.ResponseValues의 필드를 수정해야 합니다.
     *
     * @param searchHistoryId searchHistory의 식별자
     * @param pageNum 페이지 번호. 1 이상의 값
     * @param size 페이지 크기
     * @return EachResponses 객체
     */
    @Override
    public SearchResponseDTO.EachResponses getEachResponses(Long searchHistoryId, Integer pageNum, Integer size) {

        // 페이지 번호 예외처리
        if(pageNum < 1){
            throw new ErrorHandler(ErrorStatus.PAGE_NO_INVALID);
        }
        // searchHistoryId로 검색기록 불러옴
        SearchHistory searchHistory = historyRepository.findById(searchHistoryId)
                .orElseThrow(() -> new ErrorHandler(SearchStatus.SEARCH_HISTORY_NOT_FOUND));

        // 검색기록에서 패널ID 목록이랑 일치율 목록 불러옴
        // 두 리스트의 순서쌍이 이루어짐
        List<String> panelIds = searchHistory.getPanelIds();
        List<Float> concordanceRate = searchHistory.getConcordanceRate();

        // 페이징을 위한 Pageable 객체 생성
        // 페이지는 1부터 시작
        Pageable pageable = PageRequest.of(pageNum - 1, size);

        // PanelId 목록을 이용해서 Panel 조회
        Page<PanelWithRawDataDTO> panelDtoList = searchPanelService.getPanelDtoList(panelIds, pageable);

        // 페이지 범위 초과 검사 추가
        if (pageNum > panelDtoList.getTotalPages() && panelDtoList.getTotalPages() > 0) {
            throw new ErrorHandler(ErrorStatus.PAGE_NO_EXCEED);
        }

        // Panel 목록 순회하며 응답보낼 데이터 파싱
        // panelIds와 concordanceRate의 순서쌍에 맞게 패널 정보와 일치율을 담음
        List<SearchResponseDTO.ResponseValues> values = new ArrayList<>(panelDtoList.stream()
                .map(panel -> {
                    int index = panelIds.indexOf(panel.getId()); //
                    String rate = (index != -1 && index < concordanceRate.size())
                            ? String.format("%.2f", concordanceRate.get(index)) : null;

                    return SearchResponseDTO.ResponseValues.fromPanelDTO(panel, rate);
                })
                .toList());

        // 유사도 기준 내림차순 정렬
        values.sort(
                Comparator.comparing(
                        SearchResponseDTO.ResponseValues::getConcordanceRate,
                        Comparator.nullsLast(Comparator.reverseOrder())
                )
        );
        //페이징 정보 생성
        ResponsePageDTO.OffsetLimitPageInfo pageInfo = ResponsePageDTO.OffsetLimitPageInfo.from(panelDtoList);

        return SearchResponseDTO.EachResponses.builder()
                .keys(keys)
                .values(values)
                .pageInfo(pageInfo)
                .build();
    }



}
