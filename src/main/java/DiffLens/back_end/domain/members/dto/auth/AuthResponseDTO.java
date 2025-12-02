package DiffLens.back_end.domain.members.dto.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class AuthResponseDTO {

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class LoginDto{

        @JsonProperty(value = "access_token")
        private String accessToken;
        @JsonProperty(value = "refresh_token")
        private String refreshToken;

    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SignUpDto{

        @JsonProperty(value = "is_success")
        private Boolean isSuccess;

    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ReIssue{

        @JsonProperty(value = "access_token")
        private String accessToken;

    }

}
