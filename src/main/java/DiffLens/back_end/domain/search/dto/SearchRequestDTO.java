package DiffLens.back_end.domain.search.dto;

import DiffLens.back_end.domain.search.enums.mode.QuestionMode;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class SearchRequestDTO {

    // 자연어 검색
    @Getter
    @Setter
    @Builder
    public static class NaturalLanguage{
        @NotBlank
        private String question;
        @NotNull
        private QuestionMode mode;
        @NotNull
        private List<Long> filters;
    }

    // 기존 검색 결과 기반 재검색
    @Getter
    @Setter
    public static class ExistingSearchResult{
        @NotNull
        private Long id;
        @NotBlank
        private String question;
        @NotNull
        private SearchFilters filters;
    }

    // 위 두 DTO 에서 공통으로 쓰이는 Filter DTO
    @Getter
    @Setter
    public static class SearchFilters{
//        private Integer count;
//        private String gender;
        private List<Long> filters; // age_group:TWENTY
//        private Respondent respondent;
//        @JsonProperty(value = "age_group")
//        private List<AgeGroup> ageGroup;
//        private List<Residence> region;
//        @JsonProperty(value = "martial_status")
//        private List<MartialStatus> martialStatus; // 결혼상태
//        private Children children;
//        private List<Occupation> occupation;// 직업

        // TODO : 추후 필터 추가 예정...

    }



}
