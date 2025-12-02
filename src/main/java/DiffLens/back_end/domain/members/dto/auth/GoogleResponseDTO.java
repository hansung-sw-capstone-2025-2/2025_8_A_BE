package DiffLens.back_end.domain.members.dto.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

public class GoogleResponseDTO {

    @Getter
    @Setter
    public static class GoogleTokenResponseDto{
        @JsonProperty("access_token")
        private String accessToken;
        @JsonProperty("token_type")
        private String tokenType;
        @JsonProperty("expires_in")
        private Long expiresIn;
        @JsonProperty("scope")
        private String scope;
        @JsonProperty("id_token")
        private String idToken;
    }

    @Getter
    @Setter
    @ToString
    public static class GoogleUserInfoResponseDTO {

        private String id;
        private String email;
        private String name;
        private String given_name;
        private String family_name;
        private String picture;
        private String locale;

    }

}
