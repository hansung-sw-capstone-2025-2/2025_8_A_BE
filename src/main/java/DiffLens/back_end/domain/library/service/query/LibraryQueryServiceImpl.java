package DiffLens.back_end.domain.library.service.query;

import DiffLens.back_end.domain.library.converter.dtoConvert.LibraryQueryConverter;
import DiffLens.back_end.domain.library.dto.LibraryResponseDTO;
import DiffLens.back_end.domain.library.entity.Library;
import DiffLens.back_end.domain.library.entity.SearchHistoryLibrary;
import DiffLens.back_end.domain.library.repository.LibraryPanelRepository;
import DiffLens.back_end.domain.library.repository.LibraryRepository;
import DiffLens.back_end.domain.library.repository.SearchHistoryLibraryRepository;
import DiffLens.back_end.domain.library.utils.LibraryStatisticUtils;
import DiffLens.back_end.domain.library.utils.LibraryValidation;
import DiffLens.back_end.domain.members.entity.Member;
import DiffLens.back_end.domain.members.service.auth.CurrentUserService;
import DiffLens.back_end.domain.panel.entity.Panel;
import DiffLens.back_end.domain.panel.repository.PanelRepository;
import DiffLens.back_end.domain.panel.repository.projection.PanelWithRawDataDTO;
import DiffLens.back_end.domain.search.entity.SearchHistory;
import DiffLens.back_end.domain.search.service.interfaces.SearchPanelService;
import DiffLens.back_end.global.dto.ResponsePageDTO;
import DiffLens.back_end.global.responses.code.status.error.ErrorStatus;
import DiffLens.back_end.global.responses.exception.handler.ErrorHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LibraryQueryServiceImpl implements LibraryQueryService {

    private final LibraryRepository libraryRepository;
    private final LibraryPanelRepository libraryPanelRepository;
    private final PanelRepository panelRepository;
    private final SearchHistoryLibraryRepository searchHistoryLibraryRepository;

    private final SearchPanelService searchPanelService;
    private final CurrentUserService currentUserService;

    @Transactional(readOnly = true)
    public LibraryResponseDTO.ListResult getLibrariesByMember() {

        Member member = currentUserService.getCurrentUser();

        List<Library> libraries = libraryRepository.findByMemberOrderByCreatedDateDesc(member);

        List<LibraryResponseDTO.ListResult.LibraryItem> libraryItems = libraries.stream()
                .map(library -> {
                    int panelCount = libraryPanelRepository.countByLibraryId(library.getId());
                    return LibraryResponseDTO.ListResult.LibraryItem.from(library, panelCount);
                })
                .toList();

        return LibraryResponseDTO.ListResult.builder()
                .libraries(libraryItems)
                .cursorPageInfo(null) // 페이징이 필요하면 추후 구현
                .build();
    }

    @Transactional(readOnly = true)
    public LibraryResponseDTO.LibraryDetail getLibraryDetail(Long libraryId) {

        Member member = currentUserService.getCurrentUser();

        // 1. 라이브러리 조회 및 권한 검증
        Library library = libraryRepository.findById(libraryId)
                .orElseThrow(() -> new ErrorHandler(ErrorStatus.BAD_REQUEST));

        // 라이브러리 내 소유인지 확인
        LibraryValidation.checkIsMyLibrary(library, member);

        // 2. 패널 정보 조회
        List<Panel> panels = panelRepository.findByIdList(library.getPanelIds());
        List<LibraryResponseDTO.LibraryDetail.PanelInfo> panelInfos = LibraryQueryConverter.toPanelInfos(panels);

        // 3. 연결된 검색기록 조회
        List<SearchHistoryLibrary> searchHistoryLibraries = searchHistoryLibraryRepository
                .findByLibraryId(libraryId);
        List<LibraryResponseDTO.LibraryDetail.SearchHistoryInfo> searchHistoryInfos = LibraryQueryConverter.toSearchHistoryInfos(searchHistoryLibraries);

        // 4. 통계 정보 생성
        LibraryResponseDTO.LibraryDetail.Statistics statistics = LibraryStatisticUtils.createStatistics(panels);

        return LibraryResponseDTO.LibraryDetail.builder()
                .libraryId(library.getId())
                .libraryName(library.getLibraryName())
                .tags(library.getTags())
                .panelCount(panels.size())
                .panelIds(library.getPanelIds())
                .panels(panelInfos)
                .searchHistories(searchHistoryInfos)
                .statistics(statistics)
                .createdAt(library.getCreatedDate() != null ? library.getCreatedDate().toString() : null)
                .updatedAt(library.getUpdatedAt() != null ? library.getUpdatedAt().toString() : null)
                .build();
    }

    /**
     * 라이브러리 패널 목록 조회 (페이징, 일치율 없음)
     */
    @Transactional(readOnly = true)
    public LibraryResponseDTO.LibraryPanels getLibraryPanels(Long libraryId, Integer pageNum, Integer size) {

        Member member = currentUserService.getCurrentUser();

        // 1. 페이지 번호 예외처리
        if (pageNum < 1) {
            throw new ErrorHandler(ErrorStatus.PAGE_NO_INVALID);
        }

        // 2. 라이브러리 조회 및 권한 검증
        Library library = libraryRepository.findById(libraryId)
                .orElseThrow(() -> new ErrorHandler(ErrorStatus.BAD_REQUEST));

        // 라이브러리 내 소유인지 확인
        LibraryValidation.checkIsMyLibrary(library, member);

        // 3. 패널 ID 배열 조회
        List<String> panelIds = library.getPanelIds();
        if (panelIds == null || panelIds.isEmpty()) {
            return LibraryQueryConverter.emptyPanelResponse(size);
        }

        // 4. 페이징을 위한 Pageable 객체 생성
        Pageable pageable = PageRequest.of(pageNum - 1, size);

        // 5. PanelId 목록을 이용해서 Panel 조회
        Page<PanelWithRawDataDTO> panelDtoList = searchPanelService.getPanelDtoList(panelIds, pageable);

        // 6. 페이지 범위 초과 검사
        if (pageNum > panelDtoList.getTotalPages() && panelDtoList.getTotalPages() > 0) {
            throw new ErrorHandler(ErrorStatus.PAGE_NO_EXCEED);
        }

        // 7. Panel 목록을 응답 형식으로 변환 (일치율 없음)
        List<LibraryResponseDTO.LibraryPanels.PanelResponseValues> values = panelDtoList.stream()
                .map(panel -> LibraryResponseDTO.LibraryPanels.PanelResponseValues.builder()
                        .respondentId(panel.getId())
                        .gender(panel.getGender() != null ? panel.getGender().getDisplayValue() : null)
                        .age(panel.getAge() != null ? panel.getAge().toString() : null)
                        .residence(panel.getResidence())
                        .personalIncome(panel.getPersonalIncome())
                        .build())
                .toList();

        // 8. 페이징 정보 생성
        ResponsePageDTO.OffsetLimitPageInfo pageInfo = ResponsePageDTO.OffsetLimitPageInfo
                .from(panelDtoList);

        return LibraryResponseDTO.LibraryPanels.builder()
                .keys(List.of("respondent_id", "gender", "age", "residence", "personal_income"))
                .values(values)
                .pageInfo(pageInfo)
                .build();
    }

}
