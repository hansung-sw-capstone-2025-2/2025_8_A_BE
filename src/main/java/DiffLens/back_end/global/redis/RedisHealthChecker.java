package DiffLens.back_end.global.redis;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class RedisHealthChecker {

    private final RedisTemplate<String, Object> redisTemplate;

    @PostConstruct
    public void checkRedisConnection() {
        log.info("[Connection Check] Redis 연결 체크...");
        try {
            String ping = redisTemplate.getConnectionFactory() != null ? redisTemplate.getConnectionFactory()
                    .getConnection()
                    .ping() : "null";
            if(!"PONG".equals(ping)) {
                log.error("[Connection Check] Redis 연결 실패 ❌ - 응답값이 'PONG' 이 아님");
                throw new IllegalStateException("Redis 연결 실패: PING 응답 이상. 응답=" + ping);
            }
        }catch (Exception e) {
            log.error("[Connection Check] Redis 연결 실패 ❌ - {}", e.getMessage());
            throw new IllegalStateException("Redis 연결 실패: " + e.getMessage(), e);
        }
        log.info("[Connection Check] Redis 연결 성공 ✅");
    }

}
