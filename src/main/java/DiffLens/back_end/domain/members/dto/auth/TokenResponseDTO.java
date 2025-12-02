package DiffLens.back_end.domain.members.dto.auth;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TokenResponseDTO {

    private final String accessToken;
    private final String refreshToken;

    public static TokenResponseDTO of(final String accessToken, final String refreshToken) {
        return new TokenResponseDTO(accessToken, refreshToken);
    }

}
