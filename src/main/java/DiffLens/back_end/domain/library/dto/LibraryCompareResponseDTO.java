package DiffLens.back_end.domain.library.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

public class LibraryCompareResponseDTO {

  @Getter
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class CompareResult {

    @JsonProperty("group1")
    private GroupInfo group1;

    @JsonProperty("group2")
    private GroupInfo group2;

    @JsonProperty("key_characteristics")
    private List<KeyCharacteristic> keyCharacteristics;

    @JsonProperty("basic_comparison")
    private Comparisons comparisons;

    @JsonProperty("insights")
    private Insights insights;
  }

  @Getter
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class GroupInfo {

    @JsonProperty("library_id")
    private Long libraryId;

    @JsonProperty("library_name")
    private String libraryName;

    @JsonProperty("summary")
    private String summary;

    @JsonProperty("total_count")
    private Integer totalCount;

    @JsonProperty("filters")
    private List<Filter> filters;
  }

  @Getter
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class Filter {

    @JsonProperty("key")
    private String key;

    @JsonProperty("values")
    private List<String> values;
  }

  @Getter
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class KeyCharacteristic {

    @JsonProperty("characteristic")
    private String characteristic;

    @JsonProperty("description")
    private String description;

    @JsonProperty("group1_percentage")
    private Integer group1Percentage;

    @JsonProperty("group2_percentage")
    private Integer group2Percentage;

    @JsonProperty("difference")
    private Integer difference;
  }

  @Getter
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class Comparisons {

    @JsonProperty("group1")
    private GroupMetrics group1;

    @JsonProperty("group2")
    private GroupMetrics group2;
  }

  @Getter
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class GroupMetrics {

    @JsonProperty("male")
    private Integer male;

    @JsonProperty("female")
    private Integer female;

    @JsonProperty("seoul")
    private Integer seoul;

    @JsonProperty("gyeonggi")
    private Integer gyeonggi;

    @JsonProperty("busan")
    private Integer busan;

    @JsonProperty("region_etc")
    private Integer regionEtc;

    @JsonProperty("avg_age")
    private Double avgAge;

    @JsonProperty("avg_family")
    private Double avgFamily;

    @JsonProperty("avg_children")
    private Double avgChildren;

    @JsonProperty("rate_possessing_car")
    private Integer ratePossessingCar;

    @JsonProperty("avg_personal_income")
    private Integer avgPersonalIncome;

    @JsonProperty("avg_family_income")
    private Integer avgFamilyIncome;
  }

  @Getter
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class Insights {

    @JsonProperty("difference")
    private String difference;

    @JsonProperty("common")
    private String common;

    @JsonProperty("implication")
    private String implication;
  }
}
