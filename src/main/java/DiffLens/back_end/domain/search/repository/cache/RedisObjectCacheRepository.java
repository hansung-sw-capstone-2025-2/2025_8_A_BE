package DiffLens.back_end.domain.search.repository.cache;

import DiffLens.back_end.global.redis.CacheInfo;
import DiffLens.back_end.global.redis.RedisCacheRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.function.Supplier;

/**
 * 캐시 데이터를 관리하는 redis Repository
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class RedisObjectCacheRepository implements RedisCacheRepository<Object> {

    private final RedisTemplate<String, Object> redisTemplate;

    // 저장
    @Override
    public boolean save(String key, Object value, CacheInfo cacheInfo) {
        try{
            redisTemplate.opsForValue().set(key, value, cacheInfo.getTTL(), cacheInfo.getTimeUnit());
        }catch(Exception e){
            log.error("[캐싱] 캐시 저장 중 오류 발생 - {}", e.getMessage());
            return false;
        }
        return true;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T findByKey(String key){
        return (T) redisTemplate.opsForValue().get(key);
    }

    @Override
    public <T> T findOrElse(String key, Supplier<T> supplier, CacheInfo cacheInfo) {
        T value = findByKey(key);
        if (value == null) {
            value = supplier.get();
            save(key, value, cacheInfo);
        }
        return value;
    }

    @Override
    public boolean delete(String key) {
        try{
            redisTemplate.delete(key);
        }catch(Exception e){
            log.error("[캐싱] 캐시 삭제 중 오류 발생 - {}", e.getMessage());
            return false;
        }
        return true;
    }

}
