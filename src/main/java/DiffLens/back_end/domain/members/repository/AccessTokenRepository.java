package DiffLens.back_end.domain.members.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Repository
@RequiredArgsConstructor
public class AccessTokenRepository {

    private final StringRedisTemplate redisTemplate;

    private final String ACCESS_TOKEN_KEY = "accessToken:";
    private final String MEMBER_ACCESS_TOKEN_KEY = "memberAccessToken:";

    public void saveToken(Long memberId, String accessToken, long expirationMillis) {
        // 기존 memberAccessToken 삭제
        String oldToken = redisTemplate.opsForValue().get(MEMBER_ACCESS_TOKEN_KEY + memberId);
        if (oldToken != null) {
            redisTemplate.delete(ACCESS_TOKEN_KEY + oldToken);
        }

        // 새 토큰 저장
        redisTemplate.opsForValue().set(ACCESS_TOKEN_KEY + accessToken, memberId.toString(), expirationMillis, TimeUnit.MILLISECONDS);
        redisTemplate.opsForValue().set(MEMBER_ACCESS_TOKEN_KEY + memberId, accessToken, expirationMillis, TimeUnit.MILLISECONDS);
    }

    public Optional<String> getCurrentToken(Long memberId) {
        return Optional.ofNullable(redisTemplate.opsForValue().get(MEMBER_ACCESS_TOKEN_KEY + memberId));
    }

    public void deleteToken(String accessToken) {
        String memberId = redisTemplate.opsForValue().get(ACCESS_TOKEN_KEY + accessToken);
        if (memberId != null) {
            redisTemplate.delete(MEMBER_ACCESS_TOKEN_KEY + memberId);
        }
        redisTemplate.delete(ACCESS_TOKEN_KEY + accessToken);
    }

    public boolean exists(String accessToken) {
        return redisTemplate.hasKey(ACCESS_TOKEN_KEY + accessToken);
    }
}