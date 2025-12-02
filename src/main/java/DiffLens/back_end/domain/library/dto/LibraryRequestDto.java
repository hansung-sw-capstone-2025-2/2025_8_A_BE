package DiffLens.back_end.domain.library.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

public class LibraryRequestDto {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Create {

        @NotNull(message = "검색 기록 ID는 필수입니다")
        @JsonProperty("search_history_id")
        private Long searchHistoryId;

        @NotBlank(message = "라이브러리 이름은 필수입니다")
        @JsonProperty("library_name")
        private String libraryName;

        @NotNull(message = "태그는 필수입니다")
        private List<String> tags;

        @JsonProperty("panel_ids")
        private List<String> panelIds; // 선택적: null이면 SearchHistory의 panelIds 사용
    }

}
