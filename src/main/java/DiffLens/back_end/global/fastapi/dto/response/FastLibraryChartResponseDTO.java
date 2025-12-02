package DiffLens.back_end.global.fastapi.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Map;

/**
 * 라이브러리로부터 차트 생성 응답 DTO
 */
public class FastLibraryChartResponseDTO {

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class LibraryChartResponse {
        @JsonProperty("library_name")
        private String libraryName;

        @JsonProperty("panel_count")
        private Integer panelCount;

        @JsonProperty("panels")
        private List<PanelInfo> panels;

        @JsonProperty("cohort_stats")
        private Map<String, Map<String, Integer>> cohortStats;

        @JsonProperty("main_chart")
        private FastChartResponseDTO.ChartData mainChart;

        @JsonProperty("sub_charts")
        private List<FastChartResponseDTO.ChartData> subCharts;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class PanelInfo {
        @JsonProperty("panel_id")
        private String panelId;

        private Integer age;

        private String gender;

        private String residence;

        private String occupation;

        @JsonProperty("marital_status")
        private String maritalStatus;

        @JsonProperty("phone_brand")
        private String phoneBrand;

        @JsonProperty("car_brand")
        private String carBrand;
    }
}

