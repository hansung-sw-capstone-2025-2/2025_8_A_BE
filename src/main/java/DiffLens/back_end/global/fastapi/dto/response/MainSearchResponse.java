package DiffLens.back_end.global.fastapi.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;
import java.util.Map;

@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "메인 검색 응답 DTO")
public class MainSearchResponse {

    @Schema(description = "검색 결과 고유 ID")
    @JsonProperty("search_id")
    private String searchId;

    @Schema(description = "원본 쿼리 (자연어)")
    private String query;

    @Schema(description = "검색된 패널 목록")
    private List<PanelInfo> panels;

    @Schema(description = "총 검색 결과 수")
    @JsonProperty("total_couont")
    private int totalCount;

    @Schema(description = "사용된 검색 모드")
    @JsonProperty("search_mode")
    private String searchMode;

    @Schema(description = "적용된 필터")
    @JsonProperty("applied_filters")
    private Map<String, Object> appliedFilters;

    @Schema(description = "검색 방법: 'natural_language', 'recommendation', 'structured_filter'")
    @JsonProperty("search_method")
    private String searchMethod;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Schema(description = "패널 정보 DTO")
    public static class PanelInfo {

        @Schema(description = "패널 ID")
        @JsonProperty("panel_id")
        private String panelId;

        private Integer age;
        private String gender;
        private String region;
        private String occupation;
        @JsonProperty("marital_status")
        private String maritalStatus;
        @JsonProperty("phone_brand")
        private String phoneBrand;
        @JsonProperty("car_brand")
        private String carBrand;
        @JsonProperty("profile_summary")
        private String profileSummary;

        @Schema(description = "해시태그 목록")
        private List<String> hashtags;

        @JsonProperty("similarity")
        private Double similarity;
    }

}

