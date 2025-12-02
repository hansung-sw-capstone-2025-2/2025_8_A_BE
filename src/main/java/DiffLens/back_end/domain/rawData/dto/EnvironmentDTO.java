package DiffLens.back_end.domain.rawData.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EnvironmentDTO {
    @JsonProperty("버리기아까운물건")
    private String itemNotThrownAway;

    @JsonProperty("비닐절감")
    private String reducePlastic;
}
