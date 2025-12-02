package DiffLens.back_end.domain.library.converter.dtoConvert;

import DiffLens.back_end.domain.library.dto.LibraryResponseDTO;
import DiffLens.back_end.domain.library.entity.Library;
import DiffLens.back_end.domain.library.entity.SearchHistoryLibrary;
import DiffLens.back_end.domain.members.entity.Member;
import DiffLens.back_end.domain.panel.entity.Panel;
import DiffLens.back_end.domain.search.entity.SearchHistory;
import DiffLens.back_end.global.dto.ResponsePageDTO;
import DiffLens.back_end.global.responses.code.status.error.ErrorStatus;
import DiffLens.back_end.global.responses.exception.handler.ErrorHandler;

import java.util.List;

public class LibraryQueryConverter {

    public static List<LibraryResponseDTO.LibraryDetail.PanelInfo> toPanelInfos(List<Panel> panels) {
        return panels.stream()
                .map(panel -> LibraryResponseDTO.LibraryDetail.PanelInfo.builder()
                        .panelId(panel.getId())
                        .gender(panel.getGender() != null ? panel.getGender().toString() : null)
                        .age(panel.getAge())
                        .ageGroup(panel.getAgeGroup())
                        .residence(panel.getRegion())
                        .maritalStatus(panel.getMaritalStatus())
                        .childrenCount(panel.getChildrenCount())
                        .occupation(panel.getOccupation())
                        .profileSummary(panel.getProfileSummary())
                        .build())
                .toList();
    }

    public static List<LibraryResponseDTO.LibraryDetail.SearchHistoryInfo> toSearchHistoryInfos(
            List<SearchHistoryLibrary> historyLinks
    ) {
        return historyLinks.stream()
                .map(shl -> {
                    SearchHistory history = shl.getHistory();
                    return LibraryResponseDTO.LibraryDetail.SearchHistoryInfo.builder()
                            .searchHistoryId(history.getId())
                            .content(history.getContent())
                            .date(toStringOrNull(history.getDate()))
                            .panelCount(history.getPanelIds() != null ? history.getPanelIds().size() : 0)
                            .createdAt(toStringOrNull(history.getCreatedDate()))
                            .build();
                })
                .toList();
    }

    public static LibraryResponseDTO.LibraryPanels emptyPanelResponse(Integer size) {
        return LibraryResponseDTO.LibraryPanels.builder()
                .keys(List.of("respondent_id", "gender", "age", "residence", "personal_income"))
                .values(List.of())
                .pageInfo(ResponsePageDTO.OffsetLimitPageInfo.builder()
                        .offset(0)
                        .currentPage(1)
                        .currentPageCount(0)
                        .totalPageCount(0)
                        .limit(size)
                        .totalCount(0L)
                        .hasNext(false)
                        .hasPrevious(false)
                        .build())
                .build();
    }

    private static String toStringOrNull(Object obj) {
        return obj != null ? obj.toString() : null;
    }

}
