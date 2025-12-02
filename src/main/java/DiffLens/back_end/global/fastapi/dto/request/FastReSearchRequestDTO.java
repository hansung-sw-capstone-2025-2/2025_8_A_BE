package DiffLens.back_end.global.fastapi.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 *
 * 재검색
 * Spring Boot ->  Fast API 요청 형태
 *
 */
public class FastReSearchRequestDTO {

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ReSearch {

        private String query;
        private String searchMode;
        private ReSearchFilters filters;
        private List<String> existingPanelIds;

    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ReSearchFilters {
        private List<String> occupations;
    }

}
