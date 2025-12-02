package DiffLens.back_end.domain.library.service.command;

import DiffLens.back_end.domain.library.dto.LibraryCreateResult;
import DiffLens.back_end.domain.library.dto.LibraryRequestDto;
import DiffLens.back_end.domain.library.entity.*;
import DiffLens.back_end.domain.library.repository.LibraryPanelRepository;
import DiffLens.back_end.domain.library.repository.LibraryRepository;
import DiffLens.back_end.domain.library.repository.SearchHistoryLibraryRepository;
import DiffLens.back_end.domain.library.utils.LibraryValidation;
import DiffLens.back_end.domain.members.entity.Member;
import DiffLens.back_end.domain.members.service.auth.CurrentUserService;
import DiffLens.back_end.domain.panel.entity.Panel;
import DiffLens.back_end.domain.panel.repository.PanelRepository;
import DiffLens.back_end.domain.search.entity.SearchHistory;
import DiffLens.back_end.domain.search.repository.SearchHistoryRepository;
import DiffLens.back_end.global.responses.code.status.error.ErrorStatus;
import DiffLens.back_end.global.responses.code.status.error.SearchStatus;
import DiffLens.back_end.global.responses.exception.handler.ErrorHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class LibraryCommandServiceImpl implements  LibraryCommandService {

    private final SearchHistoryRepository searchHistoryRepository;
    private final LibraryRepository libraryRepository;
    private final SearchHistoryLibraryRepository searchHistoryLibraryRepository;
    private final PanelRepository panelRepository;
    private final LibraryPanelRepository libraryPanelRepository;

    private final CurrentUserService currentUserService;

    @Transactional
    public LibraryCreateResult createLibrary(LibraryRequestDto.Create request) {

        Member member = currentUserService.getCurrentUser();

        // 1. SearchHistory 검증
        SearchHistory history = searchHistoryRepository.findById(request.getSearchHistoryId())
                .orElseThrow(() -> new ErrorHandler(ErrorStatus.BAD_REQUEST));

        // 2. 권한 검증 - 본인의 검색 기록만 라이브러리로 저장 가능
        if (!history.getMember().getId().equals(member.getId())) {
            throw new ErrorHandler(ErrorStatus.FORBIDDEN);
        }

        // 3. Library 생성 (SearchHistory 참조 제거)
        // 패널 ID 결정: 요청에 있으면 사용, 없으면 검색기록의 패널 ID 사용
        List<String> panelIds = request.getPanelIds() != null
                ? request.getPanelIds()
                : history.getPanelIds();

        Library library = Library.builder()
                .libraryName(request.getLibraryName())
                .tags(request.getTags())
                .panelIds(panelIds != null ? panelIds : List.of())
                .member(member)
                .build();

        library = libraryRepository.save(library);

        // 4. Library-SearchHistory 다대다 관계 생성
        SearchHistoryLibrary searchHistoryLibrary = SearchHistoryLibrary.builder()
                .id(new SearchHistoryLibraryKey(history.getId(), library.getId()))
                .library(library)
                .history(history)
                .build();

        searchHistoryLibraryRepository.save(searchHistoryLibrary);

        // 5. Panel 관계 생성
        int panelCount = 0;
        if (panelIds != null && !panelIds.isEmpty()) {
            createLibraryPanels(library, panelIds);
            panelCount = panelIds.size();
        }

        return LibraryCreateResult.of(library, panelCount);
    }

    @Transactional
    public LibraryCreateResult addSearchHistoryToLibrary(Long libraryId, Long searchHistoryId) {

        Member member = currentUserService.getCurrentUser();

        // 1. 라이브러리 조회 및 권한 검증
        Library library = libraryRepository.findById(libraryId)
                .orElseThrow(() -> new ErrorHandler(ErrorStatus.BAD_REQUEST));

        // 라이브러리 내 소유인지 확인
        LibraryValidation.checkIsMyLibrary(library, member);

        // 2. 검색기록 조회 및 권한 검증
        SearchHistory searchHistory = searchHistoryRepository.findById(searchHistoryId)
                .orElseThrow(() -> new ErrorHandler(ErrorStatus.BAD_REQUEST));

        if (!searchHistory.getMember().getId().equals(member.getId())) {
            throw new ErrorHandler(ErrorStatus.FORBIDDEN);
        }

        // 3. 패널 ID 병합 (중복 제거)
        List<String> existingPanelIds = library.getPanelIds();
        List<String> newPanelIds = searchHistory.getPanelIds();
        List<String> mergedPanelIds = Stream.concat(
                existingPanelIds.stream(),
                newPanelIds.stream()).distinct().collect(Collectors.toList());

        // 4. 새로운 패널들만 LibraryPanel에 추가
        List<String> newPanelsToAdd = newPanelIds.stream()
                .filter(panelId -> !existingPanelIds.contains(panelId))
                .collect(Collectors.toList());

        int addedPanelCount = 0;
        if (!newPanelsToAdd.isEmpty()) {
            createLibraryPanels(library, newPanelsToAdd);
            addedPanelCount = newPanelsToAdd.size();
        }

        // 5. Library 엔티티의 panelIds 업데이트
        // 기존 엔티티의 panelIds만 업데이트 (AuditingEntityListener가 updatedAt 자동 관리)
        library.setPanelIds(mergedPanelIds);
        library = libraryRepository.save(library);

        // 6. SearchHistoryLibrary 관계 생성 (존재하지 않을 때만)
        SearchHistoryLibraryKey searchHistoryLibraryKey = new SearchHistoryLibraryKey(searchHistoryId,
                libraryId);
        boolean relationshipExists = searchHistoryLibraryRepository.existsById(searchHistoryLibraryKey);

        if (!relationshipExists) {
            SearchHistoryLibrary searchHistoryLibrary = SearchHistoryLibrary.builder()
                    .id(searchHistoryLibraryKey)
                    .library(library)
                    .history(searchHistory)
                    .build();
            searchHistoryLibraryRepository.save(searchHistoryLibrary);
        }

        return LibraryCreateResult.of(library, addedPanelCount);
    }

    private void createLibraryPanels(Library library, List<String> panelIds) {
        List<Panel> panels = panelRepository.findByIdList(panelIds);

        if (panels.size() != panelIds.size()) {
            throw new ErrorHandler(SearchStatus.NO_RESULT);
        }

        List<LibraryPanel> libraryPanels = panels.stream()
                .map(panel -> LibraryPanel.builder()
                        .id(new LibraryPanelKey(panel.getId(), library.getId()))
                        .library(library)
                        .panel(panel)
                        .build())
                .toList();

        libraryPanelRepository.saveAll(libraryPanels);
    }

}
