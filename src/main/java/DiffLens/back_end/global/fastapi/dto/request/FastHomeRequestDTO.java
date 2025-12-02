package DiffLens.back_end.global.fastapi.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

/**
 *
 * 홈화면 추천
 * Spring Boot ->  Fast API 요청 형태
 *
 */
@RequiredArgsConstructor
public class FastHomeRequestDTO {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class HomeRecommendRequest {

        @JsonProperty("search_history")
        private List<String> recentSearches;

        @JsonProperty("limit")
        private Integer limit;

        @JsonProperty("industry")
        private String industry;

    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class HomeRecommendByMemberRequest {

        @JsonProperty("member_id")
        private Long memberId;

        @JsonProperty("limit")
        private Integer limit;

        @JsonProperty("history_limit")
        private Integer historyLimit;

        @JsonProperty("industry")
        private String industry;

    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class HomeRecommendOnboarding{
        private String job;
        private String industry;
    }

}
