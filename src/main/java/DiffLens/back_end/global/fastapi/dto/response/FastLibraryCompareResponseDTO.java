package DiffLens.back_end.global.fastapi.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

/**
 * Fast API -> Spring Boot 응답 형태
 * 라이브러리 비교 응답
 */
public class FastLibraryCompareResponseDTO {

  @Getter
  @Setter
  public static class CompareResult {
    @JsonProperty("cohort_1")
    private CohortBasicInfo cohort1;

    @JsonProperty("cohort_2")
    private CohortBasicInfo cohort2;

    @JsonProperty("comparisons")
    private List<MetricComparison> comparisons;

    @JsonProperty("basic_info")
    private List<BasicInfoComparison> basicInfo;

    @JsonProperty("characteristics")
    private List<CharacteristicComparison> characteristics;

    @JsonProperty("key_insights")
    private KeyInsights keyInsights; // nullable

    @JsonProperty("summary")
    private Summary summary;

    @JsonProperty("region_distribution")
    private RegionDistribution regionDistribution; // nullable

    @JsonProperty("gender_distribution")
    private GenderDistribution genderDistribution; // nullable
  }

  @Getter
  @Setter
  public static class CohortBasicInfo {
    @JsonProperty("cohort_id")
    private String cohortId;

    @JsonProperty("cohort_name")
    private String cohortName;

    @JsonProperty("panel_count")
    private Integer panelCount;

    @JsonProperty("created_at")
    private String createdAt; // nullable
  }

  @Getter
  @Setter
  public static class MetricComparison {
    @JsonProperty("metric_name")
    private String metricName;

    @JsonProperty("metric_label")
    private String metricLabel;

    @JsonProperty("cohort_1_data")
    private Map<String, Integer> cohort1Data;

    @JsonProperty("cohort_2_data")
    private Map<String, Integer> cohort2Data;

    @JsonProperty("cohort_1_percentage")
    private Map<String, Double> cohort1Percentage;

    @JsonProperty("cohort_2_percentage")
    private Map<String, Double> cohort2Percentage;

    @JsonProperty("statistical_test")
    private StatisticalTest statisticalTest; // nullable
  }

  @Getter
  @Setter
  public static class StatisticalTest {
    @JsonProperty("test_type")
    private String testType;

    @JsonProperty("chi_square")
    private Double chiSquare;

    @JsonProperty("p_value")
    private Double pValue;

    @JsonProperty("degrees_of_freedom")
    private Integer degreesOfFreedom;

    @JsonProperty("is_significant")
    private Boolean isSignificant;

    @JsonProperty("interpretation")
    private String interpretation;

    @JsonProperty("error")
    private String error; // nullable, 에러 발생 시에만 존재
  }

  @Getter
  @Setter
  public static class BasicInfoComparison {
    @JsonProperty("metric_name")
    private String metricName;

    @JsonProperty("metric_label")
    private String metricLabel;

    @JsonProperty("cohort_1_value")
    private Double cohort1Value; // nullable

    @JsonProperty("cohort_2_value")
    private Double cohort2Value; // nullable

    @JsonProperty("difference")
    private Double difference; // nullable

    @JsonProperty("difference_percentage")
    private Double differencePercentage; // nullable
  }

  @Getter
  @Setter
  public static class CharacteristicComparison {
    @JsonProperty("characteristic")
    private String characteristic;

    @JsonProperty("cohort_1_percentage")
    private Double cohort1Percentage;

    @JsonProperty("cohort_2_percentage")
    private Double cohort2Percentage;

    @JsonProperty("cohort_1_count")
    private Integer cohort1Count;

    @JsonProperty("cohort_2_count")
    private Integer cohort2Count;

    @JsonProperty("difference_percentage")
    private Double differencePercentage;
  }

  @Getter
  @Setter
  public static class KeyInsights {
    @JsonProperty("main_differences")
    private String mainDifferences;

    @JsonProperty("commonalities")
    private String commonalities;

    @JsonProperty("implications")
    private String implications;
  }

  @Getter
  @Setter
  public static class Summary {
    @JsonProperty("total_metrics")
    private Integer totalMetrics;

    @JsonProperty("significant_differences")
    private Integer significantDifferences;

    @JsonProperty("comparison_summary")
    private String comparisonSummary;

    @JsonProperty("interpretation")
    private String interpretation;
  }

  @Getter
  @Setter
  public static class RegionDistribution {
    @JsonProperty("cohort_1")
    private Map<String, Double> cohort1; // 지역명 → 비율 (%)

    @JsonProperty("cohort_2")
    private Map<String, Double> cohort2; // 지역명 → 비율 (%)
  }

  @Getter
  @Setter
  public static class GenderDistribution {
    @JsonProperty("cohort_1")
    private Map<String, Double> cohort1; // "남성" → 비율, "여성" → 비율 (%)

    @JsonProperty("cohort_2")
    private Map<String, Double> cohort2; // "남성" → 비율, "여성" → 비율 (%)
  }
}
