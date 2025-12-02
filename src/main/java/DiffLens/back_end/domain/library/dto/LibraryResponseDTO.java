package DiffLens.back_end.domain.library.dto;

import DiffLens.back_end.domain.library.entity.Library;
import DiffLens.back_end.global.dto.ResponsePageDTO;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

public class LibraryResponseDTO {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ListResult {

        private List<LibraryItem> libraries;

        @JsonProperty("page_info")
        private ResponsePageDTO.CursorPageInfo cursorPageInfo;

        @Getter
        @Builder
        @NoArgsConstructor
        @AllArgsConstructor
        public static class LibraryItem {

            @JsonProperty("library_id")
            private Long libraryId;

            @JsonProperty("library_name")
            private String libraryName;

            private List<String> tags;

            @JsonProperty("panel_count")
            private Integer panelCount;

            @JsonProperty("panel_ids")
            private List<String> panelIds;

            @JsonProperty("created_at")
            private String createdAt;

            public static LibraryItem from(Library library, int panelCount) {
                return LibraryItem.builder()
                        .libraryId(library.getId())
                        .libraryName(library.getLibraryName())
                        .tags(library.getTags())
                        .panelCount(panelCount)
                        .panelIds(library.getPanelIds())
                        .createdAt(library.getCreatedDate().toString())
                        .build();
            }
        }

    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateResult {

        @JsonProperty("library_id")
        private Long libraryId;

        @JsonProperty("library_name")
        private String libraryName;

        private List<String> tags;

        @JsonProperty("search_history_id")
        private Long searchHistoryId;

        @JsonProperty("panel_count")
        private Integer panelCount;

        @JsonProperty("panel_ids")
        private List<String> panelIds;

        @JsonProperty("created_at")
        private String createdAt;

        public static CreateResult from(Library library, Long searchHistoryId, int panelCount) {
            return CreateResult.builder()
                    .libraryId(library.getId())
                    .libraryName(library.getLibraryName())
                    .tags(library.getTags())
                    .searchHistoryId(searchHistoryId)
                    .panelCount(panelCount)
                    .panelIds(library.getPanelIds())
                    .createdAt(library.getCreatedDate() != null ? library.getCreatedDate().toString() : null)
                    .build();
        }
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LibraryDetail {

        @JsonProperty("library_id")
        private Long libraryId;

        @JsonProperty("library_name")
        private String libraryName;

        private List<String> tags;

        @JsonProperty("panel_count")
        private Integer panelCount;

        @JsonProperty("panel_ids")
        private List<String> panelIds;

        @JsonProperty("panels")
        private List<PanelInfo> panels;

        @JsonProperty("search_histories")
        private List<SearchHistoryInfo> searchHistories;

        @JsonProperty("statistics")
        private Statistics statistics;

        @JsonProperty("created_at")
        private String createdAt;

        @JsonProperty("updated_at")
        private String updatedAt;

        @Getter
        @Builder
        @NoArgsConstructor
        @AllArgsConstructor
        public static class PanelInfo {
            @JsonProperty("panel_id")
            private String panelId;

            @JsonProperty("gender")
            private String gender;

            @JsonProperty("age")
            private Integer age;

            @JsonProperty("age_group")
            private String ageGroup;

            @JsonProperty("residence")
            private String residence;

            @JsonProperty("marital_status")
            private String maritalStatus;

            @JsonProperty("children_count")
            private Integer childrenCount;

            @JsonProperty("occupation")
            private String occupation;

            @JsonProperty("profile_summary")
            private String profileSummary;
        }

        @Getter
        @Builder
        @NoArgsConstructor
        @AllArgsConstructor
        public static class SearchHistoryInfo {
            @JsonProperty("search_history_id")
            private Long searchHistoryId;

            @JsonProperty("content")
            private String content;

            @JsonProperty("date")
            private String date;

            @JsonProperty("panel_count")
            private Integer panelCount;

            @JsonProperty("created_at")
            private String createdAt;
        }

        @Getter
        @Builder
        @NoArgsConstructor
        @AllArgsConstructor
        public static class Statistics {
            @JsonProperty("total_panels")
            private Integer totalPanels;

            @JsonProperty("gender_distribution")
            private GenderDistribution genderDistribution;

            @JsonProperty("age_group_distribution")
            private AgeGroupDistribution ageGroupDistribution;

            @JsonProperty("residence_distribution")
            private ResidenceDistribution residenceDistribution;

            @Getter
            @Builder
            @NoArgsConstructor
            @AllArgsConstructor
            public static class GenderDistribution {
                private Integer male;
                private Integer female;
                private Integer none;
            }

            @Getter
            @Builder
            @NoArgsConstructor
            @AllArgsConstructor
            public static class AgeGroupDistribution {
                @JsonProperty("20대")
                private Integer twenties;
                @JsonProperty("30대")
                private Integer thirties;
                @JsonProperty("40대")
                private Integer forties;
                @JsonProperty("50대")
                private Integer fifties;
                @JsonProperty("60대+")
                private Integer sixtiesPlus;
            }

            @Getter
            @Builder
            @NoArgsConstructor
            @AllArgsConstructor
            public static class ResidenceDistribution {
                private Integer seoul;
                private Integer gyeonggi;
                private Integer busan;
                private Integer other;
            }
        }
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LibraryDashboard {
        @JsonProperty("library_id")
        private Long libraryId;

        @JsonProperty("library_name")
        private String libraryName;

        @JsonProperty("panel_count")
        private Integer panelCount;

        @JsonProperty("main_chart")
        private ChartData mainChart;

        @JsonProperty("sub_charts")
        private List<ChartData> subCharts;

        @Getter
        @Builder
        @NoArgsConstructor
        @AllArgsConstructor
        public static class ChartData {
            @JsonProperty("chart_type")
            private String chartType;

            private String metric;

            private String title;

            private String reasoning;

            private List<ChartDataPoint> data;
        }

        @Getter
        @Builder
        @NoArgsConstructor
        @AllArgsConstructor
        public static class ChartDataPoint {
            private String category;

            private Integer value;

            private Integer male;

            @JsonProperty("male_max")
            private Integer maleMax;

            private Integer female;

            @JsonProperty("female_max")
            private Integer femaleMax;

            private String id;

            private String name;
        }
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LibraryPanels {
        private List<String> keys;

        private List<PanelResponseValues> values;

        @JsonProperty("page_info")
        private ResponsePageDTO.OffsetLimitPageInfo pageInfo;

        @Getter
        @Builder
        @NoArgsConstructor
        @AllArgsConstructor
        public static class PanelResponseValues {
            @JsonProperty("respondent_id")
            private String respondentId;

            private String gender;

            private String age;

            private String residence;

            @JsonProperty("personal_income")
            private String personalIncome;
        }
    }

}
