package DiffLens.back_end.global.fastapi.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

public class FastHomeResponseDTO {

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class HomeRecommend{

        @JsonProperty("recommendations")
        private List<Recommendation> recommendations;

        @JsonProperty("strategy_used")
        private String strategyUsed;

        @JsonProperty("total_count")
        private int totalCount;

        @JsonProperty("patterns")
        private Object patterns;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Recommendation {

        @JsonProperty("id")
        private Long id;

        @JsonProperty("query")
        private String query;

        @JsonProperty("count")
        private String count;

        @JsonProperty("description")
        private String description;

        @JsonProperty("category")
        private String category;

        @JsonProperty("personalized")
        private Boolean personalized;

        @JsonProperty("search_params")
        private Object searchParams;

        @JsonProperty("recommended_mode")
        private String recommendedMode;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SearchParams {

        @JsonProperty("age_group")
        private String ageGroup;

        @JsonProperty("gender")
        private String gender;

        @JsonProperty("region")
        private String region;

        @JsonProperty("marital_status")
        private String maritalStatus;

        @JsonProperty("occupation")
        private List<String> occupation;

        @JsonProperty("brands")
        private List<String> brands;

        @JsonProperty("limit")
        private int limit;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Patterns {

        @JsonProperty("demographic")
        private Demographic demographic;

        @JsonProperty("survey_consumption")
        private SurveyConsumption surveyConsumption;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Demographic {

        @JsonProperty("연령대")
        private List<String> 연령대;

        @JsonProperty("성별")
        private List<String> 성별;

        @JsonProperty("거주지역")
        private List<String> 거주지역;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SurveyConsumption {

        @JsonProperty("OTT개수")
        private List<String> ott개수;
    }
}
