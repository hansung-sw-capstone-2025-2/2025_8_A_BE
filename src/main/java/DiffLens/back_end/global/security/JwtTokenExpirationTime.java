package DiffLens.back_end.global.security;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum JwtTokenExpirationTime {

    ACCESS_TOKEN(1000L * 60 * 60 * 9), // 60분 (임시 9시간)
    REFRESH_TOKEN(1000L * 60 * 60 * 24 * 14),
    ;

    private final long expirationMillis;

}
