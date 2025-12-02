package DiffLens.back_end.domain.search.service.interfaces;

import DiffLens.back_end.domain.members.entity.Member;
import DiffLens.back_end.global.redis.CacheService;

/**
 *
 * 추천 데이터 캐시를 다루는 서비스 interface
 *
 * @param <T> 캐시로 다룰 데이터
 */
public interface RecommendSearchCacheService<T> extends CacheService<T, Member> {

}
