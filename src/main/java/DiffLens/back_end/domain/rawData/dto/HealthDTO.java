package DiffLens.back_end.domain.rawData.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HealthDTO {
    @JsonProperty("체력관리")
    private String fitness;

    @JsonProperty("피부상태")
    private String skinCondition;

    @JsonProperty("땀불편")
    private String sweatIssue;

    @JsonProperty("다이어트")
    private String diet;

    @JsonProperty("야식방법")
    private String lateSnackMethod;

    @JsonProperty("여름간식")
    private String summerSnack;

    @JsonProperty("초콜릿섭취")
    private String chocolateIntake;
}