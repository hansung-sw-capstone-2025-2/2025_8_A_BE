package DiffLens.back_end.domain.rawData.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DigitalDTO {
    @JsonProperty("자주쓰는앱")
    private String frequentlyUsedApp;

    @JsonProperty("AI챗봇")
    private String aiChatbot;

    @JsonProperty("AI활용")
    private String aiUsage;

    @JsonProperty("개인정보보호")
    private String privacyProtection;
}
