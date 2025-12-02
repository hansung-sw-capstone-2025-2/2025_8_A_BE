package DiffLens.back_end.domain.rawData.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SurveyResponseDTO {
    @JsonProperty("건강생활")
    private HealthDTO health;

    @JsonProperty("소비습관")
    private ConsumptionDTO consumption;

    @JsonProperty("라이프스타일")
    private LifestyleDTO lifestyle;

    @JsonProperty("디지털인식")
    private DigitalDTO digital;

    @JsonProperty("환경의식")
    private EnvironmentDTO environment;
}