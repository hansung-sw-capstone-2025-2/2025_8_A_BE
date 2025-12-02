package DiffLens.back_end.global.redis;

import java.util.function.Supplier;

public interface RedisCacheRepository<R> {

    // 저장
    boolean save(String key, Object value, CacheInfo cacheInfo);

    // 조회
    <T> T findByKey(String key);

    // 조회했는데 없을 시
    <T> T findOrElse(String key, Supplier<T> supplier, CacheInfo cacheInfo);

    // 삭제
    boolean delete(String key);

}
