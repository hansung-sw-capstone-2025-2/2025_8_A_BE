package DiffLens.back_end.domain.members.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Repository
public class RefreshTokenRepository {

    private final String REFRESH_TOKEN_REDIS_KEY = "refreshToken:";

    private final StringRedisTemplate redisTemplate;

    @Autowired
    public RefreshTokenRepository(StringRedisTemplate redisTemplate){
        this.redisTemplate = redisTemplate;
    }

    public void saveToken(Long memberId, String refreshToken, long expiration) {
        String key = REFRESH_TOKEN_REDIS_KEY + refreshToken;
        redisTemplate.opsForValue().set(key, memberId.toString(), expiration / 1000, TimeUnit.SECONDS);
        redisTemplate.opsForValue().set("member:" + memberId + ":refresh", refreshToken, expiration / 1000, TimeUnit.SECONDS);
    }

    // RefreshToken 으로 memberId 가져오기
    public Optional<Long> getMemberIdByToken(String token) {
        String key = REFRESH_TOKEN_REDIS_KEY + token;
        String memberId = redisTemplate.opsForValue().get(key);
        return memberId != null ? Optional.of(Long.parseLong(memberId)) : Optional.empty();
    }

    // RefreshToken 삭제
    public void deleteRefreshToken(String token){
        String key = REFRESH_TOKEN_REDIS_KEY + token;
        redisTemplate.delete(key);
    }

    public boolean existsRefreshToken(String token){
        String key = REFRESH_TOKEN_REDIS_KEY + token;
        return redisTemplate.hasKey(key);
    }

    // memberId로 Redis에 저장된 토큰 조회 (단순 스캔, 소규모 시스템에서만 추천)
    public String getTokenByMemberId(Long memberId) {
        for (String key : redisTemplate.keys(REFRESH_TOKEN_REDIS_KEY + "*")) {
            String value = redisTemplate.opsForValue().get(key);
            if (value != null && value.equals(memberId.toString())) {
                return key.replace(REFRESH_TOKEN_REDIS_KEY, "");
            }
        }
        return null;
    }

    // memberId로 Redis에 저장된 토큰의 남은 만료시간 반환
    public Optional<Long> getExpirationByMemberId(Long memberId) {
        String token = getTokenByMemberId(memberId);
        if (token == null) return Optional.empty();
        Long expiration = getExpirationByToken(token);
        return Optional.of(expiration);
    }

    public Long getExpirationByToken(String token){

        String key = REFRESH_TOKEN_REDIS_KEY + token;
        return redisTemplate.getExpire(key, TimeUnit.MILLISECONDS);
    }

    public void deleteByMemberId(Long memberId) {
        String refreshToken = redisTemplate.opsForValue().get("member:" + memberId + ":refresh");
        if (refreshToken != null) {
            redisTemplate.delete(REFRESH_TOKEN_REDIS_KEY + refreshToken);
            redisTemplate.delete("member:" + memberId + ":refresh");
        }
    }


}
