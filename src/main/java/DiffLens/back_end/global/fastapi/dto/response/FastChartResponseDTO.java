package DiffLens.back_end.global.fastapi.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;
import java.util.Map;

/**
 * FastAPI 차트 추천 응답 DTO
 */
public class FastChartResponseDTO {

  @Getter
  @Setter
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  public static class ChartRecommendationsResponse {
    @JsonProperty("search_id")
    private Long searchId;

    @JsonProperty("panel_count")
    private Integer panelCount;

    @JsonProperty("original_query")
    private String originalQuery;

    @JsonProperty("cohort_stats")
    private Map<String, Map<String, Integer>> cohortStats;

    @JsonProperty("main_chart")
    private ChartData mainChart;

    @JsonProperty("sub_charts")
    private List<ChartData> subCharts;
  }

  @Getter
  @Setter
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  public static class ChartData {
    @JsonProperty("chart_type")
    private String chartType;

    private String metric;

    private String title;

    private String reasoning;

    private List<ChartDataPoint> data;
  }

  @Getter
  @Setter
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
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
