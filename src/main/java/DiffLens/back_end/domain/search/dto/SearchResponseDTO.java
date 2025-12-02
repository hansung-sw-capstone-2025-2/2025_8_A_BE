package DiffLens.back_end.domain.search.dto;

import DiffLens.back_end.domain.panel.repository.projection.PanelWithRawDataDTO;
import DiffLens.back_end.global.dto.ResponsePageDTO;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

public class SearchResponseDTO {

    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SearchResult {

        // 큰 배열
        @JsonProperty("search_id")
        private Long searchId;

        private Summary summary;

        @JsonProperty("applied_filters_summary")
        private List<AppliedFilter> appliedFiltersSummary;

        @JsonProperty("main_chart")
        private ChartData mainChart;

        @JsonProperty("sub_charts")
        private List<ChartData> subCharts;

        // @JsonProperty("panel_data") // 개별 API로 분리
        // private SearchPanelDTO.PanelData panelData;

        // 중간배열
        @Getter
        @Builder
        @AllArgsConstructor
        @NoArgsConstructor
        public static class Summary {
            @JsonProperty("total_respondents")
            private Integer totalRespondents;

            @JsonProperty("average_age")
            private Double averageAge;

            @JsonProperty("data_capture_date")
            private String dataCaptureDate;

            @JsonProperty("confidence_level")
            private Integer confidenceLevel;
        }

        @Getter
        @Builder
        @AllArgsConstructor
        @NoArgsConstructor
        public static class AppliedFilter {
            @JsonProperty("key")
            private String key;

            @JsonProperty("display_value")
            private String displayValue;
        }

        @Getter
        @Builder
        @AllArgsConstructor
        @NoArgsConstructor
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
        @AllArgsConstructor
        @NoArgsConstructor
        public static class ChartDataPoint {
            private String category;

            private Integer value;

            // stacked-bar, infographic 차트용 추가 필드들
            private Integer male;

            @JsonProperty("male_max")
            private Integer maleMax;

            private Integer female;

            @JsonProperty("female_max")
            private Integer femaleMax;

            // map 차트용
            private String id;

            private String name;
        }

    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class EachResponses {

        private List<String> keys; // columns
        private List<ResponseValues> values; // row에 들어가는 값 목록

        @JsonProperty("page_info")
        private ResponsePageDTO.OffsetLimitPageInfo pageInfo;

    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ResponseValues {
        @JsonProperty("respondent_id")
        private String respondentId;
        private String gender;
        private String age;
        private String residence;
        @JsonProperty("personal_income")
        private String personalIncome;
        @JsonProperty("concordance_rate")
        private String concordanceRate; // 일치율

        public static ResponseValues fromPanelDTO(PanelWithRawDataDTO panel, String concordanceRate) {
            return ResponseValues.builder()
                    .respondentId(panel.getId())
                    .gender(panel.getGender() != null ? panel.getGender().getDisplayValue() : null)
                    .age(panel.getAge() != null ? panel.getAge().toString() : null)
                    .residence(panel.getResidence())
                    .residence(panel.getResidence())
                    .personalIncome(panel.getPersonalIncome())
                    .concordanceRate(concordanceRate)
                    .build();
        }

    }

    // 추천 검색 응답 DTO
    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Recommends {
        private List<Recommend> recommendations;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Recommend {
        private Long id;
        private String title;
        private String description;
    }

}
