package DiffLens.back_end.domain.members.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TokenWithRolesResponseDTO {

    private String accessToken; // 액세스 토큰
    private String refreshToken; // 리프레시 토큰
//    private List<String> roles; // 예: ["ROLE_OWNER", "ROLE_USER"]

    public static TokenWithRolesResponseDTO of(
            String accessToken, String refreshToken) {
        return new TokenWithRolesResponseDTO(accessToken, refreshToken);
    }

}
