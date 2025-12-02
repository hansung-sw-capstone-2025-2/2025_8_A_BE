package DiffLens.back_end.global.redis;

/**
 *
 * Redis 에서 캐시를 다루는 서비스들의 공통 인터페이스
 *
 * @param <T> Redis에서 캐시로 다룰 데이터
 * @param <R> 캐시를 저장하거나 찾는 데에 사용할 데이터
 */
public interface CacheService <T, R>{

    T getCacheInfo(R key);

    void saveCacheInfo(R data, T cacheInfo);

}
