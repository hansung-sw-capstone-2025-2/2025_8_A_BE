package DiffLens.back_end.global.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

public class ResponsePageDTO {

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CursorPageInfo {
        @JsonProperty("next_cursor")
        private Integer nextCursor;

        @JsonProperty("has_next")
        private Boolean hasNext;

        private Integer limit;

        @JsonProperty("current_page_count")
        private Integer currentPageCount;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class OffsetLimitPageInfo {

        private Integer offset;

        @JsonProperty("current_page")
        private Integer currentPage;

        @JsonProperty("current_page_count")
        private Integer currentPageCount;

        @JsonProperty("total_page_count")
        private Integer totalPageCount;

        private Integer limit;

        @JsonProperty("total_count")
        private Long totalCount;

        @JsonProperty("has_next")
        private Boolean hasNext;

        @JsonProperty("has_previous")
        private Boolean hasPrevious;

        public static OffsetLimitPageInfo from(Page<?> page) {
            return OffsetLimitPageInfo.builder()
                    .offset(page.getPageable().getPageNumber() * page.getPageable().getPageSize())
                    .currentPage(page.getNumber() + 1)
                    .currentPageCount(page.getNumberOfElements())
                    .totalPageCount(page.getTotalPages())
                    .limit(page.getSize())
                    .totalCount(page.getTotalElements())
                    .hasNext(page.hasNext())
                    .hasPrevious(page.hasPrevious())
                    .build();
        }

    }

}
