package DiffLens.back_end.domain.members.service.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DigestUtils;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class TokenBlackListService {

    private final RedisTemplate<String, Object> redisTemplate;

    private static final String BLACKLIST_PREFIX = "auth:blacklist:";

    /**
     * BlackList 내에 토큰 추가합니다.
     */
    public void addTokenToList(String token, long remainMillis) {
        String key = getKey(token);
        redisTemplate.opsForValue().set(key, true, remainMillis, TimeUnit.MILLISECONDS);
    }

    /**
     * BlackList 내에 토큰이 존재하는지 여부 확인
     * @param token accessToken
     * @return 토큰 존재여부 Boolean
     */
    public boolean isContainToken(String token) {
        String key = getKey(token);
        return redisTemplate.hasKey(key);
    }

    /**
     * 블랙리스트에서 제거 (필요한 경우만)
     */
    public void removeToken(String token) {
        String key = getKey(token);
        redisTemplate.delete(key);
    }

    private String getKey(String token) {
        return BLACKLIST_PREFIX + DigestUtils.sha1DigestAsHex(token);
    }

}
